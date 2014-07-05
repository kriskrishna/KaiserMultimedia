#!/bin/bash

# Usage: kpwpce-image.
# This script takes a vanilla RHEL / CentOS-compatible Linux and installs
# and configures software that makes up kpwpce.

# Configurable variables.

CLOUSE_VERSION=1.0rc.2.0
MYSQL_VERSION=5.5.36
CLOUSE_ONLY=

# Constants.

DOWNLOAD_URL=http://s3.amazonaws.com/mybucket
DOWNLOAD_PREFIX=myclouselocation
DOWNLOAD_IMAGE=myimageutilslocation

WGTH="--header=X-Google-Metadata-Request: True"

set -e

# Cloud provider: the script currently supports Amazon EC2 and Google Compute
# Engine.  Use ec2 or gce, correspondingly.

if wget -O /dev/null -q "$WGTH" http://metadata/computeMetadata/v1/instance/id; then
    CLOUD_PROVIDER=gce
else
    CLOUD_PROVIDER=ec2
fi

# Auto-detect platform.

ARC=$(uname -m)
if [[ "$ARC" == "x86_64" ]]; then
    OSPLAT=x64
elif [[ "$ARC" == "i686" ]]; then 
    OSPLAT=x86 
else
    echo "ERROR: Unsupported platform '$ARC'" >&2
    exit 1
fi

if [ "$CLOUD_PROVIDER" = "gce" ]; then
    # Fix a problem with incorrect time.
    # Looks like it's fixed in the newer images, consider removing it.
    sed -e "s/LOCAL/UTC/" < /etc/adjtime > adjtime
    sudo cp adjtime /etc/adjtime
    rm adjtime

    # Disable SELinux
    sed -e "s/SELINUX=enforcing/SELINUX=disabled/" < /etc/selinux/config > config
    sudo cp config /etc/selinux/config
    rm config

    # Disable firewall
    sudo iptables -F
    sudo /etc/init.d/iptables save
fi

# Install binutils to get 'strings' command.

sudo yum install -y binutils

# Download MySQL.

MYSQL_DISTR=mysql-$MYSQL_VERSION-linux2.6-$ARC
MYSQL_REL=5.5

if [[ $MYSQL_VERSION == 5.6.* ]]; then
    MYSQL_DISTR=mysql-$MYSQL_VERSION-linux-glibc2.5-$ARC
    MYSQL_REL=5.6
fi

MYSQL_DISTR_TGZ=$MYSQL_DISTR.tar.gz

MYSQL_DOWNLOAD_URL=http://downloads.mysql.com/archives/mysql-$MYSQL_REL/$MYSQL_DISTR_TGZ

if ! wget --spider -q "$MYSQL_DOWNLOAD_URL"; then
    MYSQL_DOWNLOAD_URL=http://cdn.mysql.com/Downloads/MySQL-$MYSQL_REL/$MYSQL_DISTR_TGZ
fi

if ! wget --spider -q "$MYSQL_DOWNLOAD_URL"; then
    echo "ERROR: Cannot figure out download URL" >&2
    exit 1
fi

wget -O "$MYSQL_DISTR_TGZ" "$MYSQL_DOWNLOAD_URL"

tar xzf "$MYSQL_DISTR_TGZ"
rm "$MYSQL_DISTR_TGZ"

# Create MySQL user.

sudo useradd -d /usr/local/mysql -s /sbin/nologin -c "MySQL server" -r -U mysql

# Deploy MySQL.

sudo mv "$MYSQL_DISTR" /usr/local
sudo chown -R mysql:mysql /usr/local/"$MYSQL_DISTR"
sudo ln -s /usr/local/"$MYSQL_DISTR" /usr/local/mysql

cat << 'EOF' > my.cnf
[client]
socket=/var/run/mysqld/mysqld.sock
[mysqld]
socket=/var/run/mysqld/mysqld.sock
skip-networking
EOF

sudo cp my.cnf /etc/my.cnf

cd /usr/local/mysql
sudo scripts/mysql_install_db --user=mysql
sudo cp support-files/mysql.server /etc/init.d/mysql
sudo chkconfig --add mysql

cd

echo >> .bash_profile
echo 'PATH=$PATH:/usr/local/mysql/bin' >> .bash_profile

# Start MySQL.

sudo /etc/init.d/mysql start

# Download ClouSE.

cd

CLOUSE_REL_NAME=clouse-$CLOUSE_VERSION-linux-$OSPLAT
wget $DOWNLOAD_URL/$DOWNLOAD_PREFIX/$CLOUSE_REL_NAME.tar.gz
tar xzf $CLOUSE_REL_NAME.tar.gz
rm $CLOUSE_REL_NAME.tar.gz

# Deploy ClouSE.

if [[ ! -f "$CLOUSE_REL_NAME/ha_clouse-$MYSQL_VERSION.so" ]]; then
    echo "ERROR: $CLOUSE_REL_NAME/ha_clouse-$MYSQL_VERSION.so is not found" >&2
    exit 1
fi

MYSQL_PLUGIN_DIR=/usr/local/mysql/lib/plugin

if [[ ! -d "$MYSQL_PLUGIN_DIR" ]]; then
    echo "ERROR: $MYSQL_PLUGIN_DIR is not found!!!" >&2
    exit 1
fi

sudo cp "$CLOUSE_REL_NAME/clouse.so" "$MYSQL_PLUGIN_DIR/"
sudo cp "$CLOUSE_REL_NAME/ha_clouse-$MYSQL_VERSION.so" "$MYSQL_PLUGIN_DIR/"
sudo ln -s "$MYSQL_PLUGIN_DIR/ha_clouse-$MYSQL_VERSION.so" "$MYSQL_PLUGIN_DIR/ha_clouse.so"

# Install ClouSE.

PATH=$PATH:/usr/local/mysql/bin
CLOUSE_TMP_FILE=/tmp/clouse_tmp

echo "clouse_cloud_data_url=file://$CLOUSE_TMP_FILE/db" >> my.cnf

sudo cp my.cnf /etc/my.cnf
rm my.cnf

MYSQL_INSTALL_SCRIPT="INSTALL PLUGIN ClouSE SONAME 'ha_clouse.so';INSTALL PLUGIN CLOUSE_TABLES SONAME 'ha_clouse.so';"
echo "$MYSQL_INSTALL_SCRIPT" | mysql -u root

# Download ClouSE utils.

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/.clouse_check
wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/clouse-config
wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/ec2-my.cnf

chmod 755 .clouse_check
echo >> .bash_profile
echo '~/.clouse_check' >> .bash_profile

chmod 755 clouse-config
sudo cp clouse-config /etc/init.d/clouse-config
rm clouse-config
sudo chkconfig --add clouse-config

sudo cp ec2-my.cnf /etc/my.cnf
rm ec2-my.cnf

echo '##########################################'
echo 'ClouSE installed'
echo

if [[ $CLOUSE_ONLY ]]; then
    sudo /etc/init.d/mysql stop
    sudo rm -rf $CLOUSE_TMP_FILE
    exit 0
fi

# Install Webmin.

sudo yum -y install perl-Net-SSLeay

cat << 'EOF' > webmin.repo
[Webmin]
name=Webmin Distribution Neutral
#baseurl=http://download.webmin.com/download/yum
mirrorlist=http://download.webmin.com/download/yum/mirrorlist
enabled=1
EOF

sudo cp webmin.repo /etc/yum.repos.d/webmin.repo
rm webmin.repo

wget http://www.webmin.com/jcameron-key.asc
sudo rpm --import jcameron-key.asc
rm jcameron-key.asc

sudo yum -y install webmin

# Configure Webmin ports.

function config_webmin_var {
    # Usage: config_webmin_var file key value
    # WARNING: arguments are not escaped for sed

    if ! sudo grep -q "^$2=" $1; then
        echo "ERROR: $2 is not found in $1" >&2
        exit 1
    fi

    sudo cat $1 | sed -e "s@^$2=.*@$2=$3@" > /tmp/`basename $1`
    sudo cp /tmp/`basename $1` $1
    rm /tmp/`basename $1`
}

WEBMIN_CONF=/etc/webmin/miniserv.conf

config_webmin_var "$WEBMIN_CONF" port   12321
config_webmin_var "$WEBMIN_CONF" listen 12321

# Configure Webmin MySQL module.

WEBMIN_MYSQL=/etc/webmin/mysql/config
MYSQL_BIN=/usr/local/mysql/bin

config_webmin_var "$WEBMIN_MYSQL" mysql_data    "/usr/local/mysql/data"

config_webmin_var "$WEBMIN_MYSQL" stop_cmd      "/etc/init.d/mysql stop"
config_webmin_var "$WEBMIN_MYSQL" start_cmd     "/etc/init.d/mysql start"

config_webmin_var "$WEBMIN_MYSQL" mysqldump     "$MYSQL_BIN/mysqldump"
config_webmin_var "$WEBMIN_MYSQL" mysqlimport   "$MYSQL_BIN/mysqlimport"
config_webmin_var "$WEBMIN_MYSQL" mysqlshow     "$MYSQL_BIN/mysqlshow"
config_webmin_var "$WEBMIN_MYSQL" mysql         "$MYSQL_BIN/mysql"
config_webmin_var "$WEBMIN_MYSQL" mysqladmin    "$MYSQL_BIN/mysqladmin"

config_webmin_var /etc/webmin/installed.cache mysql 1

# Add MySQL log to the syslog.

cp /etc/rsyslog.conf rsyslog.conf
echo "*.none  /usr/local/mysql/data/mysqld.err" >> rsyslog.conf
sudo cp rsyslog.conf /etc/rsyslog.conf
rm rsyslog.conf

# Install Apache.

sudo yum -y install httpd mod_ssl
sudo chkconfig --levels 2345 httpd on

# Configure options and overrides.

HTTPD_CONF=/etc/httpd/conf/httpd.conf

if ! grep -q '^<Directory "/var/www/html">$' $HTTPD_CONF; then
    echo "ERROR: directory /var/www/html is not found in $HTTPD_CONF" >&2
    exit 1
fi

sed -re "/^<Directory \"\/var\/www\/html\">$/,/^<\/Directory>$/s/(^\s*Options).*/\1 FollowSymlinks/" -e "/^<Directory \"\/var\/www\/html\">$/,/^<\/Directory>$/s/(^\s*AllowOverride).*/\1 All/" < $HTTPD_CONF > httpd.conf

sudo cp httpd.conf $HTTPD_CONF
rm httpd.conf

config_webmin_var /etc/webmin/installed.cache apache 1

# Install PHP.

sudo yum -y install php php-mysql php-gd php-xml php-pecl-apc

# Configure PHP.

PHP_INI=/etc/php.ini

if ! grep -q "^upload_max_filesize\s*=" $PHP_INI || ! grep -q "^post_max_size\s*=" $PHP_INI || ! grep -q "^mysql.*default_socket\s*=" $PHP_INI; then
    echo "ERROR: upload size and MySQL options are not found in $PHP_INI" >&2
    exit 1
fi

sed -e "s/^upload_max_filesize\s*=.*/upload_max_filesize = 16M/" -e "s/^post_max_size\s*=.*/post_max_size = 16M/" -re "s@^(mysql.*default_socket\s*=).*@\1 /var/run/mysqld/mysqld.sock@" < $PHP_INI > php.ini
sudo cp php.ini $PHP_INI
rm php.ini

config_webmin_var /etc/webmin/installed.cache phpini 1

# Install / enable EPEL repo.

if [ "$CLOUD_PROVIDER" = "gce" ]; then
    sudo rpm -Uvh http://mirrors.kernel.org/fedora-epel/6/i386/epel-release-6-8.noarch.rpm
else
    EPEL_REPO=/etc/yum.repos.d/epel.repo

    if ! sed -ne "/^\[epel\]$/,/^\[.*\]$/p" $EPEL_REPO | grep -q "^enabled="; then
        echo "ERROR: options not found in $EPEL_REPO" >&2
        exit 1
    fi

    sed -e "/^\[epel\]$/,/^\[.*\]$/s/^enabled=.*/enabled=1/" $EPEL_REPO > epel.repo
    sudo cp epel.repo $EPEL_REPO
    rm epel.repo
fi

# Install phpMyAdmin.

sudo yum -y install phpmyadmin

# Configure phpMyAdmin to run on port 12322.

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/phpMyAdmin.conf
sudo cp phpMyAdmin.conf /etc/httpd/conf/phpMyAdmin.conf
rm phpMyAdmin.conf

#sudo rm /etc/httpd/conf.d/phpMyAdmin.conf

cp $HTTPD_CONF httpd.conf

cat << 'EOF' >> httpd.conf

#
# phpMyAdmin configuration (runs over SSL on port 12322).
#

Include conf/phpMyAdmin.conf
EOF

sudo cp httpd.conf $HTTPD_CONF
rm httpd.conf

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/config.inc.php
sudo cp config.inc.php /etc/phpMyAdmin/config.inc.php
rm config.inc.php

# Configure 1GB swap file.

sudo dd if=/dev/zero of=/swapfile bs=1024 count=1048576
sudo chown root:root /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
cp /etc/fstab fstab
echo "/swapfile   swap        swap    defaults        0   0" >> fstab
sudo cp fstab /etc/fstab
rm fstab

# Download ClouSE Web Configurator.

cd
mkdir cwc
cd cwc

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/cwc/cwc.zip
wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/cwc/$OSPLAT/clouse-config

unzip cwc.zip

# Install ClouSE Web Configurator.

sudo cp -R www/clouse-web-config /var/www
sudo cp www/cwc-index.php /var/www/html/cwc-index.php
echo "DirectoryIndex cwc-index.php index.php" > .htaccess
sudo cp .htaccess /var/www/html/.htaccess

# HACK: anchor to WebDAV module configuration to insert alias for CWC

if ! grep -q "^# WebDAV module" $HTTPD_CONF; then
    echo "ERROR: couldn't find place to insert CWC alias in $HTTPD_CONF" >&2
    exit 1
fi

sed -e "/^# WebDAV module/i \
# Alias for ClouSE Web Configurator.\\
#\\
\\
Alias /clouse-web-config/ \"/var/www/clouse-web-config/\"\\
\\
<Directory \"/var/www/clouse-web-config\">\\
    Options FollowSymLinks\\
    AllowOverride None\\
</Directory>\\
\\
#" < $HTTPD_CONF > httpd.conf
sudo cp httpd.conf $HTTPD_CONF
rm httpd.conf

sudo mkdir /usr/local/clouse-web-config
sudo cp bin/* /usr/local/clouse-web-config/
sudo cp clouse-config /usr/local/clouse-web-config/
sudo chmod 4755 /usr/local/clouse-web-config/clouse-config
sudo cp ~/$CLOUSE_REL_NAME/wscmd /usr/local/clouse-web-config/

echo 'GRANT SUPER, PROCESS, RELOAD ON *.* TO "clouse_admin"@"localhost";FLUSH PRIVILEGES;' | mysql -u root

# Generate certificates such that gen-cert can override.

CERT_RDN="/O=WordPress-to-Cloud/CN=*/"
/usr/bin/openssl req -newkey rsa:2048 -keyout ec2-auto-gen.key -nodes -x509 -days 3650 -out ec2-auto-gen.crt -multivalue-rdn -subj "$CERT_RDN" 2> /dev/null
/usr/bin/openssl req -newkey rsa:2048 -keyout miniserv.pem -nodes -x509 -days 3650 -out miniserv.pem -multivalue-rdn -subj "$CERT_RDN" 2> /dev/null

CRTROOT=/etc/pki/tls
CRTFILE=$CRTROOT/certs/ec2-auto-gen.crt
KEYFILE=$CRTROOT/private/ec2-auto-gen.key
WEBMINKEY=/etc/webmin/miniserv.pem

sudo cp ec2-auto-gen.crt $CRTFILE
sudo chmod 600 $CRTFILE

sudo cp ec2-auto-gen.key $KEYFILE
sudo chmod 600 $KEYFILE

sudo cp miniserv.pem $WEBMINKEY
sudo chmod 600 $WEBMINKEY

# Update paths in the httpd configuration.

function update_cert_paths {
    # Usage: update_cert_paths <file.conf>

    if ! sudo grep -q "^SSLCertificateFile " $1 || ! grep -q  "^SSLCertificateKeyFile " $1; then
        echo "ERROR: SSLCertificateFile is not found in $1" >&2
        exit 1
    fi

    sed -e "s@^SSLCertificateFile .*@SSLCertificateFile $CRTFILE@" -e "s@^SSLCertificateKeyFile .*@SSLCertificateKeyFile $KEYFILE@" < $1 > /tmp/`basename $1`
    sudo cp /tmp/`basename $1` $1
    rm /tmp/`basename $1`
}

update_cert_paths /etc/httpd/conf.d/ssl.conf
update_cert_paths /etc/httpd/conf/phpMyAdmin.conf

# Install logrotate configuration.

cat << 'EOF' > mysql
/usr/local/mysql/data/mysqld.err {
    rotate 10
    missingok
    size 1M
    create 600 mysql root
    postrotate
        /usr/local/mysql/bin/mysqladmin -u clouse_admin flush-logs
    endscript
}
EOF

sudo cp mysql /etc/logrotate.d
rm mysql

# Install boot scripts.

sudo cp clouse-initdb /etc/init.d/clouse-initdb
sudo chkconfig --add clouse-initdb

sudo cp gen-cert /etc/init.d/gen-cert
sudo chkconfig --add gen-cert

cd
rm -rf cwc

# Install XHProf.

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/xhprof/$OSPLAT/xhprof.so
chmod 755 xhprof.so
sudo cp xhprof.so /usr/lib64/php/modules/
rm xhprof.so

cat << 'EOF' > xhprof.ini
; Enable XHProf extension module
;extension=xhprof.so
;xhprof.output_dir=/var/www/xhprof/traces
EOF

sudo cp xhprof.ini /etc/php.d/
rm xhprof.ini

wget http://pecl.php.net/get/xhprof-0.9.3.tgz
tar xzf xhprof-0.9.3.tgz
sudo cp -R xhprof-0.9.3 /var/www/xhprof
sudo mkdir /var/www/xhprof/traces
sudo chown apache:apache /var/www/xhprof/traces
rm -rf xhprof*
rm package.xml

# Download WordPress.

cd
mkdir wordpress
cd wordpress

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/wordpress/wordpress.zip
unzip wordpress.zip

# Deploy WordPress.

sudo mv wordpress /var/www/wordpress

cp /var/www/html/.htaccess .htaccess

cat << 'EOF' >> .htaccess

# BEGIN WordPress
<IfModule mod_rewrite.c>
RewriteEngine On
RewriteBase /
RewriteRule ^index\.php$ - [L]
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule . /index.php [L]
</IfModule>
# END WordPress
EOF

sudo mv .htaccess /var/www/wordpress/.htaccess

wget http://s.wordpress.org/favicon.ico
sudo mv favicon.ico /var/www/wordpress/favicon.ico

sudo chown -R apache:apache /var/www/wordpress

sudo cp /var/www/html/cwc-index.php /var/www/wordpress/cwc-index.php

# Install WordPress.

if ! grep -q '^<Directory "/var/www/html">$' $HTTPD_CONF; then
    echo "ERROR: directory /var/www/html is not found in $HTTPD_CONF" >&2
    exit 1
fi

sed -e "s@/var/www/html@/var/www/wordpress@" < $HTTPD_CONF > httpd.conf

sudo cp httpd.conf $HTTPD_CONF
rm httpd.conf
sudo rm -rf /var/www/html

# Create a cron entry to run every 10 minutes.

if [[ ! -x /usr/bin/php ]]; then
    echo "ERROR: /usr/bin/php is not found" >&2
    exit 1
fi

echo '*/10 * * * * cd /var/www/wordpress; /usr/bin/php /var/www/wordpress/wp-cron.php > /dev/null 2>&1' | sudo crontab -u apache -

# Create WordPress database.

MYSQL_WORDPRESS_SCRIPT='CREATE DATABASE wordpress_clouse;GRANT ALL PRIVILEGES ON wordpress_clouse.* TO "wordpress_user"@"localhost";GRANT ALL PRIVILEGES ON wordpress_clouse.* TO "clouse_admin"@"localhost";FLUSH PRIVILEGES;'

echo "$MYSQL_WORDPRESS_SCRIPT" | mysql -u root

# Install kpwpce bootstrap.

wget $DOWNLOAD_URL/$DOWNLOAD_IMAGE/wordpress/init-db.tgz
tar xzf init-db.tgz
sudo cp init-db.sql /usr/local/clouse-web-config/init-db.sql

echo 'SELECT * FROM wordpress_clouse.wp_users LIMIT 1;' > check-db.sql
sudo cp check-db.sql /usr/local/clouse-web-config/check-db.sql

sudo cp check-tables.lst /usr/local/clouse-web-config/check-tables.lst

# Add WordPress-specific custom commands to Webmin.

cat << 'EOF' > 1359253645.edit
/var/www/wordpress/wp-config.php
 wp-config.php 





0
0


EOF

cat << 'EOF' > 1359253645.html

Edit WordPress configuration file
EOF

sudo cp 1359253645.* /etc/webmin/custom
rm 1359253645.*

cat << 'EOF' > 1360278424.edit
/var/www/wordpress/.htaccess
 .htaccess 





0
0


EOF

cat << 'EOF' > 1360278424.html

Edit WordPress .htaccess file
EOF

sudo cp 1360278424.* /etc/webmin/custom
rm 1360278424.*

cat << 'EOF' > 1374788081.cmd
chown -R apache:apache /var/www/wordpress
 Fix www permissions 
* 0 0 0 0 0 0 0 -
EOF

cat << 'EOF' > 1374788081.html

Fix ownership of webserver root directory
EOF

sudo cp 1374788081.* /etc/webmin/custom
rm 1374788081.*

cd
rm -rf wordpress

# Remove temporary ClouSE setup.

sudo /etc/init.d/mysql stop
sudo rm -rf $CLOUSE_TMP_FILE

# Self-destruct

rm yapixx-image


# rdbms-to-hdfs
Apache spark code to extract data from a JDBC relational database to HDFS

## Setup

using MySql with some opendata example datasets
CDH 5.8(local install on my laptop following [https://github.com/krisgeus/ansible_local_cdh_hadoop](https://github.com/krisgeus/ansible_local_cdh_hadoop)

### MySql
Inspired by blog of joe fallon: [http://blog.joefallon.net/2013/10/install-mysql-on-mac-osx-using-homebrew/](http://blog.joefallon.net/2013/10/install-mysql-on-mac-osx-using-homebrew/)
brew install mysql
mysql.server restart

DONT!!!mysql_secure_installation  
DONT!!!unset TMPDIR  
DONT!!!mysql_install_db --verbose --user=`whoami` --basedir="$(brew --prefix mysql)" --datadir=/usr/local/var/mysql --tmpdir=/tmp

```bash
mysql -u root
```

```sql
create database sourcedb;

create user 'sourcedb'@'localhost' identified by 'sourcedb';

grant all on sourcedb.* to 'sourcedb'@'localhost';

exit
```

```bash
mysql -u sourcedb -D sourcedb -p
```

#### Sample databases

[https://www.ntu.edu.sg/home/ehchua/programming/sql/SampleDatabases.html](https://www.ntu.edu.sg/home/ehchua/programming/sql/SampleDatabases.html)

download and extract sakila.tar.gz

```bash
git clone https://github.com/datacharmer/test_db.git

cd test-db
mysql -u root < employees.sql

mysql -u root
```

```sql
SOURCE /Users/kgeusebroek/dev/xebia/godatadriven/projects/sourcedbload/sampledb/sakila-db/sakila-schema.sql

SOURCE /Users/kgeusebroek/dev/xebia/godatadriven/projects/sourcedbload/sampledb/sakila-db/sakila-data.sql

grant all on sakila.* to 'sourcedb'@'localhost';

grant all on employees.* to 'sourcedb'@'localhost';

exit
```

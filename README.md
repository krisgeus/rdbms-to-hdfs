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

## Zeppelin notebook for exploration

In the notebooks directory we have a zeppelin notebook with the experimentation code

The following settings are needed to make this work:

```xml
<property>
  <name>zeppelin.notebook.dir</name>
  <value>${git clone dir of rdbms-to-hdfs}/notebooks/</value>
  <description>path or URI for notebook persist</description>
</property>
```

Inside the notebook some specific spark interperter settings are mentioned.
this mainly consists of adding the needed dependencies for making the jdbc connection.

Since we use mysql we use the mysql:mysql-connector-java:6.0.3

```

## Running the steps

### Step1a Full load of tables to parquet files on HDFS
spark-submit --class nl.krisgeus.jdbc.JdbcMain target/scala-2.10/rdbms-to-hdfs-assembly-0.1-SNAPSHOT.jar -s 0 -c employees -o /tmp/sourcedb

or

spark-submit --class nl.krisgeus.jdbc.JdbcMain target/scala-2.10/rdbms-to-hdfs-assembly-0.1-SNAPSHOT.jar --step 0 --config employees --output /tmp/sourcedb

#!/bin/bash

db_user=spring_user
db_pass='pass'
db_name=db_spring
netbeans19=apache-netbeans_19-1_all.deb 

if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

clear

read -p "Введіть ім'я користувача Postgres SQL: " db_user

if [ "$db_user" = "" ]; then
	echo "Ім'я користувача не може бути пустим!"
	exit 1
else
    echo "Прийнято"
fi

read -sp "Введіть пароль для $db_user: " db_pass

if [ "$db_pass" = "" ]; then
	echo "Пароль не може бути пустим!"
	exit 1
else
    echo "Прийнято"
fi

read -p "Введіть ім'я бази данних Postgres SQL: " db_name

if [ "$db_name" = "" ]; then
	echo "Ім'я бази даних не може бути пустим!"
	exit 1
else
    echo "Прийнято"
fi

echo "************************** 
Ім'я бази даних: $db_name
Користувач: $db_user
Пароль: *******
"

sudo apt update

echo "******************************************************************************
*                  встановлення Java JDK 21
******************************************************************************"
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "System has no java"
    sudo apt install openjdk-21-jdk -y
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" > "17" ]]; then
        echo version is higher than 17
    else         
        echo version is 17 or less, updating to 21
	sudo apt install openjdk-21-jdk -y
    fi
fi


echo "******************************************************************************
*                  встановлення бази даних Postgres SQL
******************************************************************************"

sudo apt install postgresql postgresql-contrib -y

echo "******************************************************************************
*                  налаштування Postgres SQL
******************************************************************************"

currentuser=$(stat -c "%G" .)

sudo usermod -aG $currentuser postgres

echo "SELECT 'CREATE DATABASE $db_name' WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = '$db_name')\gexec

DO
\$do\$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles
      WHERE  rolname = '$db_user') THEN
      CREATE ROLE $db_user LOGIN PASSWORD '$db_pass';
   ELSE
   	RAISE NOTICE 'Користувач "$db_user" Вже існує в базі даних. Пропущено.';
   END IF;
END
\$do\$;

GRANT ALL PRIVILEGES ON DATABASE $db_name TO $db_user;"  | sudo tee /opt/createdb.sql >/dev/null

#sudo -i -u postgres psql -version

sudo -i -u postgres psql -f /opt/createdb.sql

echo "******************************************************************************
*                  встановлення curl
******************************************************************************"

sudo apt install curl -y

echo "******************************************************************************
*                  завантаження Apache NetBeans 19
******************************************************************************"
if [ -f ./$netbeans19 ]; then
	echo "середовище вже завантажено"
else 
	curl https://archive.apache.org/dist/netbeans/netbeans-installers/19/$netbeans19 --output $netbeans19
fi

echo "******************************************************************************
*                  встановлення Apache NetBeans 19
******************************************************************************"

sudo dpkg -i $netbeans19

echo "******************************************************************************
*                  встановлення Github client
******************************************************************************"

sudo apt install git -y

echo "******************************************************************************
*                  Клонування Spring Web-Service із Github
******************************************************************************"
if [ -e ./SpringWS ]; then
	echo "проєкт SpringWS вже існує, клонування пропущено"
else 
	git clone https://github.com/Wishmaster-sa/SpringWS.git

	sudo chown -R $currentuser:$currentuser ./SpringWS
	sudo mv ./SpringWS/maven ./SpringWS/.mvn
fi


echo "******************************************************************************
*                  Налаштування проєкта
******************************************************************************"
propertiesFile="./SpringWS/src/main/resources/application.properties"  

old_user=$(cat $propertiesFile | \
  awk -F'[=]' '$1 == "spring.datasource.username" {print "username="$2}' | sed 's/.*=//')

sed -i "s/spring.datasource.username=$old_user/spring.datasource.username=$db_user/g" $propertiesFile

old_pass=$(cat $propertiesFile | \
  awk -F'[=]' '$1 == "spring.datasource.password" {print "username="$2}' | sed 's/.*=//')

sed -i "s/spring.datasource.password=$old_pass/spring.datasource.password=$db_pass/g" $propertiesFile

old_base=$(cat $propertiesFile |   awk -F'[=]' '$1 == "spring.datasource.url" {print "db_name="$2}' | sed 's/.*\///')

sed -i "s/$old_base/$db_name/g" $propertiesFile

cd ./SpringWS
sudo bash mvnw -N wrapper:wrapper


echo "******************************************************************************
*                  ВІТАЄМО! РОЗГОРТАННЯ СЕРЕДОВИЩА ПРОЄКТУ ЗАВЕРШЕНО!
******************************************************************************"


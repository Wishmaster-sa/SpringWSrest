# Розгортання вебсрвісу в Docker

## Вимоги

| ПЗ               |   Версія   | Примітка                     |
|:-----------------|:----------:|------------------------------|
| PostgreSQL       |            |                              |
| Docker           | **20.10+** |                              |
| Docker Compose   |   10.5+    | Якщо планується використання |
| Git              |            | Для клонування репозиторію   |

## Змінні оточення

Вебсервіс підтримує конфігурацію через змінні оточення. 

Нижче наведено основні параметри:

### Змінні оточення для конфігурації Spring REST Webservice

- `SPRING_APPLICATION_NAME`: Назва застосунку Spring Boot, яка відображається у логах та активах.

- `SPRING_DATASOURCE_URL`: URL підключення до бази даних PostgreSQL.
- `SPRING_DATASOURCE_USERNAME`: Ім'я користувача для підключення до бази даних.
- `SPRING_DATASOURCE_PASSWORD`: Пароль користувача бази даних.
- `SPRING_DATASOURCE_DRIVER-CLASS-NAME`: Клас JDBC-драйвера для підключення до PostgreSQL (наприклад, `org.postgresql.Driver`).

- `SPRING_JPA_HIBERNATE_DDL-AUTO`: Режим ініціалізації бази даних Hibernate (`none`, `validate`, `update`, `create`, `create-drop`).
- `SPRING_JPA_DATABASE`: Тип бази даних (`postgresql`, `mysql`, тощо).
- `SPRING_JPA_DATABASE-PLATFORM`: Діалект Hibernate, що використовується (наприклад, `org.hibernate.dialect.PostgreSQLDialect`).
- `SPRING_JPA_SHOW-SQL`: Виводити SQL-запити в лог (`true` або `false`).
- `SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL`: Форматувати SQL-запити у логах (`true` або `false`).

- `WEBSERVICE_SETTINGS_LOGLEVEL`: Рівень логування вебсервісу (0 — вимкнено, 1 — лише помилки, 2 — повне логування).
- `WEBSERVICE_SETTINGS_LOGFILENAME`: Повний шлях до лог-файлу, у який записуються логи застосунку.

- `SERVER_SSL_KEY-STORE-TYPE`: Тип сховища ключів SSL (наприклад, `PKCS12` або `JKS`).
- `SERVER_SSL_KEY-STORE`: Шлях до key-store (наприклад, `classpath:keystore/keystore.p12`).
- `SERVER_SSL_KEY-STORE-PASSWORD`: Пароль доступу до key-store.
- `SERVER_SSL_TRUST-STORE`: Шлях до trust-store (наприклад, `classpath:keystore/truststore.p12`).
- `SERVER_SSL_TRUST-STORE-PASSWORD`: Пароль доступу до trust-store.
- `SERVER_SSL_TRUST-STORE-TYPE`: Тип сховища довіри SSL (`PKCS12`, `JKS` тощо).
- `SERVER_PORT`: Порт, на якому запускається вебсервіс (типово `8080`).
- `SERVER_SSL_ENABLED`: Увімкнення SSL (`true` або `false`).
- `SERVER_SSL_CLIENT-AUTH`: Політика аутентифікації клієнта по SSL (`NONE`, `WANT`, `NEED`).

### Приклад
```env
SPRING_APPLICATION_NAME=Spring REST Webservice

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/demospring
SPRING_DATASOURCE_USERNAME=spring
SPRING_DATASOURCE_PASSWORD=springp
SPRING_DATASOURCE_DRIVER-CLASS-NAME=org.postgresql.Driver

SPRING_JPA_HIBERNATE_DDL-AUTO=update
SPRING_JPA_DATABASE=postgresql
SPRING_JPA_DATABASE-PLATFORM=org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_SHOW-SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

WEBSERVICE_SETTINGS_LOGLEVEL=2
WEBSERVICE_SETTINGS_LOGFILENAME=/tmp/springWSrest.log

SERVER_SSL_KEY-STORE-TYPE=PKCS12
SERVER_SSL_KEY-STORE=classpath:keystore/keystore.p12
SERVER_SSL_KEY-STORE-PASSWORD=localhost
SERVER_SSL_TRUST-STORE=classpath:keystore/truststore.p12
SERVER_SSL_TRUST-STORE-PASSWORD=localhost
SERVER_SSL_TRUST-STORE-TYPE=PKCS12
SERVER_PORT=8080
SERVER_SSL_ENABLED=false
SERVER_SSL_CLIENT-AUTH=NONE
```



## Збір Docker-образу

Для того, щоб зібрати Docker-образ, необхідно:
1. Клонувати репозиторій:
```bash
git clone https://github.com/Wishmaster-sa/SpringWSrest.git
```
2. Перейти до директорії з вебсервісом:
```bash
cd SpringWSrest
```
3. Виконати наступну команду в кореневій директорії проєкту:
```bash
sudo docker build -t SpringWSrest-app .
```

Дана команда створить Docker-образ з іменем `SpringWSrest-app`, використовуючи Dockerfile, який знаходиться в поточній директорії.

## Створення бази даних для сервісу
Для створення бази даних та її структури необхідно:
1. Встановити СУБД PostgreSQL згідно настанов пунктів 3-5 [настанов з ручного встановлення вебсервісу](manual_installation.md#3-встановити-субд-postgresql).

2. Створити базу даних та користувача настанов пункту 6 [настанов з ручного встановлення вебсервісу](manual_installation.md#6-створити-базу-даних-та-користувача-для-цього-необхідно).

3. Змінити конфігураційні файли PostgreSQL таким чином щоб мати змогу підключатись до БД з будь-якої IP адреси.

## Запуск та використання контейнера зі змінними оточення

Щоб запустити контейнер з вебсервісом, необхідно виконати наступну команду:

```bash
sudo docker run -it --rm -p 8080:8080 \
  -e SPRING_APPLICATION_NAME="Spring REST Webservice" \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/demospring" \
  -e SPRING_DATASOURCE_USERNAME="spring" \
  -e SPRING_DATASOURCE_PASSWORD="springp" \
  -e SPRING_DATASOURCE_DRIVER-CLASS-NAME="org.postgresql.Driver" \
  -e SPRING_JPA_HIBERNATE_DDL-AUTO="update" \
  -e SPRING_JPA_DATABASE="postgresql" \
  -e SPRING_JPA_DATABASE-PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
  -e SPRING_JPA_SHOW-SQL="true" \
  -e SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL="true" \
  -e WEBSERVICE_SETTINGS_LOGLEVEL="2" \
  -e WEBSERVICE_SETTINGS_LOGFILENAME="" \
  -e SERVER_SSL_KEY-STORE-TYPE="PKCS12" \
  -e SERVER_SSL_KEY-STORE="classpath:keystore/keystore.p12" \
  -e SERVER_SSL_KEY-STORE-PASSWORD="localhost" \
  -e SERVER_SSL_TRUST-STORE="classpath:keystore/truststore.p12" \
  -e SERVER_SSL_TRUST-STORE-PASSWORD="localhost" \
  -e SERVER_SSL_TRUST-STORE-TYPE="PKCS12" \
  -e SERVER_PORT="8080" \
  -e SERVER_SSL_ENABLED="false" \
  -e SERVER_SSL_CLIENT-AUTH="NONE" \
  SpringWSrest-app
```
де:
- параметр `-p 8080:8080` перенаправляє порт 8080 на локальній машині на порт 8080 всередині контейнера.

**Примітка:** У випадку розташування бази даних на одному хості з вебсервісом в значенні параметра `SPRING_DATASOURCE_URL` необхідно вказувати IP-адресу хоста, значення `localhost` або `127.0.0.1` працювати не будуть. Окрім цього необхідно налаштувати зовнішні підключення до бази даних


## Перегляд журналів подій

Якщо виведення журналів подій налаштоване у консоль, переглядати їх можна за допомогою команди:

```bash
docker logs <container_id>
```

##
Матеріали створено за підтримки проєкту міжнародної технічної допомоги «Підтримка ЄС цифрової трансформації України (DT4UA)».

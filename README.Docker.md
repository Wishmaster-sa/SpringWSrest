### Building and running your application

When you're ready, start your application by running:
`docker compose up --build`.

Your application will be available at http://localhost:8080.

### Deploying your application to the cloud

First, build your image, e.g.: `docker build -t myapp .`.
If your cloud uses a different CPU architecture than your development
machine (e.g., you are on a Mac M1 and your cloud provider is amd64),
you'll want to build the image for that platform, e.g.:
`docker build --platform=linux/amd64 -t myapp .`.

Then, push it to your registry, e.g. `docker push myregistry.com/myapp`.

Consult Docker's [getting started](https://docs.docker.com/go/get-started-sharing/)
docs for more detail on building and pushing.

```env
SERVER_PORT=8080;SERVER_SSL_CLIENT-AUTH=NONE;SERVER_SSL_ENABLED=false;SERVER_SSL_KEY-STORE=classpath:keystore/keystore.p12;SERVER_SSL_KEY-STORE-PASSWORD=localhost;SERVER_SSL_KEY-STORE-TYPE=PKCS12;SERVER_SSL_TRUST-STORE=classpath:keystore/truststore.p12;SERVER_SSL_TRUST-STORE-PASSWORD=localhost;SERVER_SSL_TRUST-STORE-TYPE=PKCS12;SPRING_APPLICATION_NAME=Spring REST Webservice;SPRING_DATASOURCE_DRIVER-CLASS-NAME=org.postgresql.Driver;SPRING_DATASOURCE_PASSWORD=your_db_password;SPRING_DATASOURCE_URL=jdbc:postgresql://172.16.172.129:5432/your_db_name;SPRING_DATASOURCE_USERNAME=your_db_user;SPRING_JPA_DATABASE=postgresql;SPRING_JPA_DATABASE-PLATFORM=org.hibernate.dialect.PostgreSQLDialect;SPRING_JPA_HIBERNATE_DDL-AUTO=update;SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true;SPRING_JPA_SHOW-SQL=true;WEBSERVICE_SETTINGS_LOGFILENAME=/tmp/springWSrest.log;WEBSERVICE_SETTINGS_LOGLEVEL=2
```
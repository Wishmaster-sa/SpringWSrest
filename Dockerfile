# syntax=docker/dockerfile:1

# Comments are provided throughout this file to help you get started.
# If you need more help, visit the Dockerfile reference guide at
# https://docs.docker.com/go/dockerfile-reference/

# Want to help us make this template better? Share your feedback here: https://forms.gle/ybq9Krt8jtBL3iCk7

################################################################################

# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:21-jdk-jammy as deps

WORKDIR /build

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven

# Copy the mvnw wrapper with executable permissions.
#COPY --chmod=0755 mvnw mvnw
COPY --chmod=0755 . .

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.m2 so that subsequent builds don't have to
# re-download packages.
# Кэшируем зависимости и собираем проект
# Кэшируем зависимости и собираем проект
#RUN mvn dependency:go-offline -DskipTests
#RUN mvn package -DskipTests


RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -DskipTests

################################################################################

# Create a stage for building the application based on the stage with downloaded dependencies.
# This Dockerfile is optimized for Java applications that output an uber jar, which includes
# all the dependencies needed to run your app inside a JVM. If your app doesn't output an uber
# jar and instead relies on an application server like Apache Tomcat, you'll need to update this
# stage with the correct filename of your package and update the base image of the "final" stage
# use the relevant app server, e.g., using tomcat (https://hub.docker.com/_/tomcat/) as a base image.
FROM deps as package

WORKDIR /build

#ARG SERVER_PORT=8080
#ENV SERVER_PORT=$SERVER_PORT
#ARG SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
#ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=$SPRING_DATASOURCE_DRIVER_CLASS_NAME

#ARG SPRING_DATASOURCE_URL=jdbc:postgresql://10.211.55.5:5432/demospring
#ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL

#ARG SPRING_JPA_DATABASE=postgresql
#ENV SPRING_JPA_DATABASE=$SPRING_JPA_DATABASE

#ARG SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
#ENV SPRING_JPA_DATABASE_PLATFORM=$SPRING_JPA_DATABASE_PLATFORM

#ARG SPRING_JPA_HIBERNATE_DDL_AUTO=update
#ENV SPRING_JPA_HIBERNATE_DDL_AUTO=$SPRING_JPA_HIBERNATE_DDL_AUTO

#ARG SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
#ENV SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=$SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL

#ARG SPRING_JPA_SHOW_SQL=true
#ENV SPRING_JPA_SHOW_SQL=$SPRING_JPA_SHOW_SQL

#ARG SPRING_DATASOURCE_USERNAME=spring
#ENV SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME

#ARG SPRING_DATASOURCE_PASSWORD=springp
#ENV SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD

#ARG WEBSERVICE_SETTINGS_LOGFILENAME=/tmp/springWSrest.log
#ENV WEBSERVICE_SETTINGS_LOGFILENAME=$WEBSERVICE_SETTINGS_LOGFILENAME

#ARG WEBSERVICE_SETTINGS_LOGLEVEL=2
#ENV WEBSERVICE_SETTINGS_LOGLEVEL=$WEBSERVICE_SETTINGS_LOGLEVEL

COPY ./src src/
#COPY . .
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests && \
    mv target/$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Create a stage for extracting the application into separate layers.
# Take advantage of Spring Boot's layer tools and Docker's caching by extracting
# the packaged application into separate layers that can be copied into the final stage.
# See Spring's docs for reference:
# https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Create a new stage for running the application that contains the minimal
# runtime dependencies for the application. This often uses a different base
# image from the install or build stage where the necessary files are copied
# from the install stage.
#
# The example below uses eclipse-turmin's JRE image as the foundation for running the app.
# By specifying the "21-jre-jammy" tag, it will also use whatever happens to be the
# most recent version of that tag when you build your Dockerfile.
# If reproducibility is important, consider using a specific digest SHA, like
# eclipse-temurin@sha256:99cede493dfd88720b610eb8077c8688d3cca50003d76d1d539b0efc8cca72b4.
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user that the app will run under.
# See https://docs.docker.com/go/dockerfile-user-best-practices/
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser
WORKDIR /app

# Copy the executable from the "package" stage.
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]
#CMD sh -c "find / -name SpringWS-0.0.1-SNAPSHOT.jar"

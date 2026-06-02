###
# Image pour la compilation
FROM maven:3-eclipse-temurin-17 AS build-image
WORKDIR /build/


# On lance la compilation Java
# On débute par une mise en cache docker des dépendances Java
# cf https://www.baeldung.com/ops/docker-cache-maven-dependencies
COPY ./pom.xml /build/pom.xml
RUN mvn verify --fail-never

# Et la compilation du code Java
COPY ./src/   /build/src/
RUN mvn --batch-mode -e \
    -Dmaven.test.skip=false \
    -Duser.timezone=Europe/Paris \
    -Duser.language=fr \
    package

###
# Image pour le module API
FROM eclipse-temurin:17-jre AS api-recherche-image
WORKDIR /app/

# Copie de l'application compilée
COPY --from=build-image /build/target/*.jar /app/theses-api-recherche.jar

# Téléchargement d'une version fixe de l'agent OpenTelemetry pour la reproductibilité
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.3.0/opentelemetry-javaagent.jar /app/opentelemetry.jar

ENTRYPOINT ["java", "-javaagent:/app/opentelemetry.jar", "-jar", "/app/theses-api-recherche.jar"]

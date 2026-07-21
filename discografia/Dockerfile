FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootWar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/discografia-1.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
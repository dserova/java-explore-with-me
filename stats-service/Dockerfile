FROM maven:slim as builder
WORKDIR /usr/src/app
COPY . .
RUN mvn clean install

FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
RUN apk add --no-cache curl dumb-init  \
    && addgroup --system javauser  \
    && adduser -S -s /bin/false -G javauser javauser  \
    && chown -R javauser:javauser /app
COPY --from=builder /usr/src/app/server/target/explore-with-me-stats.jar ./explore-with-me-stats.jar
USER javauser
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["dumb-init", "java", "-jar", "./explore-with-me-stats.jar"]
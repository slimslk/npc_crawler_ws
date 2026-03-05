# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /build/target/*jar-with-dependencies.jar app.jar

ENV JAVA_OPTS="\
-XX:+UseSerialGC \
-XX:MaxRAMPercentage=65 \
-XX:+UseContainerSupport \
-XX:MaxMetaspaceSize=128m \
-Dio.netty.allocator.numDirectArenas=0 \
-Dio.netty.allocator.maxOrder=7 \
-Dio.netty.noPreferDirect=true"

CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]

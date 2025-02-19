FROM openjdk:21
ADD /target/Inventory.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]
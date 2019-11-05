FROM openjdk:8
EXPOSE 5000
ADD target/greeting.jar greeting.jar
ENTRYPOINT ["java","-jar","/greeting.jar"]

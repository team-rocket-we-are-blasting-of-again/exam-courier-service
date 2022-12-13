FROM tobiaszimmer/exam-gateway-subscription:java-17
COPY target/*SNAPSHOT.jar /application.jar
COPY target/*.jar /application-stubs.jar
COPY gateway-routes.json /gateway-routes.json
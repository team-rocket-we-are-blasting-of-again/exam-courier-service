#FROM tobiaszimmer/exam-gateway-subscription:java-17
#COPY target/*.jar/ /application.jar
#COPY gateway-routes.json /gateway-routes.json

FROM tobiaszimmer/exam-gateway-subscription:java-17
ARG jar_file
COPY $jar_file /application.jar
COPY gateway-routes.json /gateway-routes.json
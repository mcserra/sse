1. Launch activeMq
    - docker pull rmohr/activemq
    - docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
2. Package and deploy
    - mvn clean package && java -jar payara-micro.jar --autobindhttp --deploy activemq-rar-5.12.0.rar --deploy target/sse.war --port 2765

TODO: Add Dockerfile and remove jars

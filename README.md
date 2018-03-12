# Timetracker Frontend Application


TimeTracker is a legacy application that keeps track of employee work times. The application is deployed as docker container, whose docker image is available at the following URL along with some samples calls:

https://hub.docker.com/r/alirizasaral/timetracker/


Build the application

1.) goto to the root directory of project

2.) run

$ mvn clean compile package


Build docker image

3.) run

$ docker network create tracker-net
$ docker build --name timetrackerfe --network tracker-net cyrof/timetracker .
docker build -t cyrof/timetracker  .

Run application in docker container

$ docker run -d -p 8081:8081 --name timetrackerfe  --network tracker-net -e JAVA_OPTS="-Dintegration.timetracker.base-path=http://timetracker:8080" cyrof/timetracker

We can setup address where is runnig timetracker backend through integration.timetracker.base-path


Prerequisite to fully functionaly frontend application Timetracker FE is run backend of timetracker.
1.) Pull  timetracker from 

https://hub.docker.com/r/alirizasaral/timetracker/

$ docker pull alirizasaral/timetracker

Run timetracker backend with command:

$ docker run -d -p 8080:8080 --name timetracker --network tracker-net alirizasaral/timetracker:1



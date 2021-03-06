# Genetic Algorithm Timetable

Create a timetable via a genetic algorithm machine learning method.

## How to Run

This project is set up to run with Gradle, and the wrapper included. The Spring boot framework runs the app:

`./gradlew bootRun`

API will be running at `localhost:5000/*`

### Prerequisites 

* Java 8
* Gradle
* PostgreSQl 9.6 database

## How to build

Gradle will build the Java project before it runs `bootRun`

The frontend must be built by Webpack when modified. It may be invoked with `npx webpack`
or can build files on change with `npm run-script watch`

## How to Deploy

The project is set up to be deployed as a JAR on an AWS Elastic Beanstalk container
running barebones Java 8 environment (not an EB Tomcat config)

`./gradlew bootJar`  
_JAR can be found as build/libs/ga-rest-api-VERSION.jar_

Deploy jar to AWS

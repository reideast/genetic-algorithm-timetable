# This work is based on the Gradle Docker file, see: https://github.com/keeganwitt/docker-gradle/blob/a206b4a26547df6d8b29d06dd706358e3801d4a9/jdk8/Dockerfile
# and also see: https://hub.docker.com/r/jhoglin/gradle-node/dockerfile

#FROM adoptopenjdk:8-jdk-hotspot
FROM openjdk:8-alpine
# OpenJDK docker, see: https://hub.docker.com/_/openjdk

RUN apk add --no-cache \
  --repository http://dl-cdn.alpinelinux.org/alpine/v3.7/main/ nodejs=8.9.3-r1

# Hack permissions so that root access to the image during build isn't required (would be prevented by a proper Node docker image?)
RUN mkdir /.npm \
  && chmod +rwx /.npm

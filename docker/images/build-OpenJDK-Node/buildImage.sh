#!/bin/bash -x

docker build --tag=build-openjdk-node .

docker tag build-openjdk-node andreweast2/build-openjdk-node:latest

echo "Verifying..."
docker run build-openjdk-node:latest node --version
docker run build-openjdk-node:latest npm --version


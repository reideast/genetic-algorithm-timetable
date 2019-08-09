#!/bin/bash -x

docker build --tag=build-openjdk-node-compiled .

docker tag build-openjdk-node-compiled andreweast2/build-openjdk-node-compiled:latest

echo "Verifying..."
docker run build-openjdk-node-compiled:latest node --version
docker run build-openjdk-node-compiled:latest npm --version

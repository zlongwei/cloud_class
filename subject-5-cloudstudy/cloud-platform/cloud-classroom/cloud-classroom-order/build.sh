#!/usr/bin/env bash
mvn clean install -DskipTests

# 推送到harbor私库
docker build -t 148.70.96.49/study/cloud-classroom-order:1.0 .
docker push 148.70.96.49/study/cloud-classroom-order:1.0

# 或者推送到hub.docker上
#docker build -t xiaochunping/cloud-classroom-course:1.0 .
#docker push xiaochunping/cloud-classroom-course:1.0
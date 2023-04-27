#!/bin/sh

echo "[$(date)] [Startscript] Copying config & starting Jar"

cp /etc/configs/config.yaml `pwd`/config.yaml

java -jar /run/api.jar
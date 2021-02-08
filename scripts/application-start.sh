#!/bin/bash

cd /home/ec2-user/momelet/backend/spring
docer-compose pull
docker-compose up -d
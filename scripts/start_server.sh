#!/bin/bash
echo "sdfafasfsfsaf" > a.log
sudo su
java -jar  /home/ec2-user/demo/demoforcicd-1.0-SNAPSHOT.jar & > test.log

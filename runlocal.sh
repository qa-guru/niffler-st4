#!/bin/bash
./localenv.sh
./runfrontend.sh &
./gradlew bootRun --args='--spring.profiles.active=local' --parallel

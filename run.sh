#!/bin/bash

SUBDIR="author-service"
MAIN_DIR=$(pwd)

SUBPROJECT_PID=0
MAINPROJECT_PID=0

cleanup() {
    if [[ $SUBPROJECT_PID -ne 0 ]]; then
        kill $SUBPROJECT_PID
    fi
    if [[ $MAINPROJECT_PID -ne 0 ]]; then
        kill $MAINPROJECT_PID
    fi
    exit 0
}

trap cleanup SIGINT SIGTERM

cd "$SUBDIR"
mvn spring-boot:run &
SUBPROJECT_PID=$!

cd "$MAIN_DIR"
mvn spring-boot:run &
MAINPROJECT_PID=$!
wait $MAINPROJECT_PID
cleanup

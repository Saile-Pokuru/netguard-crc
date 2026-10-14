#!/bin/bash
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/model/*.java src/gui/*.java src/app/*.java
java -cp out app.Main

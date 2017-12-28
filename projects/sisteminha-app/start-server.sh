#!/bin/sh
gnome-terminal -x sh -c "sisteminha-backend/./gradlew -p sisteminha-backend build && java -jar sisteminha-backend/build/libs/sisteminha-0.0.1.jar; bash"
gnome-terminal -x sh -c "npm --prefix ./sisteminha-frontend install ./sisteminha-frontend && npm --prefix ./sisteminha-frontend start ./sisteminha-frontend; bash"

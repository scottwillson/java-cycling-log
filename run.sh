#!/bin/sh

cp -r src/java/com/butlerpress/cyclinglog/*.xml build/classes/com/butlerpress/cyclinglog/
CP=build/classes/:
CP=`echo lib/*.jar | tr ' ' :`:${CP}
CP=`echo build/classes/com/butlerpress/cyclinglog/*.xml | tr ' ' :`:${CP}
CP=`echo etc/*.xml | tr ' ' :`:${CP}

java -cp $CP com.butlerpress.cyclinglog.App "$@";

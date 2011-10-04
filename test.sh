#!/bin/sh

CP=build/classes/:
CP=`echo lib/*.jar | tr ' ' :`:${CP}
CP=`echo build/classes/com/butlerpress/cyclinglog/*.xml | tr ' ' :`:${CP}
CP=`echo build/classes/*.xml | tr ' ' :`:${CP}
CP=`echo build/classes/*.properties | tr ' ' :`:${CP}

java -cp $CP junit.textui.TestRunner com.butlerpress.cyclinglog.$@;

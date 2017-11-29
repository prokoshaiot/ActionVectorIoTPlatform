#!/bin/bash

start() {
	echo "Starting V1.5 adapter.....";
	HOME_DIR=`pwd`
	LIB_DIR=$HOME_DIR/lib;
	REC_DIR=$HOME_DIR/resources;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-beanutils-1.8.3.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-collections-3.2.1.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-io-2.0.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-lang-2.5.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-io-2.0.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-logging-1.1.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/log4j-1.2.9.jar;
	CLASSPATH=$CLASSPATH:/$LIB_DIR/commons-configuration-1.6.jar;
	CLASSPATH=$CLASSPATH:/$REC_DIR;
	CLASSPATH=$CLASSPATH:/$REC_DIR/configuration.properties;
	CLASSPATH=$CLASSPATH:/$REC_DIR/log4j.properties;
	CLASSPATH=$CLASSPATH:/$HOME_DIR/../source/JVMDataCollector/dist/JVMDataCollector.jar;
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Common_Applications_and_Service_Frameworks/IOT_Gateway/Gateway_Event_Dispatcher_Framework/TCP-Dispatcher/TCPEventDispatcher/dist/TCPEventDispatcher.jar
	
	export CLASSPATH;
	uniqueSearchKey="--JVMadapter-adapter."`basename $PWD`;
	#connect to same JVM for testing
	nohup java -Xmx1024m -Djava.awt.headless=true -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=5558  -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false   -Djavax.net.ssl.trustStore=sadeskKey -Djavax.net.ssl.trustStorePassword=123456 com.meritsystems.monitor.JVMMonitor $uniqueSearchKey & 
	#connect to external jvm
	#java com.meritsystems.monitor.Monitor $uniqueSearchKey &
	return 0;
}

stop() {
	echo "Stopping V1.5 adapter.....";
	uniqueSearchKey="\-\-JVMadapter\-adapter."`basename $PWD`;
	echo $uniqueSearchKey;
	PROCID=`ps xa | grep $uniqueSearchKey  | grep -v grep | sed  "s/^ *//" | cut --fields=1 --delim=" "`;
	echo "V1.5 Adapter processID = " $PROCID;
	kill -9 $PROCID;
	return 0;
}

if [ "$1" = "--start" ]; then
	start
elif [ "$1" = "--kill" ]; then
	stop
fi

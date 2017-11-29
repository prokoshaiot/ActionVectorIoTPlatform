#!/bin/bash

start() {
	echo "Starting Ganglia adapter.....";
	JAVA_HOME=/usr/bin/java;
	HOME_DIR=`pwd`;
	CLASSPATH=$JAVA_HOME/lib/tools.jar:.;
	for i in `find $HOME_DIR/lib/ -name '*.jar' |grep .jar`; do CLASSPATH=$CLASSPATH:$i; done
	CLASSPATH=$CLASSPATH:$HOME_DIR/../source/GangliaDataCollector/dist/GangliaDataCollector.jar;
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Domain_Applications_and_Services_Framework–ITInfraDomain/IOT_Gateway/Ganglia_Event_Schema_Mapper_Framework/GangliaEventSchemaMapper/dist/GangliaEventSchemaMapper.jar
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Domain_Applications_and_Services_Framework–ITInfraDomain/IOT_Gateway/Ganglia_Protocol_Handler_Framework/GangliaProtocolHandler/dist/GangliaProtocolHandler.jar
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Common_Applications_and_Service_Frameworks/IOT_Gateway/Gateway_Event_Dispatcher_Framework/HTTP-Dispatcher/HTTPEventDispatcher/dist/HTTPEventDispatcher.jar
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Common_Applications_and_Service_Frameworks/IOT_Gateway/Gateway_Event_Dispatcher_Framework/TCP-Dispatcher/TCPEventDispatcher/dist/TCPEventDispatcher.jar
	echo $CLASSPATH;
	export JAVA_HOME CLASSPATH;
	uniqueSearchKey="--Gangliaadapter-adapter."`basename $PWD`;
	nohup java -Djava.awt.headless=true  -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=6666  -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  -Djavax.net.ssl.trustStore=sadeskKey -Djavax.net.ssl.trustStorePassword=123456 com.prokosha.adapter.ganglia.GangliaAdapter 192.168.1.7 8659 $uniqueSearchKey &	return 0;
}

stop() {
	echo "Stopping Ganglia adapter.....";
	uniqueSearchKey="\-\-Gangliaadapter\-adapter."`basename $PWD`;
	echo $uniqueSearchKey;
	PROCID=`ps xa | grep $uniqueSearchKey  | grep -v grep | sed  "s/^ *//" | cut --fields=1 --delim=" "`;
	echo "Ganglia Adapter processID = " $PROCID;
	kill -9 $PROCID;
	return 0;
}

if [ "$1" = "--start" ]; then
	start
elif [ "$1" = "--kill" ]; then
	stop
fi

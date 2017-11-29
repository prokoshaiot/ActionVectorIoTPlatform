#!/bin/bash

start() {
	echo "Starting CEPEngine.....";
	JAVA_HOME=/usr/bin/java
	HOME_DIR=`pwd`
	CLASSPATH=$JAVA_HOME/lib/tools.jar:.
	for i in `find $HOME_DIR/lib/ -name '*.jar' |grep .jar`; do CLASSPATH=$CLASSPATH:$i; done
	CLASSPATH=$CLASSPATH:$HOME_DIR/sadeskCEP2.0.jar
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Common_Applications_and_Service_Frameworks/IOT_Server/License_Framework/LicenseKeyGenerator/dist/LicenseKeyGenerator.jar

	echo $CLASSPATH;
	export JAVA_HOME CLASSPATH;

	uniqueSearchKey="--engine-engine."`basename $PWD`;
	nohup java  com.prokosha.licensekeygenerator.LicenseKeyGenerator	
	nohup java -Duser.timezone="IST" -Djava.awt.headless=true -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8082 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false com.prokosha.sadeskCeP.sadeskCepEngine Ganglia $uniqueSearchKey &
	return 0;
}

stop() {
	echo "Stopping CEPEngine.....";
	uniqueSearchKey="\-\-engine\-engine."`basename $PWD`;
	echo $uniqueSearchKey;
	PROCID=`ps xa | grep $uniqueSearchKey  | grep -v grep | sed  "s/^ *//" | cut --fields=1 --delim=" "`;
	echo "CEPEngine processID = " $PROCID;
	kill -9 $PROCID;
	return 0;
}

if [ "$1" = "--start" ]; then
	start
elif [ "$1" = "--kill" ]; then
	stop
fi

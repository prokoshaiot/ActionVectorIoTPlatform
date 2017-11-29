#!/bin/bash

start() {
	CATALINA_HOME=/home/rekhas/AVSA/apache-tomcat-7.0.34;

	echo "starting DashBoard Parser.....";
	CATALINA_BASE=$CATALINA_HOME;
	HOME_DIR=`pwd`;
	CLASSPATH=$JAVA_HOME/lib/tools.jar:.;
	for i in `find $HOME_DIR/lib/ -name '*.jar' |grep .jar`; do CLASSPATH=$CLASSPATH:$i; done
	CLASSPATH=$CLASSPATH:$HOME_DIR/../source/DashBoard-JSONgenerator/dist/DashBoard-JSONgenerator.jar;
	echo $CLASSPATH;
	export JAVA_HOME CLASSPATH CATALINA_HOME CATALINA_BASE;
	uniqueSearchKey="--ITjsonParser-jsonParser."`basename $PWD`;
	nohup java -Xmx768m -Djava.awt.headless=true -server -Dcatalina.base="$CATALINA_BASE" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=6664  -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  main.ServiceThreadListener --domainName=merit.actionvector.com $uniqueSearchKey &
#	nohup java -Djava.awt.headless=true -server -Dcatalina.base="$CATALINA_BASE" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=6665  -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  main.ServiceThreadListener --domainName=calligo.actionvector.com $uniqueSearchKey &        
	return 0;
}

stop() {
	echo "stoping DashBoard Parser.....";
	uniqueSearchKey="\-\-ITjsonParser\-jsonParser."`basename $PWD`;
	echo $uniqueSearchKey;
	PROCID=`ps xa | grep $uniqueSearchKey  | grep -v grep | sed  "s/^ *//" | cut --fields=1 --delim=" "`;
	echo "Adapter processID = " $PROCID;
	kill -9 $PROCID;
	return 0;
}

if [ "$1" = "--start" ]; then
	stop
	start
elif [ "$1" = "--kill" ]; then
	stop
elif [ "$1" = "--restart" ]; then
	stop
	start
fi

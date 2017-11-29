#!/bin/bash

start() {
        echo "Starting TCP Event Receiver .....";
	HOME_DIR=`pwd`
	for i in `find $HOME_DIR/lib/ -name '*.jar' |grep .jar`; do CLASSPATH=$CLASSPATH:$i; done
	CLASSPATH=$CLASSPATH:$HOME_DIR/../source/TCPEventReceiver/dist/TCPEventReceiver.jar
	CLASSPATH=$CLASSPATH:/home/rekhas/AVSA/Source/IoT_Common_Applications_and_Service_Frameworks/IOT_Server/Event_Logger_Framework/source/ETLAdapter/dist/ETLAdapter.jar
	echo $CLASSPATH
	export CLASSPATH

	uniqueSearchKey="--IT_tcp_receiver-tcpr."`basename $PWD`;

	nohup java -Xmx1024m -Djava.awt.headless=true -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8088 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  -Djavax.net.ssl.keyStore="$HOME_DIR"/sadeskKey -Djavax.net.ssl.keyStorePassword=123456 com.prokosha.tcp.eventlistener.TCPEventReceiver $uniqueSearchKey &

        return 0;
}

stop() {
        echo "Stopping TCP Event Receiver .....";
        uniqueSearchKey="\-\-IT_tcp_receiver\-tcpr."`basename $PWD`;
        echo $uniqueSearchKey;
        PROCID=`ps xa | grep $uniqueSearchKey  | grep -v grep | sed  "s/^ *//" | cut --fields=1 --delim=" "`;
        echo "TCPEventReceiver processID = " $PROCID;
        kill -9 $PROCID;
        return 0;
}

if [ "$1" = "--start" ]; then
        start
elif [ "$1" = "--kill" ]; then
        stop
fi
  

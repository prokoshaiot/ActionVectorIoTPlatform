APP_DIR=/home/vijayasri/reports
CLASSPATH=$JAVA_HOME/lib
CLASSPATH=$CLASSPATH:$APP_DIR/lib/*

echo $CLASSPATH
export CLASSPATH

cd $APP_DIR/Adapters/reportAdapters
nohup java reportadapter.ReadOpsGanglia &

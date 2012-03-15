#!/bin/bash

###############################################################
#	Starter script for AutoSS
#	Written by : Pulasthi Mahawithana <pulasthi7@gmail.com>
#
#	Usage : <script name> arg1 arg2 arg3 arg4
#			arg1 - The Home directory of the AutoSS
#			arg2 - The Media File(Converted to .wav)
#			arg3 - The Subtitle File
#			arg4 - The Stack size in MB (512 recommended)
#
###############################################################

AUTOSS_HOME=$1
MEDIA=$2
SUBFILE=$3
STACK_SIZE=$4
FREQ_FILE=$AUTOSS_HOME/freq.txt
PATH=$PATH:$AUTOSS_HOME/tools:$AUTOSS_HOME/tools/CMU_Cam_Toolkit/bin
#CLASSPATH=.:$PWD/lib/jmf.jar:$PWD/lib/sphinx4.jar:$PWD/lib/jsapi.jar
cd $AUTOSS_HOME
java -jar $AUTOSS_HOME/tools/AutoSS-start.jar $SUBFILE
$AUTOSS_HOME/tools/mklm $FREQ_FILE
cp $FREQ_FILE.arpa ./config/captiondata.arpa
rm $FREQ_FILE*
java -mx$STACK_SIZE\m -jar $AUTOSS_HOME/jar/AutoSS.jar $MEDIA $SUBFILE

#!/bin/sh --

JAVA=%JAVA%
DEBUG=%DEBUG%
VERSION=%VERSION%
JARPATH=%JARPATH%
JAVAUNIXLIBPATH=%JAVAUNIXLIBPATH%
JAVAUNIXJARPATH=%JAVAUNIXJARPATH%

exec $JAVA -DPid=$$ -DVersion=$VERSION -Djava.library.path=$JAVAUNIXLIBPATH -cp $JAVAUNIXJARPATH/unix.jar:$JAVAUNIXJARPATH/debug-$DEBUG.jar:$JAVAUNIXJARPATH/hexdump.jar:$JARPATH/dbus.jar:$JARPATH/dbus-bin.jar org.freedesktop.dbus.bin.Caller "$@"
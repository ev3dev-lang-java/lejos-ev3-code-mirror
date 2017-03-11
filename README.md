# lejos-ev3-code-mirror

This project is a Git mirror repo to store sources from the following repo:

``
git clone https://git.code.sf.net/p/lejos/ev3/code lejos-ev3-code
```

The project has multiple projects inside:

```
lejos-ev3-code git:(master) ls
BrickPiRXTX                   EV3PairTest                   ev3installer                  images
BrickPiTest                   EV3RCXTest                    ev3menutools                  jna-3.2.7
DBusJava                      EV3RemoteEV3                  ev3pcsamples                  org.lejos.ev3.example
EV3GPS                        EV3RemoteNXT                  ev3release                    org.lejos.ev3.ldt
EV3MCLTest                    EV3SensorMonitor              ev3samples                    org.lejos.ev3.ldt.feature
EV3MapTest                    android                       ev3scripts                    org.lejos.ev3.ldt.update-site
EV3Menu                       brickpiclasses                ev3snapshot                   org.lejos.ev3.pcexample
EV3NewMenu                    ev3classes                    ev3tools
```

but we only store the projects related with the Java library:

- DBusJava
- ev3classes

# Local compilation 

In order to compile, it is necessary to compile first DBusJava and later compile ev3classes. Both projects use Java Ant.



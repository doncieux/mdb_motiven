# Motiven in MDB
Java implementation of the ongoing research in motivation to be integrated in cafer 

## Libs installation
For using this project, it is needed to install two libraries in the maven repository. This libraries are in folder lib, and for its instalation it must be executed:
```sh
$ mvn install:install-file -Dfile=lib/artificial_neural_networks-1.01.jar -DgroupId=es.udc.gii.common -DartifactId=evolutionary_algorithms -Dversion=1.0 -Dpackaging=jar
$ mvn install:install-file -Dfile=lib/evolutionary_algorithms-1.0.jar -DgroupId=es.udc.gii.common -DartifactId=artificial_neural_networks -Dversion=1.01 -Dpackaging=jar
$ mvn install:install-file -Dfile=lib/anji-1.0.jar -DgroupId=com -DartifactId=anji -Dversion=1.0 -Dpackaging=jar
```

## Description
This project is a very light version of the old MDB with the ongoing MotivEn research, based on correlations.


## USAGE

Important:
Due to an error in anji-neat library, it is mandatory create a folder to use neat, what is set up in the neat properties file (it is already created in this program, see config folder). 

The MDB is though as an independent cognitive architecture to be run with any robot or simulator. For this, to run the MDB, it is needed to run the simulator first, so the MDB will connect to it by TCP/IP. After running the rimulator or robot, the MDB can be executed:

```sh
$ java -jar MDBCore-full config/MDB-Config.xml
```

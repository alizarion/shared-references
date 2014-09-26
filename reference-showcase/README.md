References Showcase
====================
Author : Selim Bensenouci.

what is this?
------------
  
this example demonstrates use of references libraries, the application is  
a simple social network like, that allow users registration commenting content and profile.  

Requirement
-----------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.0 or better.  
The application this project produces is designed to be run on JBoss WildFly.  
they will use for persistence wildFly H2 in-memory database.  

Deploying the application
--------------------------

1. Start your WildFly server.  
>Do not forget to declare your environment variable `JBOSS_HOME, M2_HOME, JAVA_HOME`.  
`$JBOSS_HOME/bin/standalone.sh`  
2. package the war application using maven.  

`mvn clean:package`  

3. deploy the war using the maven wildfly plugin.  

`mvn wildfly:deploy`  

4. access to the webapp using.   

[http://localhost:8080/reference-showcase](http://localhost:8080/reference-showcase)







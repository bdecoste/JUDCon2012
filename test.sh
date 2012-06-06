#!/bin/sh

java -cp order-consumer/target/test-classes:rules-camel-cbr/target/classes:/home/bdecoste/.m2/repository/javax/jms/jms/1.1/jms-1.1.jar:/home/bdecoste/.m2/repository/org/switchyard/switchyard-test/0.4.0.Final/switchyard-test-0.4.0.Final.jar:/home/bdecoste/.m2/repository/org/hornetq/hornetq-core/2.2.13.Final/hornetq-core-2.2.13.Final.jar:/home/bdecoste/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar:/home/bdecoste/.m2/repository/org/hornetq/hornetq-jms-client/2.2.11.Final/hornetq-jms-client-2.2.11.Final.jar:/home/bdecoste/.m2/repository/org/jboss/netty/netty/3.2.1.Final/netty-3.2.1.Final.jar:/home/bdecoste/.m2/repository/org/milyn/milyn-smooks-all/1.5/milyn-smooks-all-1.5.jar:/home/bdecoste/.m2/repository/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:/home/bdecoste/.m2/repository/commons-logging/commons-logging-api/1.0.4/commons-logging-api-1.0.4.jar:/home/bdecoste/.m2/repository/commons-codec/commons-codec/1.5/commons-codec-1.5.jar:/home/bdecoste/.m2/repository/junit/junit/4.10/junit-4.10.jar  org.switchyard.quickstarts.demo.multiapp.consumer.OrderIntakeClient $1

#:/home/bdecoste/OpenShift/JUD/tmp/coretest/target/classes


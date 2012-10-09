# JUDCon2012 Demo

## Running the Example

1. Create an OpenShift jbossas-7 instance names 'as1': rhc app create -a as1 -t jbossas-t -d
2. Download this git repo 
3. Build the project: mvn clean install -Dmaven.test.skip=true
4. Replace the default .openshift/config/standalone.xml,.openshift/action_hooks/pre_build and pom.xml file with the ones from the jboss directory
5. Replace the default src directory with restful-order/src
6. Copy caching/target/caching-1.0.jar to the root directory of the .git repo
7. Copy artifacts/target/OrderService.jar to the deployments directory of the .git repo
8. Copy bpm-service/target/switchyard-quickstart-demo-bpm-service.jar to the deployments directory of the .git repo
9. Copy order-service/target/switchyard-quickstart-demo-multi-order-service.jar to the deployments directory of the .git repo
10. Copy rules-camel-cbr/target/switchyard-quickstart-demo-rules-camel-cbr.jar to the deployments directory of the .git repo
11. Copy web/target/switchyard-quickstart-demo-multi-web.war to the deployments directory of the .git repo
12. push changes (git add ., git commit -a -m "test", git push)


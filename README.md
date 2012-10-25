# JUDCon2012 Demo

## Running the Example

1. Create a tmp directory

2. cd into the tmp dir

3. Download this git repo

4. Replace every occurence of 127.*.*.* with the value of OPENSHIFT_INTERNAL_IP in any of the projects files (e.g. .xml, .java)

5. Build the project: mvn clean install -Dmaven.test.skip=true

6. Build the web project: cd web & mvn clean install -Dmaven.test.skip=true

7. cd ..

7. Create an OpenShift jbossas-7 instance names 'as1': rhc app create -a as1 -t jbossas-t -d

8. cd as1 

9. Replace the default .openshift/config/standalone.xml,.openshift/action_hooks/pre_build and pom.xml file with the ones from the tmp/jboss directory

10. Replace the default src directory with tmp/restful-order/src

11. Copy tmp/jboss/modules to .openshift/config

12. Copy tmp/caching/target/caching-1.0.jar to the root directory of the .git repo

13. Copy tmp/artifacts/target/OrderService.jar to the deployments directory of the .git repo

14. Copy tmp/bpm-service/target/switchyard-quickstart-demo-bpm-service.jar to the deployments directory of the .git repo

15. Copy tmp/order-service/target/switchyard-quickstart-demo-multi-order-service.jar to the deployments directory of the .git repo

16. Copy tmp/rules-camel-cbr/target/switchyard-quickstart-demo-rules-camel-cbr.jar to the deployments directory of the .git repo

17. Copy tmp/web/target/switchyard-quickstart-demo-multi-web.war to the deployments directory of the .git repo

18. push changes (git add ., git commit -a -m "test", git push)


# JUDCon2012 Demo

## Running the Example

1. Create an OpenShift jbossas-7 instance names 'as1': rhc app create -a as1 -t jbossas-t -d

2. Add the SwitchYard cartridge: rhc cartridge add -a as1 -c switchyard-0.6

3. Create a tmp directory

4. cd into the tmp dir

5. Clone this git repo

6. Replace every occurence of 127.*.*.* with the value of OPENSHIFT_INTERNAL_IP in any of the projects files (e.g. .xml, .java)

7. Replace /home/bdecoste/OpenShift/JUDCon in the pom.xml files to a locatiom where you would like to store the artifacts

8. Build the project: mvn clean install -Dmaven.test.skip=true

9. Build the web project: cd web & mvn clean install -Dmaven.test.skip=true

10. cd ../as1 

11. Replace the default .openshift/config/standalone.xml,.openshift/action_hooks/pre_build and pom.xml file with the ones from the tmp/jboss directory

12. Replace the default src directory with tmp/restful-order/src

13. Copy tmp/caching/target/caching-1.0.jar to the root directory of the .git repo

14. Copy tmp/artifacts/target/OrderService.jar to the deployments directory of the .git repo

15. Copy tmp/bpm-service/target/switchyard-quickstart-demo-bpm-service.jar to the deployments directory of the .git repo

16. Copy tmp/order-service/target/switchyard-quickstart-demo-multi-order-service.jar to the deployments directory of the .git repo

17. Copy tmp/rules-camel-cbr/target/switchyard-quickstart-demo-rules-camel-cbr.jar to the deployments directory of the .git repo

18. Copy tmp/web/target/switchyard-quickstart-demo-multi-web.war to the deployments directory of the .git repo

19. push changes (git add ., git commit -a -m "test", git push)


# DOCKER AND KUBERNETES - SOA Assignment 2 Summer Term 2020

DOCKER
-------

####### To fix white space issue execute  below command inside BeverageService ######

find . -type f -print0 | xargs -0 dos2unix

#### Compose#########

	Start all Container:
	- docker-compose up

	Stop all container gracefully an save data to json file
	- docker-compose stop --timeout 45


### To remove all instanaces ####
docker system prune -a --volumes


KUBERNETES
-----------
- Run "docker-compose" inside /BeverageService (** Only if docker images were not created earlier)
- Run "kubectl apply -f ." inside /BeverageService/k8s
- Wait for about 35 seconds to allow pods/containers to run or "kubectl get pods" to see the running status of pods.

-Swagger docs accessible through "localhost:8080/swagger-ui" for customers and "localhost:8090/swagger-ui" for management. 

 config-map.yaml contains configuration related to environment variables.

Health check "/live" (Liveness) and "/ready" (Readiness) can be configured in production environment but since	development environment is unpredictable and appropriate value for time (initialDelaySeconds, timeoutSeconds) is difficult to determine, it has been commented out in .yaml files.


Insomnia Project
-----------------
-Import Insomnia_Assignment2.json to Insomnia.
The project has been divided into Customer and Management with their respective API calls.
The "base_url" can be modified from Environment -> "base_url" from current "http://localhost:8080/v1" for customer and "http://localhost:8090/v1" for management.

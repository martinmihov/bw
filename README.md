# bw

Requirements to run the application:

Git
Docker Desktop
Java 21 
Maven

Step 1: clone the repo

Step 2: run PowerShell as administrator and navigate to the root folder of the project

Step 3: build and start all services by executing in PowerShell "docker compose up --build" command

Step 4: either import the postman collection in the resource folder or use curl in cmd: 

curl -s -X POST "http://localhost:8080/user-posts/gather"

curl -s "http://localhost:8080/user-posts?page=0&size=10"

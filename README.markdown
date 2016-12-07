#Deploy
develop machine side
``` sh
# Build docker image
sbt app/docker
# Export image ready for building.
docker save -o ~/test tw.com.mai-mai/property-management
# Deploy to aws
scp ~/test YOUR_IP:~      
```
---
server side
``` sh
# import image to docker
sudo docker load -i test
# Run the image and map its port from 8080 to host's 8081 port
docker run -d -p8081:8080 tw.com.mai-mai/property-management
```

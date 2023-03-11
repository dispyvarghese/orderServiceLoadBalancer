# orderServiceLoadBalancer
This is an example for clientside loadbalancing. here this app is acting as a client to order. Register this app to eureka.It also act as a microservice.

Add this dependency-spring-cloud-starter-loadbalancer
and in the controller class , register orderservice to RoundRobinLoadBalancer, then get the instance and route it for whichever service we need loading balancing.

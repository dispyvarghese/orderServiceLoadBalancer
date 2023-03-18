package com.orderserviceloadbalancer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class OrderServiceLoadBalancerController {

	@Autowired
	LoadBalancerClientFactory clientFactory;
	
	@Autowired
	RestTemplate restTemplate;
	
	@CircuitBreaker(name="backup",fallbackMethod = "fallBackForGetOrders")
	@GetMapping("/orders")
	public String getOrders() {
		
		RoundRobinLoadBalancer lb= clientFactory.getInstance("ORDERSERVICE", RoundRobinLoadBalancer.class);
		ServiceInstance instance = lb.choose().block().getServer();
		
		int port = instance.getPort();
		String host= instance.getHost();
		
		String url= "http://"+host+":"+port+"/orders";
		
		String response = restTemplate.getForObject(url, String.class);
		
		return response;
		
	}
	
	public String fallBackForGetOrders(CallNotPermittedException ex) {
		return "response from fallBack method";
		
	}
}

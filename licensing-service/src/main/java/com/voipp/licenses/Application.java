package com.voipp.licenses;

import com.voipp.licenses.events.events.OrganizationChangeModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Sink.class)
@EnableCircuitBreaker
public class Application {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnFactory = new
                JedisConnectionFactory();
        jedisConnFactory.setHostName("");
        jedisConnFactory.setPort(1);
        return jedisConnFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(String /*OrganizationChangeModel*/ model) {
        System.err.println("Received an event for orgranizaion id: " + model /*model.getOrgId()*/);
    }
}

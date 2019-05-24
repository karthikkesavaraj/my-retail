package com.myretail.myretailpdp

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableCircuitBreaker
class MyRetailPdpApplication{

	@Bean
	fun restTemplate(): RestTemplate {
		return RestTemplate()
	}

}

fun main(args: Array<String>) {
	runApplication<MyRetailPdpApplication>(*args)
}


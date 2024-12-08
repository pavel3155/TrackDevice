package com.example.TrackDevice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;


@SpringBootApplication
public class TrackDeviceApplication {
	@CacheEvict
	public static void main(String[] args) {
		SpringApplication.run(TrackDeviceApplication.class, args);
	}

}

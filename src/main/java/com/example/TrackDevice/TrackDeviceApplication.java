package com.example.TrackDevice;

import com.example.TrackDevice.model.ActDev;
import com.example.TrackDevice.repo.ActDevRepository;
import com.example.TrackDevice.service.ActDevService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDate;


@SpringBootApplication
public class TrackDeviceApplication implements CommandLineRunner {
	@Autowired
	private ActDevService actDevService;
	@Autowired
	private ActDevRepository actDevRepository;

	public static void main(String[] args) {
		SpringApplication.run(TrackDeviceApplication.class, args);
	}
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		ActDev actDev=actDevRepository.getById(1);
		System.out.println("actDev= "+actDev);
		LocalDate date = actDev.getAct_date();
		System.out.println("date= "+date);

	}
}
//actDevService.add(); // Вызываем метод add() после запуска приложения
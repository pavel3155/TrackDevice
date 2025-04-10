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
import java.time.format.DateTimeFormatter;


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
//		ActDev actDev=actDevRepository.getById(1);
//		System.out.println("actDev= "+actDev);
//
//		LocalDate date = actDev.getDate();
//		System.out.println("date= "+date);
//
//		String strDate=date.toString();
//		System.out.println("strDate= "+strDate);

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		String dateString = "2025-01-28";
//		LocalDate localDate = LocalDate.parse(dateString, formatter);
//		actDevService.add("ТС-02/001-Р-25", localDate); // Вызываем метод add() после запуска приложения
//		actDevService.add("ТС-02/002-Р-25", localDate);
//		actDevService.add("ТС-02/003-Р-25", localDate);

	}
}
//actDevService.add(); // Вызываем метод add() после запуска приложения
package com.machlou.collector;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

enum DataManipulation {
	MERGE, DELETE;
}

class Room{}
class Reservation{}

@Entity @Table(name = "SYNCHRONIZE_STATES") @Data @NoArgsConstructor @AllArgsConstructor @ToString
class SynchronizeState implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dataType;
	
	@Enumerated(EnumType.STRING)
	private DataManipulation manupulation;
}

@RepositoryRestResource
interface SynchronizeStateRepository extends JpaRepository<SynchronizeState, Long> {
}

@SpringBootApplication
public class DataCollectorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCollectorServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(SynchronizeStateRepository stateRepository) {
		return args -> {
			stateRepository.save(new SynchronizeState(null, Room.class.getSimpleName(), DataManipulation.MERGE));
			stateRepository.save(new SynchronizeState(null, Room.class.getSimpleName(), DataManipulation.DELETE));
			stateRepository.save(new SynchronizeState(null, Reservation.class.getSimpleName(), DataManipulation.MERGE));
			stateRepository.findAll().forEach(System.out::println);
		};
	}

}

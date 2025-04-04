package com.example.rent_car;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

	private List<Car> cars = new ArrayList<>();

	public CarController() {
		cars.add(new Car("11AA22", "Ferrari", 100));
		cars.add(new Car("33BB44", "Porsche", 150));
		cars.add(new Car("55CC66", "VroomVroom", 70));
		cars.add(new Car("77DD88", "Voiture", 80));
	}

	@GetMapping("/cars")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Car> listOfCars() {
		return cars;
	}

	@GetMapping("/cars/{plateNumber}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Car aCar(@PathVariable("plateNumber") String plateNumber) throws Exception {
		return cars.stream().filter(c -> c.getPlateNumber().equals(plateNumber)).findFirst()
		    .orElseThrow(() -> new Exception("Voiture non trouvée"));
	}

	@PutMapping(value = "/cars/{plateNumber}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> rentOrGetBack(@PathVariable("plateNumber") String plateNumber,
	    @RequestParam(value = "rent", required = true) boolean rent, @RequestBody(required = false) Dates dates)
	    throws Exception {

		Car car = cars.stream().filter(c -> c.getPlateNumber().equals(plateNumber)).findFirst()
		    .orElseThrow(() -> new Exception("Voiture non trouvée"));

		if (rent) {
			if (car.isRented()) {
				return ResponseEntity.badRequest().body("Voiture déjà louée");
			}
			if (dates == null || dates.getBegin() == null || dates.getEnd() == null) {
				return ResponseEntity.badRequest().body("Les dates de location sont obligatoires");
			}

			car.setRentalDates(dates);
			car.setRented(true);

			return ResponseEntity
			    .ok(String.format("Voiture %s louée du %s au %s", plateNumber, dates.getBegin(), dates.getEnd()));
		} else {
			if (!car.isRented()) {
				return ResponseEntity.badRequest().body("Voiture déjà disponible");
			}

			car.setRented(false);
			car.setRentalDates(null);

			return ResponseEntity.ok("Voiture " + plateNumber + " retournée avec succès");
		}
	}

}
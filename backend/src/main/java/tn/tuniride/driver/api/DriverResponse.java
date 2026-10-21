package tn.tuniride.driver.api;

import java.time.Instant;
import java.util.UUID;
import tn.tuniride.driver.domain.Driver;
import tn.tuniride.driver.domain.DriverStatus;

/** What the API exposes about a driver: never the JPA entity itself. */
public record DriverResponse(
		UUID id,
		String fullName,
		String phone,
		String taxiLicenseNumber,
		DriverStatus status,
		Instant createdAt) {

	public static DriverResponse from(Driver driver) {
		return new DriverResponse(
				driver.getId(),
				driver.getFullName(),
				driver.getPhone(),
				driver.getTaxiLicenseNumber(),
				driver.getStatus(),
				driver.getCreatedAt());
	}

}

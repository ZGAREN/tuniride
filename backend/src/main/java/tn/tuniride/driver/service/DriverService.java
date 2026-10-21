package tn.tuniride.driver.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.tuniride.common.ConflictException;
import tn.tuniride.common.NotFoundException;
import tn.tuniride.driver.api.CreateDriverRequest;
import tn.tuniride.driver.api.DriverResponse;
import tn.tuniride.driver.domain.Driver;
import tn.tuniride.driver.domain.DriverStatus;
import tn.tuniride.driver.repository.DriverRepository;

@Service
@Transactional(readOnly = true)
public class DriverService {

	private final DriverRepository drivers;

	public DriverService(DriverRepository drivers) {
		this.drivers = drivers;
	}

	@Transactional
	public DriverResponse register(CreateDriverRequest request) {
		// Friendly early check; the UNIQUE constraints in the database remain the real guarantee
		// when two requests race (see GlobalExceptionHandler#handleDataIntegrity).
		if (drivers.existsByPhone(request.phone())) {
			throw new ConflictException("A driver with phone " + request.phone() + " already exists");
		}
		if (drivers.existsByTaxiLicenseNumber(request.taxiLicenseNumber())) {
			throw new ConflictException("A driver with this taxi license number already exists");
		}
		Driver driver = new Driver(request.fullName(), request.phone(), request.taxiLicenseNumber());
		return DriverResponse.from(drivers.save(driver));
	}

	public DriverResponse get(UUID id) {
		return DriverResponse.from(find(id));
	}

	public List<DriverResponse> list(DriverStatus status) {
		List<Driver> result = status == null
				? drivers.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
				: drivers.findByStatusOrderByCreatedAtDesc(status);
		return result.stream().map(DriverResponse::from).toList();
	}

	@Transactional
	public DriverResponse changeStatus(UUID id, DriverStatus status) {
		Driver driver = find(id);
		driver.changeStatus(status);
		// No save() needed: the entity is managed, so Hibernate flushes the change at commit (dirty checking).
		return DriverResponse.from(driver);
	}

	private Driver find(UUID id) {
		return drivers.findById(id)
				.orElseThrow(() -> new NotFoundException("Driver " + id + " not found"));
	}

}

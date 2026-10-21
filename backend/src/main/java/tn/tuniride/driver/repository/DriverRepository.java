package tn.tuniride.driver.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.tuniride.driver.domain.Driver;
import tn.tuniride.driver.domain.DriverStatus;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

	boolean existsByPhone(String phone);

	boolean existsByTaxiLicenseNumber(String taxiLicenseNumber);

	List<Driver> findByStatusOrderByCreatedAtDesc(DriverStatus status);

}

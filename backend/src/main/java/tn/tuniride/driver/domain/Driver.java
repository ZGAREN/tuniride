package tn.tuniride.driver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

/**
 * A licensed taxi driver. Mapped onto the table created by V2__create_driver.sql;
 * Hibernate only validates the mapping, Flyway owns the schema.
 */
@Entity
@Table(name = "driver")
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "full_name", nullable = false, length = 100)
	private String fullName;

	@Column(nullable = false, unique = true, length = 12)
	private String phone;

	@Column(name = "taxi_license_number", nullable = false, unique = true)
	private String taxiLicenseNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private DriverStatus status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Version
	private int version;

	/** Required by JPA; not for application code. */
	protected Driver() {
	}

	public Driver(String fullName, String phone, String taxiLicenseNumber) {
		this.fullName = fullName;
		this.phone = phone;
		this.taxiLicenseNumber = taxiLicenseNumber;
		this.status = DriverStatus.OFFLINE;
		this.createdAt = Instant.now();
	}

	public void changeStatus(DriverStatus newStatus) {
		this.status = newStatus;
	}

	public UUID getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getPhone() {
		return phone;
	}

	public String getTaxiLicenseNumber() {
		return taxiLicenseNumber;
	}

	public DriverStatus getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public int getVersion() {
		return version;
	}

}

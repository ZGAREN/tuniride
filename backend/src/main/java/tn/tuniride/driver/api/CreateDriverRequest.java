package tn.tuniride.driver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDriverRequest(
		@NotBlank @Size(max = 100) String fullName,
		@NotBlank @Pattern(regexp = "^\\+216[0-9]{8}$", message = "must be a Tunisian number: +216 followed by 8 digits")
		String phone,
		@NotBlank @Size(max = 255) String taxiLicenseNumber) {
}

package tn.tuniride.driver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.tuniride.common.ConflictException;
import tn.tuniride.common.NotFoundException;
import tn.tuniride.driver.api.CreateDriverRequest;
import tn.tuniride.driver.api.DriverResponse;
import tn.tuniride.driver.domain.Driver;
import tn.tuniride.driver.domain.DriverStatus;
import tn.tuniride.driver.repository.DriverRepository;

/** Pure unit test: no Spring, no database. The repository is a Mockito mock. */
@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

	@Mock
	DriverRepository drivers;

	@InjectMocks
	DriverService service;

	private final CreateDriverRequest request = new CreateDriverRequest("Sami Ben Ali", "+21698123456", "TX-1001");

	@Test
	void registersANewDriverAsOffline() {
		when(drivers.existsByPhone(request.phone())).thenReturn(false);
		when(drivers.existsByTaxiLicenseNumber(request.taxiLicenseNumber())).thenReturn(false);
		when(drivers.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

		DriverResponse response = service.register(request);

		assertThat(response.fullName()).isEqualTo("Sami Ben Ali");
		assertThat(response.status()).isEqualTo(DriverStatus.OFFLINE);
	}

	@Test
	void refusesADuplicatePhoneWithoutSaving() {
		when(drivers.existsByPhone(request.phone())).thenReturn(true);

		assertThatThrownBy(() -> service.register(request)).isInstanceOf(ConflictException.class);
		verify(drivers, never()).save(any());
	}

	@Test
	void changesTheStatusOfAnExistingDriver() {
		UUID id = UUID.randomUUID();
		Driver driver = new Driver("Sami Ben Ali", "+21698123456", "TX-1001");
		when(drivers.findById(id)).thenReturn(Optional.of(driver));

		DriverResponse response = service.changeStatus(id, DriverStatus.AVAILABLE);

		assertThat(response.status()).isEqualTo(DriverStatus.AVAILABLE);
	}

	@Test
	void failsWhenTheDriverDoesNotExist() {
		UUID id = UUID.randomUUID();
		when(drivers.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.get(id)).isInstanceOf(NotFoundException.class);
	}

}

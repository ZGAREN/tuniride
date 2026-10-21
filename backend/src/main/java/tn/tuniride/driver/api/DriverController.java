package tn.tuniride.driver.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.tuniride.driver.domain.DriverStatus;
import tn.tuniride.driver.service.DriverService;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

	private final DriverService driverService;

	public DriverController(DriverService driverService) {
		this.driverService = driverService;
	}

	@PostMapping
	public ResponseEntity<DriverResponse> register(@Valid @RequestBody CreateDriverRequest request) {
		DriverResponse created = driverService.register(request);
		return ResponseEntity.created(URI.create("/api/drivers/" + created.id())).body(created);
	}

	@GetMapping("/{id}")
	public DriverResponse get(@PathVariable UUID id) {
		return driverService.get(id);
	}

	@GetMapping
	public List<DriverResponse> list(@RequestParam(required = false) DriverStatus status) {
		return driverService.list(status);
	}

	@PatchMapping("/{id}/status")
	public DriverResponse changeStatus(@PathVariable UUID id, @Valid @RequestBody UpdateDriverStatusRequest request) {
		return driverService.changeStatus(id, request.status());
	}

}

package tn.tuniride.driver.api;

import jakarta.validation.constraints.NotNull;
import tn.tuniride.driver.domain.DriverStatus;

public record UpdateDriverStatusRequest(@NotNull DriverStatus status) {
}

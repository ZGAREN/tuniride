package tn.tuniride;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import tn.tuniride.support.IntegrationTest;

/**
 * Smoke test: the Spring context boots and Flyway migrations apply against a real
 * PostGIS database (the same image as infra/docker-compose.yml), not an in-memory fake.
 */
class TuniRideApplicationTests extends IntegrationTest {

	@Autowired
	JdbcTemplate jdbc;

	@Test
	void contextLoadsAndPostgisIsEnabled() {
		String version = jdbc.queryForObject("SELECT postgis_version()", String.class);
		assertThat(version).startsWith("3.5");
	}

}

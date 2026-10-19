package tn.tuniride;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Smoke test: the Spring context boots and Flyway migrations apply against a real
 * PostGIS database (the same image as infra/docker-compose.yml), not an in-memory fake.
 */
@SpringBootTest
@Testcontainers
class TuniRideApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			DockerImageName.parse("postgis/postgis:17-3.5").asCompatibleSubstituteFor("postgres"));

	@Autowired
	JdbcTemplate jdbc;

	@Test
	void contextLoadsAndPostgisIsEnabled() {
		String version = jdbc.queryForObject("SELECT postgis_version()", String.class);
		assertThat(version).startsWith("3.5");
	}

}

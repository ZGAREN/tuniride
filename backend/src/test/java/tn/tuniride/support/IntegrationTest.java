package tn.tuniride.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for tests that need the full Spring context and a real PostGIS database.
 * The container is started once per JVM ("singleton container" pattern) and shared by every
 * subclass, which also lets Spring reuse one cached application context.
 */
@SpringBootTest
public abstract class IntegrationTest {

	@ServiceConnection
	protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
			DockerImageName.parse("postgis/postgis:17-3.5").asCompatibleSubstituteFor("postgres"));

	static {
		POSTGRES.start();
	}

}

package tn.tuniride.driver.api;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.tuniride.support.IntegrationTest;

/**
 * Full stack: HTTP request -> controller -> service -> JPA -> real PostGIS -> JSON response.
 * Each test uses its own phone/license numbers so tests stay independent on the shared database.
 */
@AutoConfigureMockMvc
class DriverApiIntegrationTest extends IntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void registersFetchesAndMakesADriverAvailable() throws Exception {
		String id = register("Sami Ben Ali", "+21698000001", "TX-2001");

		mockMvc.perform(get("/api/drivers/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.phone").value("+21698000001"))
				.andExpect(jsonPath("$.status").value("OFFLINE"));

		mockMvc.perform(patch("/api/drivers/" + id + "/status")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"status": "AVAILABLE"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("AVAILABLE"));

		mockMvc.perform(get("/api/drivers").param("status", "AVAILABLE"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].id", hasItem(id)));

		mockMvc.perform(get("/api/drivers").param("status", "OFFLINE"))
				.andExpect(jsonPath("$[*].id", not(hasItem(id))));
	}

	@Test
	void rejectsADuplicatePhoneWith409() throws Exception {
		register("Sami Ben Ali", "+21698000002", "TX-2002");

		mockMvc.perform(post("/api/drivers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(driverJson("Another Driver", "+21698000002", "TX-2003")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void rejectsAnInvalidPhoneWith400AndNamesTheField() throws Exception {
		mockMvc.perform(post("/api/drivers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(driverJson("Sami Ben Ali", "0612345678", "TX-2004")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.phone").exists());
	}

	@Test
	void returns404ForAnUnknownDriver() throws Exception {
		mockMvc.perform(get("/api/drivers/00000000-0000-0000-0000-000000000000"))
				.andExpect(status().isNotFound());
	}

	private String register(String fullName, String phone, String license) throws Exception {
		String body = mockMvc.perform(post("/api/drivers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(driverJson(fullName, phone, license)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn().getResponse().getContentAsString();
		return JsonPath.read(body, "$.id");
	}

	private static String driverJson(String fullName, String phone, String license) {
		return """
				{"fullName": "%s", "phone": "%s", "taxiLicenseNumber": "%s"}
				""".formatted(fullName, phone, license);
	}

}

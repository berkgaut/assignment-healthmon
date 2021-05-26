package tools.berkgaut.assignment.healthmon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import tools.berkgaut.assignment.healthmon.apiobjects.Service;
import tools.berkgaut.assignment.healthmon.components.HealthChecker;
import tools.berkgaut.assignment.healthmon.components.HealthMonitor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthmonApplicationTests {

	@LocalServerPort
	private int localServerPort;
	
	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@MockBean
	HealthChecker mockHealthChecker;

	@Autowired
	HealthMonitor healthMonitor;
	public static final TypeReference<ArrayList<Service>> LIST_OF_SERVICES_TYPE = new TypeReference<>() {};

	@Test
	public void serviceCrudFlow() throws JsonProcessingException {
		cleanupDb();

		// GIVEN no services added yet
		// WHEN list of services is requested
		// THEN the result is an empty list
		{
			ResponseEntity<String> response = getServices();
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			List<Service> serviceList = (new ObjectMapper()).readValue(response.getBody(), LIST_OF_SERVICES_TYPE);
			assertThat(serviceList).isEmpty();
		}

		// GIVEN no services added yet
		// WHEN a asked to create a new service
		// THEN the result contains a service with assigned ID
		String serviceId;
		{
			Service serviceData = Service.builder()
					.name("test service")
					.url("https://test-service.example.com:6789/")
					.timeoutMillis(9000)
					.build();
			ResponseEntity<String> response = createService((new ObjectMapper()).writeValueAsString(serviceData));
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			Service newService = (new ObjectMapper()).readValue(response.getBody(), Service.class);
			assertThat(newService.getId()).isNotBlank();
			assertThat(newService.getName()).isEqualTo("test service");
			assertThat(newService.getCreated()).isNotBlank();
			assertThat(newService.getUpdated()).isNotBlank(); // never updated yet
			assertThat(newService.getStatus()).isEqualTo("UNKNOWN"); // because no check performed yet
			serviceId = newService.getId();
		}

		// GIVEN a service exists
		// WHEN asked to update a service
		// THEN the result contains an updated service
		{
			Service updateData = Service.builder()
					.name("test service 2")
					.url("https://test-service-2.example.com:7890")
					.build();
			ResponseEntity<String> response = updateService(serviceId, (new ObjectMapper()).writeValueAsString(updateData));
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			Service updatedService = (new ObjectMapper()).readValue(response.getBody(), Service.class);
			assertThat(updatedService.getName()).isEqualTo("test service 2");
			assertThat(updatedService.getUrl()).isEqualTo("https://test-service-2.example.com:7890");
		}

		// GIVEN a service exists
		// WHEN asked to deleted a service
		{
			ResponseEntity<Void> response = deleteService(serviceId);
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
		}
		// THEN service list is empty
		{
			ResponseEntity<String> response = getServices();
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			List<Service> serviceList = (new ObjectMapper()).readValue(response.getBody(), LIST_OF_SERVICES_TYPE);
			assertThat(serviceList).isEmpty();
		}
	}

	@Test
	public void healthy() throws Exception {
		cleanupDb();
		{
			Service serviceData = Service.builder()
					.name("test service")
					.url("https://test-service.example.com:6789/")
					.timeoutMillis(9000)
					.build();
			ResponseEntity<String> response = createService((new ObjectMapper()).writeValueAsString(serviceData));
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
		}
		when(mockHealthChecker.performCheck(any(), any())).thenReturn(true);
		healthMonitor.performChecks();
		{
			ResponseEntity<String> response = getServices();
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			List<Service> serviceList = (new ObjectMapper()).readValue(response.getBody(), LIST_OF_SERVICES_TYPE);
			assertThat(serviceList).hasSize(1);
			assertThat(serviceList.get(0).getStatus()).isEqualTo("OK");
		}

	}

		// ---- helper methods

	private ResponseEntity<String> getServices() {
		URI uri = getServiceUriBuilder().path("/v1/services").build();
		RequestEntity<Void> request = RequestEntity.get(uri).build();
		return restTemplate.exchange(request, String.class);
	}

	private ResponseEntity<String> createService(String createData) {
		URI uri = getServiceUriBuilder().path("/v1/services").build();
		RequestEntity<String> request = RequestEntity
				.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(createData);
		return restTemplate.exchange(request, String.class);
	}

	private ResponseEntity<String> updateService(String serviceId, String updateData) {
		URI uri = getServiceUriBuilder().path("/v1/services/{serviceId}").build(serviceId);
		RequestEntity<String> request = RequestEntity
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(updateData);
		return restTemplate.exchange(request, String.class);
	}

	private ResponseEntity<Void> deleteService(String serviceId) {
		URI uri = getServiceUriBuilder().path("/v1/services/{serviceId}").build(serviceId);
		RequestEntity<Void> request = RequestEntity.delete(uri).build();
		return restTemplate.exchange(request, Void.class);
	}

	private UriBuilder getServiceUriBuilder() {
		UriBuilderFactory factory = new DefaultUriBuilderFactory();
		return factory.builder().scheme("http").host("localhost").port(localServerPort);
	}

	private void cleanupDb() {
		jdbcTemplate.execute("delete from healthcheck");
		jdbcTemplate.execute("delete from service");
	}
}

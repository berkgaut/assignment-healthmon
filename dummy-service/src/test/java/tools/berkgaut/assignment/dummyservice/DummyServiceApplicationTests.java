package tools.berkgaut.assignment.dummyservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class DummyServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;


	public String baseUrl() {
		return "http://localhost:" + port;
	}

	@Test
	void responseOk() {
		ResponseEntity<String> response = get("/health/200");
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	void response404() {
		ResponseEntity<String> response = get("/health/404");
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}

	@Test
	void responseBadCode() {
		ResponseEntity<String> response = get("/health/i-am-not-a-number");
		assertThat(response.getStatusCodeValue()).isEqualTo(500);
	}

	@Test
	void responseTimeout() {
		RestTemplate restTemplateWithTimeout =
				new RestTemplateBuilder()
						.setReadTimeout(Duration.ofMillis(1000))
						.build();

		RequestEntity<Void> requestEntity = createRequestEntity("/health/200", 1500L);
		assertThatThrownBy(() -> restTemplateWithTimeout.exchange(requestEntity, String.class))
				.isInstanceOf(RestClientException.class);
	}

	// ---- Helpers

	private ResponseEntity<String> get(String path) {
		RequestEntity<Void> requestEntity = createRequestEntity(path, null);
		return restTemplate.exchange(requestEntity, String.class);
	}

	private RequestEntity<Void> createRequestEntity(String path, Long delayMs) {
		UriBuilderFactory factory = new DefaultUriBuilderFactory();
		URI uri = factory.uriString(baseUrl())
				.path(path)
				.queryParam("delayMs", delayMs == null ? "" : delayMs.toString())
				.build();
		return RequestEntity.get(uri)
				.build();
	}
}

package com.example.demo.consumer.contract.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(value = {PactConsumerTestExt.class})
@PactTestFor(providerName = "note_provider", port = "48083")
public class NoteServiceConsumerTest {

    public static final String PATH_GET_NOTA = "/nota";
    private RestTemplate restTemplate = new RestTemplate();


    @Pact(consumer = "note_consumer")
    public RequestResponsePact noteFromIdPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        PactDslJsonBody bodyResponse = new PactDslJsonBody()
                .stringType("id", "1")
                .stringType("content", "A simple note")
                .date("createAt", "yyyy-MM-dd")
                .stringType("author", "John Doe")
                .asBody();

        return builder
                .given("GET nota 200")
                .uponReceiving("A request to nota with id")
                .matchPath("/nota/1")
                .method(RequestMethod.GET.name())
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(bodyResponse)
                .toPact();
    }

    @Pact(consumer = "note_consumer")
    public RequestResponsePact noteFromIdPactNoContent(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        return builder
                .given("GET note 404")
                .uponReceiving("A not found note")
                .matchPath("/nota/101")
                .method(RequestMethod.GET.name())
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "noteFromIdPactNoContent")
    public void testNoteConsumerNoContent(MockServer mockProvider) throws IOException {
        UriComponentsBuilder builderOk = UriComponentsBuilder.fromUriString(mockProvider.getUrl() + PATH_GET_NOTA + "/{id}");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", "101");

        assertThatThrownBy(() -> {
            restTemplate.getForEntity(builderOk.buildAndExpand(urlParams).toUri(), Void.class);
        })
                .isInstanceOf(HttpClientErrorException.class).hasMessage("404 Not Found: [no body]");
    }

    @Test
    @PactTestFor(pactMethod = "noteFromIdPact")
    public void testNoteConsumer(MockServer mockProvider) throws IOException {
        // GET OK
        UriComponentsBuilder builderOk = UriComponentsBuilder.fromUriString(mockProvider.getUrl() + PATH_GET_NOTA + "/{id}");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", "1");


        ResponseEntity<String> responseEntity = restTemplate.getForEntity(builderOk.buildAndExpand(urlParams).toUri(), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().get("Content-Type").get(0).toString()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

}

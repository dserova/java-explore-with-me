package ru.practicum.explorewithmestatsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithmestatsclient.client.BaseClient;
import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;


@Service
@Slf4j
public class ClientCreateHit extends BaseClient {

    private static final String API_PREFIX = "/hit";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ClientCreateHit(@Value("${ewm-stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public EndpointHitDto createHit(EndpointHitDto hit) {
        try {
            ResponseEntity<Object> object = post("", hit);
            return mapper.convertValue(object.getBody(), EndpointHitDto.class);
        } catch (Exception e) {
            log.error("Check statistic service.");
            return new EndpointHitDto();
        }
    }
}

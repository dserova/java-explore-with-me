package ru.practicum.explorewithmestatsclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithmestatsclient.client.BaseClient;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ClientGetStats extends BaseClient {

    private static final String API_PREFIX = "/stats";
    final Mapping mapping = new Mapping();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ClientGetStats(@Value("${ewm-stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    private <T> String buildURI(String key, T value, MultiValueMap<String, T> storage) {
        String pathUrl = (storage.isEmpty() ? "?" : "&") + key + "={" + key + "}";
        storage.add(key, value);
        return pathUrl;
    }

    private <T> String buildURIList(String key, List<T> values, MultiValueMap<String, Object> storage) {
        StringBuilder pathUrl = new StringBuilder();
        Integer i = 0;
        for (T value : values) {
            pathUrl
                    .append(storage.isEmpty() ? "?" : "&")
                    .append(key)
                    .append("={")
                    .append(key).append(i)
                    .append("}");
            storage.add(key + i, value);
            i++;
        }
        return pathUrl.toString();
    }

    public List<ViewStatsDto> getStats(Calendar start, Calendar end, List<String> uris, Boolean unique) {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        String pathUrl = buildURIList("uris", uris, param) +
                buildURI("start", start.toInstant(), param) +
                buildURI("end", end.toInstant(), param) +
                buildURI("unique", unique, param);
        ResponseEntity<List<ViewStatsDto>> object = get(pathUrl, param.toSingleValueMap());
        return mapping.mapList(Objects.requireNonNull(object.getBody()), ViewStatsDto.class);
    }

    public List<ViewStatsDto> getStats(Calendar start, Calendar end, List<String> uris) {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        String pathUrl = buildURIList("uris", uris, param) +
                buildURI("start", start.toInstant(), param) +
                buildURI("end", end.toInstant(), param);
        ResponseEntity<List<ViewStatsDto>> object = get(pathUrl, param.toSingleValueMap());
        return mapping.mapList(Objects.requireNonNull(object.getBody()), ViewStatsDto.class);
    }

    public List<ViewStatsDto> getStats(Calendar start, Calendar end, Boolean unique) {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        String pathUrl = buildURI("start", start.toInstant(), param) +
                buildURI("end", end.toInstant(), param) +
                buildURI("unique", unique, param);
        ResponseEntity<List<ViewStatsDto>> object = get(pathUrl, param.toSingleValueMap());
        return mapping.mapList(Objects.requireNonNull(object.getBody()), ViewStatsDto.class);
    }

    public List<ViewStatsDto> getStats(Calendar start, Calendar end) {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        String pathUrl = buildURI("start", start.toInstant(), param) +
                buildURI("end", end.toInstant(), param);
        ResponseEntity<List<ViewStatsDto>> object = get(pathUrl, param.toSingleValueMap());
        return mapping.mapList(Objects.requireNonNull(object.getBody()), ViewStatsDto.class);
    }

    @NoArgsConstructor
    public class Mapping {
        public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
            return source
                    .stream()
                    .map(element -> mapper.convertValue(element, targetClass))
                    .collect(Collectors.toList());
        }
    }
}

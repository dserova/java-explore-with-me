package ru.practicum.explorewithmestatsserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.explorewithmestatsclient.service.ClientCreateHit;
import ru.practicum.explorewithmestatsclient.service.ClientGetStats;
import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;
import ru.practicum.explorewithmestatsserver.helpers.Generate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExploreWithMeStatsServiceApplicationTests {

    final RestTemplateBuilder builder = new RestTemplateBuilder();
    @Value("${local.server.port}")
    private Integer port;

    @Test
    void clientTest() {
        ClientCreateHit clientCreateHit = new ClientCreateHit("http://localhost:" + port, builder);
        ClientGetStats clientGetStats = new ClientGetStats("http://localhost:" + port, builder);

        // Generate hits
        EndpointHitDto hit1 = new Generate().random(EndpointHitDto.class);
        EndpointHitDto hit2 = new Generate().random(EndpointHitDto.class);
        EndpointHitDto hit3 = new Generate().random(EndpointHitDto.class);
        Calendar t1 = hit3.getTimestamp();
        t1.add(Calendar.YEAR, 2);
        hit3.setUri("/event/2");
        hit3.setTimestamp(t1);
        EndpointHitDto hit4 = new Generate().random(EndpointHitDto.class);
        hit4.setUri("/event/2");
        hit4.setTimestamp(t1);
        EndpointHitDto hit5 = new Generate().random(EndpointHitDto.class);
        hit5.setUri("/event/2");
        hit5.setTimestamp(t1);

        // Generate calendars
        Calendar now1 = Calendar.getInstance();
        now1.add(Calendar.YEAR, -10);

        Calendar now2 = Calendar.getInstance();
        now2.add(Calendar.YEAR, 10);

        Calendar now3 = Calendar.getInstance();
        now3.add(Calendar.YEAR, 1);

        // Add hits
        EndpointHitDto b = clientCreateHit.createHit(hit1);
        log.info("1) hit=\n{}", b);
        b = clientCreateHit.createHit(hit2);
        log.info("2) hit=\n{}", b);
        b = clientCreateHit.createHit(hit3);
        log.info("3) hit=\n{}", b);
        b = clientCreateHit.createHit(hit4);
        log.info("4) hit=\n{}", b);
        b = clientCreateHit.createHit(hit5);
        log.info("5) hit=\n{}", b);

        // Get stats
        List<String> uris = new ArrayList<>();
        uris.add("/event/1");

        List<ViewStatsDto> stats1 = clientGetStats.getStats(now1, now2, uris, true);
        List<ViewStatsDto> vsl = new ArrayList<>();
        vsl.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats1={}\n{}", stats1, vsl);
        Assertions.assertEquals(stats1, vsl);

        List<ViewStatsDto> stats2 = clientGetStats.getStats(now1, now2, uris);
        List<ViewStatsDto> vsl2 = new ArrayList<>();
        vsl2.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats2, vsl2);
        Assertions.assertEquals(stats2, vsl2);

        List<ViewStatsDto> stats3 = clientGetStats.getStats(now1, now2, true);
        List<ViewStatsDto> vsl3 = new ArrayList<>();
        vsl3.add(new ViewStatsDto("TEST APP", "/event/2", 3L));
        vsl3.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats3, vsl3);
        Assertions.assertEquals(stats3, vsl3);

        List<ViewStatsDto> stats4 = clientGetStats.getStats(now1, now2, false);
        List<ViewStatsDto> vsl4 = new ArrayList<>();
        vsl4.add(new ViewStatsDto("TEST APP", "/event/2", 3L));
        vsl4.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats4, vsl4);
        Assertions.assertEquals(stats4, vsl4);

        List<ViewStatsDto> stats5 = clientGetStats.getStats(now1, now2);
        List<ViewStatsDto> vsl5 = new ArrayList<>();
        vsl5.add(new ViewStatsDto("TEST APP", "/event/2", 3L));
        vsl5.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats5, vsl5);
        Assertions.assertEquals(stats5, vsl5);

        uris.add("/event/2");

        List<ViewStatsDto> stats6 = clientGetStats.getStats(now1, now2, uris, true);
        List<ViewStatsDto> vsl6 = new ArrayList<>();
        vsl6.add(new ViewStatsDto("TEST APP", "/event/2", 3L));
        vsl6.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats6, vsl6);
        Assertions.assertEquals(stats6, vsl6);

        List<ViewStatsDto> stats7 = clientGetStats.getStats(now1, now2, uris);
        List<ViewStatsDto> vsl7 = new ArrayList<>();
        vsl7.add(new ViewStatsDto("TEST APP", "/event/2", 3L));
        vsl7.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats7, vsl7);
        Assertions.assertEquals(stats7, vsl7);

        List<ViewStatsDto> stats8 = clientGetStats.getStats(now1, now3, uris, true);
        List<ViewStatsDto> vsl8 = new ArrayList<>();
        vsl8.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats8, vsl8);
        Assertions.assertEquals(stats8, vsl8);

        List<ViewStatsDto> stats9 = clientGetStats.getStats(now1, now3, uris);
        List<ViewStatsDto> vsl9 = new ArrayList<>();
        vsl9.add(new ViewStatsDto("TEST APP", "/event/1", 2L));
        log.info("stats2={}\n{}", stats9, vsl9);
        Assertions.assertEquals(stats9, vsl9);
    }
}

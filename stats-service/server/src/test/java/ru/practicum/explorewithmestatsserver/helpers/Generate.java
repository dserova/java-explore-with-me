package ru.practicum.explorewithmestatsserver.helpers;

import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.Ipv4AddressRandomizer;
import org.jeasy.random.randomizers.range.LocalDateTimeRangeRandomizer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static org.jeasy.random.FieldPredicates.*;

@RequiredArgsConstructor
public class Generate {

    final DateTimeFormatter myCustomFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public <T> T random(Class<T> targetClass) {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(12345L)
                .randomize(named("timestamp").and(ofType(LocalDateTime.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusSeconds(1),
                        LocalDateTime.now().plusSeconds(30)
                ).getRandomValue())
                //.randomize(named("timestamp").and(ofType(Calendar.class)), () -> new CalendarRandomizer().getRandomValue())
                .randomize(named("timestamp").and(ofType(Calendar.class)), Calendar::getInstance)
                .randomize(named("timestamp").and(ofType(String.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusSeconds(1),
                        LocalDateTime.now().plusSeconds(30)
                ).getRandomValue().format(myCustomFormatter))
                //.randomize(named("app").and(ofType(String.class)), () -> new CityRandomizer().getRandomValue())
                .randomize(named("app").and(ofType(String.class)), () -> "TEST APP")
                .randomize(named("uri").and(ofType(String.class)), () -> "/event/" + "1")
                .randomize(named("ip").and(ofType(String.class)), () -> new Ipv4AddressRandomizer().getRandomValue())
                .objectPoolSize(100)
                .randomizationDepth(15)
                .charset(StandardCharsets.UTF_8)
                .stringLengthRange(5, 50)
                .collectionSizeRange(1, 10)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(false)
                .ignoreRandomizationErrors(true)
                .excludeField(named("id").and(inClass(targetClass)));
        EasyRandom generator = new EasyRandom(parameters);
        return generator.nextObject(targetClass);
    }
}

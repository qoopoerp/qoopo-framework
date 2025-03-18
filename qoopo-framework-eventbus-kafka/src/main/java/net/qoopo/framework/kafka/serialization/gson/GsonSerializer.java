package net.qoopo.framework.kafka.serialization.gson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.qoopo.framework.kafka.util.InstantAdapter;
import net.qoopo.framework.kafka.util.LocalDateAdapter;
import net.qoopo.framework.kafka.util.LocalDateTimeAdapter;
import net.qoopo.framework.kafka.util.LocalTimeAdapter;
import net.qoopo.framework.kafka.util.OffsetDateTimeAdapter;
import net.qoopo.framework.kafka.util.OffsetTimeAdapter;
import net.qoopo.framework.kafka.util.ZonedDateTimeAdapter;

public class GsonSerializer<T> implements Serializer<T> {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(OffsetTime.class, new OffsetTimeAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    @Override
    public byte[] serialize(String topic, T data) {
        return data == null ? null : gson.toJson(data).getBytes();
    }
}

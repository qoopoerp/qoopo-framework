package net.qoopo.framework.kafka.serialization.gson;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.qoopo.framework.kafka.util.InstantAdapter;
import net.qoopo.framework.kafka.util.LocalDateAdapter;
import net.qoopo.framework.kafka.util.LocalDateTimeAdapter;
import net.qoopo.framework.kafka.util.LocalTimeAdapter;
import net.qoopo.framework.kafka.util.OffsetDateTimeAdapter;
import net.qoopo.framework.kafka.util.OffsetTimeAdapter;
import net.qoopo.framework.kafka.util.ZonedDateTimeAdapter;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE = "gson.deserializer.type";
    public static final String TYPE_MAPPING = "gson.deserializer.type.mapping";
    public static final String TYPE_FIELD = "type";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(OffsetTime.class, new OffsetTimeAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();
    private Class<T> type;

    private Map<String, String> mapMapping = null;

    public GsonDeserializer() {
        // Se inicializa vacío porque Kafka lo necesita sin argumentos
    }

    public GsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (configs.containsKey(TYPE)) {
            try {
                this.type = (Class<T>) Class.forName((String) configs.get(TYPE));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("No se pudo encontrar la clase para JSON deserialization", e);
            }
        }
        if (configs.containsKey(TYPE_MAPPING)) {
            try {
                this.mapMapping = (Map) configs.get(TYPE_MAPPING);
            } catch (Exception e) {
                throw new RuntimeException("No se pudo leer el Mapping configurado", e);
            }
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        String json = new String(data, StandardCharsets.UTF_8);

        Class<T> currentType = null;

        try {
            // mira si se ha definido un valor type en la clase
            Map<String, Object> rawData = gson.fromJson(json, Map.class);
            String subType = (String) rawData.get(TYPE_FIELD);
            if (mapMapping != null) {
                if (mapMapping.containsKey(subType)) {
                    subType = mapMapping.get(subType);
                }
            }
            currentType = (Class<T>) Class.forName(subType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo encontrar la clase para JSON deserialization", e);
        }

        if (currentType == null)
            currentType = type;

        if (currentType == null) {
            // return null;
            return gson.fromJson(json, new TypeToken<T>() {
            }.getType());
        } else {
            return gson.fromJson(json, currentType);
        }

    }

    @Override
    public void close() {
    }
}
package net.qoopo.framework.kafka.serialization.jsonb;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class JsonbDeserializer<T> implements Deserializer<T> {

    public static final String TYPE = "json.deserializer.type";
    public static final String TYPE_MAPPING = "json.deserializer.type.mapping";
    public static final String TYPE_FIELD = "type";

    private static final Jsonb jsonb = JsonbBuilder.create();
    private Class<T> type;

    private Map<String, String> mapMapping = null;

    public JsonbDeserializer() {
        // Se inicializa vacío porque Kafka lo necesita sin argumentos
    }

    public JsonbDeserializer(Class<T> type) {
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

        if (data == null)
            return null;

        String json = new String(data, StandardCharsets.UTF_8);

        Class<T> currentType = null;
        try {
            // mira si se ha definido un valor type en la clase
            Map<String, Object> rawData = jsonb.fromJson(json, Map.class);
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
            return null;
        }
        return jsonb.fromJson(json, currentType);
    }

    @Override
    public void close() {
    }
}
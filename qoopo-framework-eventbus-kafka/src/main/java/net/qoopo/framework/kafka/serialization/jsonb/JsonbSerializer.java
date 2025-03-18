package net.qoopo.framework.kafka.serialization.jsonb;

import org.apache.kafka.common.serialization.Serializer;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class JsonbSerializer<T> implements Serializer<T> {
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    public byte[] serialize(String topic, T data) {
        return data == null ? null : jsonb.toJson(data).getBytes();
    }
}

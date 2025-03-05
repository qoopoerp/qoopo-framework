package net.qoopo.framework.serialization;

/**
 * Permite serializar un objeto Java
 */
public interface Serializer<INPUT, OUTPUT> {
    public OUTPUT serialize(INPUT input);

    public INPUT deserialize(OUTPUT input);

    public default OUTPUT write(INPUT input) {
        return serialize(input);
    }

    public default INPUT read(OUTPUT input) {
        return deserialize(input);
    }
}

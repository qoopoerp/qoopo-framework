package net.qoopo.framework.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteSerializer implements Serializer<Object, byte[]> {

    public byte[] serialize(Object object) {
        byte[] data = null;
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(bos);) {
                out.writeObject(object);
                data = bos.toByteArray();
            }
        } catch (IOException ex) {
        }
        return data;
    }

    public Object deserialize(byte[] datos) {
        Object object = null;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(datos))) {
            object = in.readObject();
        } catch (Exception ex) {
            //
        }
        return object;
    }

}

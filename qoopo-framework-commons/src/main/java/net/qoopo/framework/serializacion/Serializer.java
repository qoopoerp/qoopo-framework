package net.qoopo.framework.serializacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serializer {

    public static byte[] write(Object object) {
        byte[] data = null;
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(bos);) {
                out.writeObject(object);
                data = bos.toByteArray();
            }
        } catch (IOException ex) {
        }
        return data;
    }

    public static void write(Object object, OutputStream outStream) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(outStream)) {
            out.writeObject(object);
        }
    }

    public static void writeToFile(Object object, String file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(object);
        }
    }

    public static Object read(InputStream stream) throws IOException, ClassNotFoundException {
        Object object;
        try (ObjectInputStream in = new ObjectInputStream(stream)) {
            object = in.readObject();
        }
        return object;
    }

    public static Object read(byte[] datos) {
        Object object = null;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(datos))) {
            object = in.readObject();
        } catch (Exception ex) {
            //
        }
        return object;
    }

    public static Object readFromFile(String file) throws IOException, ClassNotFoundException {
        Object object;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            object = in.readObject();
        }
        return object;
    }
}

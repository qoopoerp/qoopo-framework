package net.qoopo.framework.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class StreamSerializer {

    public void write(Object object, OutputStream outStream) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(outStream)) {
            out.writeObject(object);
        }
    }

    public Object read(InputStream stream) throws IOException, ClassNotFoundException {
        Object object;
        try (ObjectInputStream in = new ObjectInputStream(stream)) {
            object = in.readObject();
        }
        return object;
    }

}

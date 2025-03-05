package net.qoopo.framework.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileSeriliazer {

    public void serialize(Object object, File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(object);
        }
    }

    public Object deserialize(File file) throws IOException, ClassNotFoundException {
        Object object;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            object = in.readObject();
        }
        return object;
    }

}

package net.qoopo.framework.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.qoopo.framework.compressor.GZIPCompressorUtil;

/**
 * Permite seralizar objetos para almacenarlos en un arhivo
 *
 * @author noroot
 */
public class SerializerFile {

    public static void addObject(String filename, Object obj, boolean append, boolean comprimir) {
        File file = new File(filename);
        ObjectOutputStream out = null;
        try {
            if (!file.exists() || !append) {
                out = new ObjectOutputStream(new FileOutputStream(filename));
            } else {
                out = new AppendableObjectOutputStream(new FileOutputStream(filename, append));
            }
            if (comprimir) {
                out.writeObject(GZIPCompressorUtil.compress(obj));
            } else {
                out.writeObject(obj);
            }
            out.flush();
        } catch (Exception e) {

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static List<Object> readObjects(String filename) {
        File file = new File(filename);
        List<Object> lst = new ArrayList<>();
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));) {
                while (true) {
                    lst.add(ois.readObject());
                }
            } catch (Exception e) {

            }
        }
        return lst;
    }

    public static int countObjects(String filename) {
        File file = new File(filename);
        int total = 0;
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));) {
                while (true) {
                    ois.readObject();
                    total++;
                }
            } catch (Exception e) {
            }
        }
        return total;
    }

    public static Object readObject(String filename, int indice) {
        return readObject(filename, indice, false);
    }

    public static Object readObject(String filename, int indice, boolean comprimido) {
        File file = new File(filename);
        int total = 0;
        Object objeto = null;
        boolean seguir = true;
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));) {
                while (seguir) {
                    objeto = ois.readObject();
                    if (total == indice) {
                        //descomprimo
                        if (comprimido) {
                            objeto = GZIPCompressorUtil.decompress((byte[]) objeto);
                        }
                        seguir = false;
                        break;
                    } else {
                        objeto = null;
                    }
                    total++;
                }
            } catch (Exception e) {

            }
        }
        return objeto;
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // do not write a header, but reset:
            // this line added after another question
            // showed a problem with the original
            reset();
        }
    }
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        List list = new ArrayList();
//        list.add("uno");
//        list.add("dos");
//        list.add("tres");
//        list.add("cuatro");
//        SerializarUtil.writeObject(list, "prueba.txt");
//        List otherList = (List) SerializarUtil.readObject("prueba.txt");
//        System.out.println("La lista original es igual a la leida ? " + list.equals(otherList));
//    }
}

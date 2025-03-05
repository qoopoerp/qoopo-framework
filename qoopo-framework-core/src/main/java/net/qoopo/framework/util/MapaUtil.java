package net.qoopo.framework.util;

import java.util.HashMap;

import net.qoopo.framework.serialization.ByteSerializer;

public class MapaUtil {

    private static ByteSerializer serializer = new ByteSerializer();

    private MapaUtil() {
        //
    }

    public static byte[] agregarValor(String clave, Object valor, byte[] origen) {
        HashMap<String, Object> mapa;
        if (origen != null) {
            try {
                mapa = (HashMap<String, Object>) serializer.read(origen);
            } catch (Exception e) {
                mapa = new HashMap<>();
            }
        } else {
            mapa = new HashMap<>();
        }
        mapa.put(clave, valor);
        return serializer.write(mapa);
    }

    public static Object leerValor(String clave, byte[] origen) {
        HashMap<String, Object> mapa;
        if (origen != null) {
            try {
                mapa = (HashMap<String, Object>) serializer.read(origen);
            } catch (Exception e) {
                mapa = new HashMap<>();
            }

        } else {
            mapa = new HashMap<>();
        }
        return mapa.get(clave);
    }
}

package net.qoopo.qoopoframework.util;

import java.util.HashMap;

import net.qoopo.util.serializacion.Serializer;

public class MapaUtil {

    private MapaUtil() {
        //
    }

    public static byte[] agregarValor(String clave, Object valor, byte[] origen) {
        HashMap<String, Object> mapa;
        if (origen != null) {
            try {
                mapa = (HashMap<String, Object>) Serializer.read(origen);
            } catch (Exception e) {
                mapa = new HashMap<>();
            }
        } else {
            mapa = new HashMap<>();
        }
        mapa.put(clave, valor);
        return Serializer.write(mapa);
    }

    public static Object leerValor(String clave, byte[] origen) {
        HashMap<String, Object> mapa;
        if (origen != null) {
            try {
                mapa = (HashMap<String, Object>) Serializer.read(origen);
            } catch (Exception e) {
                mapa = new HashMap<>();
            }

        } else {
            mapa = new HashMap<>();
        }
        return mapa.get(clave);
    }
}

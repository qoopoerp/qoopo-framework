package net.qoopo.framework.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class QoopoReflection {

    private static Logger log = Logger.getLogger("qoopo-reflection");

    private static Map<Class<?>, List<Object>> instances = new HashMap<>();

    private static String[] PREFIX = { "net", "com", "ec", "org" };

    public static void registerPrefix(String... prefix) {
        PREFIX = prefix;
    }

    /**
     * Busca una implementacion de una anotacion
     * 
     * @param annotationClass
     * @return
     */
    public static List getBeanAnnotaded(Class<? extends Annotation> annotationClass) {
        return getBeanAnnotaded(annotationClass, true, null);
    }

    public static List getBeanAnnotaded(Class<? extends Annotation> annotationClass, Collection<Class> ignoredClass) {
        return getBeanAnnotaded(annotationClass, true, ignoredClass);
    }

    public static Object getBeanAnnotadedFirst(Class<? extends Annotation> annotationClass) {
        try {
            return getBeanAnnotaded(annotationClass, true, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getBeanAnnotadedFirst(Class<? extends Annotation> annotationClass,
            Collection<Class> ignoredClass) {
        try {
            return getBeanAnnotaded(annotationClass, true, ignoredClass).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * * Busca una implementacion de una anotacion
     * 
     * @param annotationClass
     * @param cache           si es verdadero toma los objetos instanciados
     *                        previamente y no los vuelve a instanciar
     * @return Lista de los ojetos instanciados de las implementacione sencontradas
     */
    public static List getBeanAnnotaded(Class<? extends Annotation> annotationClass, boolean cache,
            Collection<Class> ignoredClass) {
        if (instances.containsKey(annotationClass) && cache) {
            return instances.get(annotationClass);
        } else {
            List<Object> returnValue = new ArrayList<>();
            Reflections reflections = new Reflections(PREFIX, Scanners.TypesAnnotated);
            Set<Class<?>> clasesAnotadas = reflections.getTypesAnnotatedWith(annotationClass);
            // Imprimimos las clases encontradas
            for (Class<?> clase : clasesAnotadas) {
                if (ignoredClass == null || !ignoredClass.contains(clase)) {
                    // Service anotacion = clase.getAnnotation(Service.class);
                    try {
                        returnValue.add(createNewInstance(clase));
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                            | IllegalAccessException e) {

                        e.printStackTrace();
                    }
                    // log.info("[+] Servicio cargado: [".concat(anotacion.name()));
                }
            }
            instances.put(annotationClass, returnValue);
            return returnValue;
        }
    }

    /**
     * Busca una implementacion/herencia de una interfaz/clase
     * 
     * @param interfaceClass
     * @return Lista de los ojetos instanciados de las implementacione sencontradas
     */
    public static List getBeanImplemented(Class interfaceClass) {
        return getBeanImplemented(interfaceClass, true, null);
    }

    public static List getBeanImplemented(Class interfaceClass, Collection<Class> ignoredClass) {
        return getBeanImplemented(interfaceClass, true, ignoredClass);
    }

    public static Object getBeanImplementedFirst(Class interfaceClass) {
        try {
            return getBeanImplemented(interfaceClass, true, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getBeanImplementedFirst(Class interfaceClass, Collection<Class> ignoredClass) {
        try {
            return getBeanImplemented(interfaceClass, true, ignoredClass).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * * Busca una implementacion/herencia de una interfaz/clase
     * 
     * @param interfaceClass
     * @param cache          si es verdadero toma los objetos instanciados
     *                       previamente y no los vuelve a instanciar
     * @param ignoredClass   las clases que se deben ignorar para buscar las
     *                       implementaciones
     * @return Lista de los ojetos instanciados de las implementacione sencontradas
     */
    public static List getBeanImplemented(Class interfaceClass, boolean cache, Collection<Class> ignoredClass) {
        if (instances.containsKey(interfaceClass) && cache) {
            return instances.get(interfaceClass);
        } else {
            List<Object> returnValue = new ArrayList<>();
            Reflections reflections = new Reflections(PREFIX, Scanners.SubTypes);
            Set<Class<?>> clasesAnotadas = reflections.getSubTypesOf(interfaceClass);
            for (Class<?> clase : clasesAnotadas) {
                if (ignoredClass == null || !ignoredClass.contains(clase)) {
                    // Service anotacion = clase.getAnnotation(Service.class);
                    try {
                        Object instancedObject = createNewInstance(clase);
                        returnValue.add(instancedObject);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                            | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                // log.info("[+] Servicio cargado: [".concat(anotacion.name()));
            }
            instances.put(interfaceClass, returnValue);
            return returnValue;
        }
    }

    // Método genérico para crear una instancia de cualquier clase
    private static Object createNewInstance(Class<?> clase)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // Obtener el constructor predeterminado (sin parámetros)
        Constructor<?> constructor = clase.getDeclaredConstructor();
        // Asegurarnos de que el constructor sea accesible
        constructor.setAccessible(true);
        // Crear y devolver una nueva instancia
        return constructor.newInstance();
    }

}

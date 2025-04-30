package net.qoopo.framework.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * Permite acceder y manejar las instancias de las clases que implementan una
 * interfaz o heredan de una
 * clase
 */

public class QoopoReflection {

    private static Logger log = Logger.getLogger("qoopo-reflection");

    private static Map<Class<?>, List<?>> instances = new HashMap<>();

    private static String[] PREFIX = { "net", "com", "ec", "org" };

    public static void registerPrefix(String... prefix) {
        PREFIX = prefix;
    }

    /**
     * Busca una implementacion/herencia de una interfaz/clase
     * 
     * @param interfaceClass
     * @return Lista de los ojetos instanciados de las implementaciones encontradas
     */
    public static <T> List<T> getBeansImplemented(Class<T> interfaceClass) {
        return getBeansImplemented(interfaceClass, true, null);
    }

    public static <T> List<T> getBeansImplemented(Class<T> interfaceClass,
            Collection<Class<? extends T>> ignoredClass) {
        return getBeansImplemented(interfaceClass, true, ignoredClass);
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
        if (instances.containsKey(annotationClass) && cache && instances.get(annotationClass) != null
                && !instances.get(annotationClass).isEmpty()) {
            return instances.get(annotationClass);
        } else {
            List<Object> returnValue = new ArrayList<>();
            Reflections reflections = new Reflections(PREFIX, Scanners.TypesAnnotated);
            Set<Class<?>> clasesAnotadas = reflections.getTypesAnnotatedWith(annotationClass);
            // Imprimimos las clases encontradas
            for (Class<?> clase : clasesAnotadas) {
                if (ignoredClass == null || !ignoredClass.contains(clase)) {
                    try {
                        if (!Modifier.isAbstract(clase.getModifiers())) {
                            // T instance = createNewInstance(clase);
                            // updateInstance(clase, instance);
                            Object instance = getInstance(clase);
                            if (instance != null)
                                returnValue.add(instance);
                        }
                    } catch (Exception e) {
                        log.severe("[X] Error instanciando [" + clase.getCanonicalName() + "]  abstract -> "
                                + Modifier.isAbstract(clase.getModifiers()));
                        // e.getCause().printStackTrace();
                        e.printStackTrace();
                    }
                }
            }
            instances.put(annotationClass, returnValue);
            return returnValue;
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
    public static <T> List<T> getBeansImplemented(Class<T> interfaceClass, boolean cache,
            Collection<Class<? extends T>> ignoredClass) {
        if (cache && instances.containsKey(interfaceClass) &&  instances.get(interfaceClass) != null
                && !instances.get(interfaceClass).isEmpty()) {
            return (List<T>) instances.get(interfaceClass);
        } else {
            List<T> returnValue = new ArrayList<>();
            Reflections reflections = new Reflections(PREFIX, Scanners.SubTypes);
            Set<Class<? extends T>> clasesImplementadas = reflections.getSubTypesOf(interfaceClass);
            for (Class<? extends T> clase : clasesImplementadas) {
                if (ignoredClass == null || !ignoredClass.contains(clase)) {
                    try {
                        if (!Modifier.isAbstract(clase.getModifiers())) {
                            // T instance = createNewInstance(clase);
                            // updateInstance(clase, instance);
                            T instance = getInstance(clase);
                            if (instance != null)
                                returnValue.add(instance);
                        }
                    } catch (Exception e) {
                        log.severe("[X] Error instanciando [" + clase.getCanonicalName() + "]  abstract -> "
                                + Modifier.isAbstract(clase.getModifiers()));
                        // e.getCause().printStackTrace();
                        e.printStackTrace();
                    }
                }
            }
            instances.put(interfaceClass, returnValue);
            return returnValue;
        }
    }

    private static <T> T getInstance(Class<T> clase) {
        log.info("Getting instance of -> "+ clase.getCanonicalName());
        if (instances.containsKey(clase) && instances.get(clase)!=null && !instances.get(clase).isEmpty()) {
            List<T> list = (List<T>) instances.get(clase);
            return list.get(0);
        } else {
            T instance;
            try {
                instance = createNewInstance(clase);
                updateInstance(clase, instance);
                return instance;
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.warning("No se pudo instanciar la clase [" + clase.getCanonicalName() + "]");
        return null;
    }

    /**
     * Actualiza la instancia de una clase en el mapa
     * 
     * @param clase
     * @param instance
     */
    private static void updateInstance(Class<?> clase, Object instance) {
        log.info("[+] Instanciando [" + clase.getCanonicalName() + "]");
        if (instances.containsKey(clase)) {
            List<Object> list = (List<Object>) instances.get(clase);
            list.add(instance);
            instances.put(clase, list);
        } else {
            List<Object> list = new ArrayList<>();
            list.add(instance);
            instances.put(clase, list);
        }
    }

    /**
     * Crea una instancia de una clase.
     * 
     * La clase debe tener un constructur vacio
     * 
     * @param clase
     * @param instance
     */
    // Método genérico para crear una instancia de cualquier clase
    private static <T> T createNewInstance(Class<T> clase)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Obtener el constructor predeterminado (sin parámetros)
        Constructor<?> constructor = clase.getDeclaredConstructor();
        // Asegurarnos de que el constructor sea accesible
        constructor.setAccessible(true);
        // Crear y devolver una nueva instancia
        return clase.cast(constructor.newInstance());
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

    public static <T> T getBean(Class<T> interfaceClass) {
        try {
            return interfaceClass.cast(getBeansImplemented(interfaceClass, true, null).get(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> interfaceClass, boolean cache) {
        try {
            return interfaceClass.cast(getBeansImplemented(interfaceClass, cache, null).get(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> interfaceClass, Collection<Class<? extends T>> ignoredClass) {
        try {
            return interfaceClass.cast(getBeansImplemented(interfaceClass, true, ignoredClass).get(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> interfaceClass, boolean cache, Collection<Class<? extends T>> ignoredClass) {
        try {
            return interfaceClass.cast(getBeansImplemented(interfaceClass, cache, ignoredClass).get(0));
        } catch (Exception e) {
            return null;
        }
    }

}

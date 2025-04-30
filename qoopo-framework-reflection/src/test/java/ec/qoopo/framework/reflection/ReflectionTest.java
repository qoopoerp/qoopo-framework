package ec.qoopo.framework.reflection;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import ec.qoopo.framework.reflection.testcase.annotation.Myannotation;
import ec.qoopo.framework.reflection.testcase.imp.Bird;
import ec.qoopo.framework.reflection.testcase.imp.Dolphin;
import ec.qoopo.framework.reflection.testcase.imp.Duck;
import ec.qoopo.framework.reflection.testcase.imp.Horse;
import ec.qoopo.framework.reflection.testcase.implementation.Animal;
import net.qoopo.framework.reflection.QoopoReflection;

public class ReflectionTest {

    private static Logger log = Logger.getLogger("ReflectionTest");

    @Test
    public void testImplementation() {
        try {

            List<Animal> animales = QoopoReflection.getBeansImplemented(Animal.class);
            assertTrue(animales.size() == 4);

            for (Animal animal : animales) {
                log.info("Animal specie= " + animal.getSpecie() + " class= " + animal.getClass().getName() + " Name="
                        + animal.getName());
            }

            // vuelvo a probar para ver si se mantiene la lista
            log.info("Segunda carga -->");

            animales = QoopoReflection.getBeansImplemented(Animal.class);
            assertTrue(animales.size() == 4);

            for (Animal animal : animales) {
                log.info("Animal specie= " + animal.getSpecie() + " class= " + animal.getClass().getName() + " Name="
                        + animal.getName());
            }

            // voy a buscar por cada tipo de animal

            log.info("Tercera carga -->");
            Animal horse = QoopoReflection.getBean(Horse.class);
            Animal duck = QoopoReflection.getBean(Duck.class);
            Animal dolphin = QoopoReflection.getBean(Dolphin.class);
            Animal bird = QoopoReflection.getBean(Bird.class);

            log.info("Animal specie= " + horse.getSpecie() + " class= " + horse.getClass().getName() + " Name="
                    + horse.getName());
            log.info("Animal specie= " + duck.getSpecie() + " class= " + duck.getClass().getName()
                    + " Name=" + duck.getName());
            log.info("Animal specie= " + dolphin.getSpecie() + " class= " + dolphin.getClass().getName()
                    + " Name=" + dolphin.getName());
            log.info("Animal specie= " + bird.getSpecie() + " class= " + bird.getClass().getName()
                    + " Name=" + bird.getName());

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testAnnotation() {
        try {

            log.info("Pruebas de anotaciones");

            List<Animal> animales = QoopoReflection.getBeanAnnotaded(Myannotation.class);
            assertTrue(animales.size() == 2);

            for (Animal animal : animales) {
                log.info("Animal specie= " + animal.getSpecie() + " class= " + animal.getClass().getName() + " Name="
                        + animal.getName());
            }

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

    }
}

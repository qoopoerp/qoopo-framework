package ec.qoopo.framework.reflection.testcase.imp;

import ec.qoopo.framework.reflection.testcase.annotation.Myannotation;
import ec.qoopo.framework.reflection.testcase.implementation.Animal;
import ec.qoopo.framework.reflection.testcase.implementation.CanSwim;

@Myannotation
public class Dolphin extends Animal
        implements CanSwim {

    public Dolphin() {
        super("Dolphin " + System.currentTimeMillis());
    }

    @Override
    public String getSpecie() {
        return "Dolphin";
    }

    @Override
    public void swim() {
        System.out.println("Dolphin is swimming");
    }

}

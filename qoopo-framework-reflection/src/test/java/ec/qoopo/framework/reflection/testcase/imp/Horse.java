package ec.qoopo.framework.reflection.testcase.imp;

import ec.qoopo.framework.reflection.testcase.annotation.Myannotation;
import ec.qoopo.framework.reflection.testcase.implementation.Animal;
import ec.qoopo.framework.reflection.testcase.implementation.CanWalk;

@Myannotation
public class Horse extends Animal implements CanWalk {

    public Horse() {
        super("Horse " + System.currentTimeMillis());
    }

    @Override
    public String getSpecie() {
        return "Horse";
    }

    @Override
    public void walk() {
        System.out.println("Horse is walking");
    }

}

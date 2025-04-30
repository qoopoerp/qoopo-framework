package ec.qoopo.framework.reflection.testcase.imp;

import ec.qoopo.framework.reflection.testcase.implementation.Animal;
import ec.qoopo.framework.reflection.testcase.implementation.CanFlye;
import ec.qoopo.framework.reflection.testcase.implementation.CanSwim;
import ec.qoopo.framework.reflection.testcase.implementation.CanWalk;

public class Duck extends Animal implements CanSwim, CanWalk, CanFlye {

    public Duck() {
        super("Duck " + System.currentTimeMillis());
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }

    @Override
    public void walk() {
        System.out.println("Duck is walking");
    }

    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }

    @Override
    public String getSpecie() {
        return "Duck";
    }

}

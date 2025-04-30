package ec.qoopo.framework.reflection.testcase.imp;

import ec.qoopo.framework.reflection.testcase.implementation.Animal;
import ec.qoopo.framework.reflection.testcase.implementation.CanFlye;
import ec.qoopo.framework.reflection.testcase.implementation.CanWalk;

public class Bird extends Animal implements CanFlye, CanWalk {

    public Bird() {
        super("Bird " + System.currentTimeMillis());
    }

    @Override
    public void fly() {
        System.out.println("I am flying");
    }

    @Override
    public void walk() {
        System.out.println("I am walking");
    }

    @Override
    public String getSpecie() {
        return "Bird";
    }

}

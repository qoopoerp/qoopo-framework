package ec.qoopo.framework.reflection.testcase.implementation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Animal {

    private String name;

    public abstract String getSpecie();

}

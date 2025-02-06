package net.qoopo.framework.filter.core.condition;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ConditionCollection extends Condition {

    private List<Condition> items = new ArrayList<>();

    public ConditionCollection(String name) {
        super.name = name;
    }

    public ConditionCollection(String name, List<Condition> items) {
        super.name = name;
        this.items = items;
    }

    public static ConditionCollection of(String name, List<Condition> items) {
        return new ConditionCollection(name, items);
    }

    public void addItem(Condition item) {
        items.add(item);
    }

    public void removeItem(Condition item) {
        items.remove(item);
    }

}

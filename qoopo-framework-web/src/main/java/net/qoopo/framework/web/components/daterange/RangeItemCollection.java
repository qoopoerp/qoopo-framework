package net.qoopo.framework.web.components.daterange;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeItemCollection extends RangeItem {

    private List<RangeItem> items = new ArrayList<>();

    public RangeItemCollection(String name) {
        super.name = name;
    }

    public RangeItemCollection(String name, List<RangeItem> items) {
        super.name = name;
        this.items = items;
    }

    public static RangeItemCollection of(String name, List<RangeItem> items) {
        return new RangeItemCollection(name, items);
    }

    public void addItem(RangeItem item) {
        items.add(item);
    }

    public void removeItem(RangeItem item) {
        items.remove(item);
    }

}

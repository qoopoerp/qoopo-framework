package net.qoopo.framework.pattern.specification.testcase;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.specification.Specification;
import net.qoopo.framework.pattern.specification.testcase.model.Color;
import net.qoopo.framework.pattern.specification.testcase.model.Product;

@AllArgsConstructor
public class ColorSpecification implements Specification<Product> {

    private Color color;

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        return candidate.getColor().equals(color);
    }

}

package net.qoopo.framework.pattern.specification.testcase;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.specification.Specification;
import net.qoopo.framework.pattern.specification.testcase.model.Product;

@AllArgsConstructor
public class DescriptionSpecification implements Specification<Product> {

    private String criteria;

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        return candidate.getDescription().toLowerCase().contains(criteria.toLowerCase());
    }

}

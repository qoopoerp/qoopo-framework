package net.qoopo.framework.pattern.specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.pattern.specification.testcase.ColorSpecification;
import net.qoopo.framework.pattern.specification.testcase.DescriptionSpecification;
import net.qoopo.framework.pattern.specification.testcase.PriceSpecification;
import net.qoopo.framework.pattern.specification.testcase.model.Color;
import net.qoopo.framework.pattern.specification.testcase.model.Product;
import net.qoopo.framework.pattern.specification.testcase.model.Size;

public class SpecificationTest {

        private static Logger log = Logger.getLogger("security-test");

        private Product[] data = {
                        Product.builder().name("Mouse").description("Mouse Genius Gamer").color(Color.RED)
                                        .size(Size.SMALL)
                                        .price(15.0).build(),
                        Product.builder().name("Teclado").description("Teclado Genius").color(Color.BLACK)
                                        .size(Size.MEDIUM)
                                        .price(45.0).build(),
                        Product.builder().name("Escritorio").description("Escritorio Gamer").color(Color.GREEN)
                                        .size(Size.EXTRA_LARGE).price(150.0).build(),
                        Product.builder().name("Monitor").description("Monitor Gamer AOC").color(Color.RED)
                                        .size(Size.EXTRA_LARGE)
                                        .price(100.0).build(),
                        Product.builder().name("Cargador").description("Cargador wireless").color(Color.GREEN)
                                        .size(Size.SMALL)
                                        .price(25.0).build()
        };

        @Test
        public void testSpecification() {
                try {

                        DescriptionSpecification descriptionSpecification = new DescriptionSpecification("Gamer");
                        PriceSpecification priceSpecification = new PriceSpecification(40.0);
                        ColorSpecification colorSpecification = new ColorSpecification(Color.GREEN);

                        AndSpecification<Product> nameAndPrice = new AndSpecification<>(descriptionSpecification,
                                        priceSpecification);

                        log.info("Description");
                        List<Product> result = Stream.of(data).filter(c -> descriptionSpecification.isSatisfiedBy(c))
                                        .collect(Collectors.toList());
                        result.forEach(c -> log.info(c.toString()));
                        assertEquals(3, result.size());

                        log.info("Price");
                        result = Stream.of(data).filter(c -> priceSpecification.isSatisfiedBy(c))
                                        .collect(Collectors.toList());
                        result.forEach(c -> log.info(c.toString()));
                        assertEquals(3, result.size());

                        log.info("Color");
                        result = Stream.of(data).filter(c -> colorSpecification.isSatisfiedBy(c))
                                        .collect(Collectors.toList());
                        result.forEach(c -> log.info(c.toString()));
                        assertEquals(2, result.size());

                        log.info("Description and Price");

                        result = Stream.of(data).filter(c -> nameAndPrice.isSatisfiedBy(c))
                                        .collect(Collectors.toList());
                        result.forEach(c -> log.info(c.toString()));
                        assertEquals(2, result.size());

                        log.info("Combined (Description, Price, Color)");

                        ICompositeSpecification<Product> specification = new WrapperSpecification<>(
                                        descriptionSpecification)
                                        .and(priceSpecification)
                                        .and(colorSpecification);

                        result = Stream.of(data).filter(c -> specification.isSatisfiedBy(c))
                                        .collect(Collectors.toList());
                        result.forEach(c -> log.info(c.toString()));
                        assertEquals(1, result.size());

                } catch (Exception ex) {
                        ex.printStackTrace();
                        assertTrue(false);
                }
        }

}

package net.qoopo.framework.commons.eventbus;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.strings.HtmlUtil;

public class StringsTest {

        private static Logger log = Logger.getLogger("strings-test");

        @Test
        public void testHtml() {
                try {
                        String input = "ALBERTO PAÚL GARCÍA PATIÑO";
                        String outputExpected = "ALBERTO PA&Uacute;L GARC&Iacute;A PATI&Ntilde;O";
                        String output = HtmlUtil.prepareHtml(input);
                        log.info("output=" + output);
                        assertTrue(outputExpected.equals(output));
                } catch (Exception ex) {
                        ex.printStackTrace();
                        assertTrue(false);
                }
        }

}

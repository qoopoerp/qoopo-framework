package net.qoopo.qoopo.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.reflection.QoopoReflection;
import net.qoopo.framework.security.authentication.repository.UserRepository;

public class LoadRepositoryTest {

    private static Logger log = Logger.getLogger("security-test");

    @Test
    public void testRepository() {
        try {
            List<UserRepository> userRepositoryList = QoopoReflection
                    .getBeansImplemented(UserRepository.class);

            assertTrue(userRepositoryList != null && !userRepositoryList.isEmpty());
            log.info("repositorios cargados->" + userRepositoryList.size());
            assertEquals(2, userRepositoryList.size());

            for (UserRepository userRepository : userRepositoryList) {
                log.info("cargado? " + (userRepository != null) + " -  "
                        + (userRepository != null ? userRepository.getClass().getCanonicalName() : ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

}

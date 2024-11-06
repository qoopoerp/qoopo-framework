package net.qoopo.framework.security.authentication.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qoopo.framework.security.authentication.service.UserNotFoundException;
import net.qoopo.framework.security.authentication.user.UserData;

public class InMemoryUserRepository implements UserRepository {

    private Map<String, UserData> users;

    public InMemoryUserRepository() {
        this(Collections.EMPTY_LIST);
    }

    public InMemoryUserRepository(UserData... users) {
        this(Arrays.asList(users));
    }

    public InMemoryUserRepository(List<UserData> users) {
        this.users = new HashMap<>();
        for (UserData data : users) {
            this.users.put(data.getUser(), data);
        }
    }

    @Override
    public UserData findUserDataByUserName(String user) throws UserNotFoundException {
        if (users.containsKey(user)) {
            return users.get(user);
        } else {
            throw new UserNotFoundException();
        }
    }

}

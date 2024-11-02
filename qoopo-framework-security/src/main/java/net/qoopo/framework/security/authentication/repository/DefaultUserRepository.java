package net.qoopo.framework.security.authentication.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qoopo.framework.security.authentication.service.UserNotFoundException;
import net.qoopo.framework.security.authentication.user.UserData;

public class DefaultUserRepository implements UserRepository {

    private Map<String, UserData> users;

    public DefaultUserRepository(List<UserData> listUsers) {
        users = new HashMap<>();
        for (UserData data : listUsers) {
            users.put(data.getUser(), data);
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

package net.qoopo.framework.security.authentication.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserData implements UserData {
    private String user;
    private String encodedPassword;
}

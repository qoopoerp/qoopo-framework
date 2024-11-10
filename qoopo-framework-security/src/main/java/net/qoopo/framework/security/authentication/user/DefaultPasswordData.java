package net.qoopo.framework.security.authentication.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.qoopo.framework.security.authentication.password.PasswordData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultPasswordData implements PasswordData {
    private String encodedPassword;
}

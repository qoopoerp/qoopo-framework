package net.qoopo.framework.security.authentication.user;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.qoopo.framework.security.permission.GrantedPermission;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserData implements UserData {
    private String user;
    private String encodedPassword;
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean credentiaslExpired = false;
    @Builder.Default
    private boolean accountLocked = false;
    @Builder.Default
    private boolean enabled = true;

    private Object data;
    private Collection<GrantedPermission> permissions;
}

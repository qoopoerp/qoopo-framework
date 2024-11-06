package net.qoopo.framework.security.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultGrantedPermission implements GrantedPermission {
    private String permission;
    private Object data;

}

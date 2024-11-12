package net.qoopo.framework.security.core.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Builder.Default
    private Object data=null;

    public static GrantedPermission of(String permission) {
        return DefaultGrantedPermission.builder().permission(permission).build();
    }

    public static GrantedPermission of(String permission, Object data) {
        return DefaultGrantedPermission.builder().permission(permission).data(data).build();
    }

    public static List<GrantedPermission> of(String... permissions) {
        return DefaultGrantedPermission.of(Arrays.asList(permissions));
    }

    public static List<GrantedPermission> of(List<String> permissions) {
        List<GrantedPermission> returnList = new ArrayList<>();
        permissions.forEach(c -> returnList.add(DefaultGrantedPermission.of(c)));
        return returnList;
    }

}

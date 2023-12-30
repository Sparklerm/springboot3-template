package ${package}.config.security.component;

import ${package}.common.enums.YesNoEnum;
import ${package}.model.po.PermissionPO;
import ${package}.model.po.UserPO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author alex meng
 * @createDate 2023-12-23 08:54
 */
@Data
public class SecurityUserDetails implements UserDetails {

    private UserPO user;

    private List<PermissionPO> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getId() + ":" + permission.getName()))
                .collect(Collectors.toList());
    }

    public SecurityUserDetails(UserPO user, List<PermissionPO> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(user.getStatus(), YesNoEnum.YES.getKey());
    }
}

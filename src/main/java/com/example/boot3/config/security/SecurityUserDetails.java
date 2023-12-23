package com.example.boot3.config.security;

import com.example.boot3.common.enums.YesNoEnum;
import com.example.boot3.model.po.AdminUserPO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 08:54
 */
@Data
public class SecurityUserDetails implements UserDetails {

    private AdminUserPO user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> permissions = List.of("fun1");
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public SecurityUserDetails(AdminUserPO user) {
        this.user = user;
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

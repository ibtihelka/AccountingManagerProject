package com.accounting_manager.accounting_auth.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    CLIENT(Collections.emptySet()),
    ACCOUNTANT(
            Set.of(
                    Permission.ACCOUNTANT_READ,
                    Permission.ACCOUNTANT_UPDATE,
                    Permission.ACCOUNTANT_DELETE,
                    Permission.ACCOUNTANT_CREATE
            )
    ),
    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_CREATE,
                    Permission.ACCOUNTANT_READ,
                    Permission.ACCOUNTANT_UPDATE,
                    Permission.ACCOUNTANT_DELETE,
                    Permission.ACCOUNTANT_CREATE
            )
    );


    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
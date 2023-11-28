package br.com.contact.api.entity.model.response;

import br.com.contact.api.entity.Permission;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Long id;

    private String fullname;

    private String username;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    private Set<PermissionResponse> permissions;

    private Set<AddressResponse> addresses = new HashSet<>();

    private ContactInfoResponse contactInfo;
}

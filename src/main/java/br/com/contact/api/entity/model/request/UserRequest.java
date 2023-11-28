package br.com.contact.api.entity.model.request;

import br.com.contact.api.entity.model.response.AddressResponse;
import br.com.contact.api.entity.model.response.ContactInfoResponse;
import br.com.contact.api.entity.model.response.PermissionResponse;
import jakarta.validation.constraints.NotEmpty;
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
public class UserRequest {

    @NotEmpty(message = "The Full Name is required.")
    private String fullname;

    @NotEmpty(message = "The Username is required.")
    private String username;

    @NotEmpty(message = "The password is required.")
    private String password;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    private Set<PermissionResponse> permissions;

    private Set<AddressResponse> addresses = new HashSet<>();

    private ContactInfoResponse contactInfo;
}

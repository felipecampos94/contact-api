package br.com.contact.api.entity.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRequest {

    private String street;

    private String city;

    private String state;

    private Integer zip;
}

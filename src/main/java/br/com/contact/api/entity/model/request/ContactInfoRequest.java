package br.com.contact.api.entity.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactInfoRequest {

    private AddressRequest homeAddress;

    private PhoneRequest homePhone;
}

package br.com.contact.api.entity.model.response;

import br.com.contact.api.entity.Address;
import br.com.contact.api.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactInfoResponse {

    private AddressResponse homeAddress;

    private PhoneResponse homePhone;
}

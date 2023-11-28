package br.com.contact.api.entity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyResponse implements Serializable {

    private Long id;

    private String name;

    private String cnpj;

    private AddressResponse address;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<CompanyResponse> companyBranches;
}
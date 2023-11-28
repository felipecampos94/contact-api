package br.com.contact.api.entity.model.request;

import br.com.contact.api.entity.Company;
import br.com.contact.api.entity.model.response.AddressResponse;
import br.com.contact.api.entity.model.response.CompanyResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyRequest implements Serializable {

    @NotEmpty(message = "The Name is required.")
    private String name;

    @CNPJ(message = "The CPNJ invalid.")
    @NotEmpty(message = "The CNPJ is required.")
    private String cnpj;

    private AddressResponse address;

    private Set<CompanyResponse> companyBranches;
}
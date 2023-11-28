package br.com.contact.api.service;

import br.com.contact.api.entity.Address;
import br.com.contact.api.entity.Company;
import br.com.contact.api.entity.model.request.CompanyRequest;
import br.com.contact.api.entity.model.response.AddressResponse;
import br.com.contact.api.entity.model.response.CompanyResponse;
import br.com.contact.api.repository.CompanyRepository;
import br.com.contact.api.service.exceptions.DataIntegrityViolationException;
import br.com.contact.api.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    // Exception
    private static final String OBJECT_NOT_FOUND = "Object Not Found!";
    private static final String CNPJ_ALREADY_EXIST = "CNPJ already exist.";

    // Address
    private static final String STREET = "123 Main St";
    private static final String CITY = "São Paulo";
    private static final String STATE = "SP";
    private static final int ZIP = 12345;

    // Company
    private static final Long COMPANY_ID = 1L;
    private static final String NAME = "Company";
    private static final String CNPJ = "33333749000100";

    // Company Branch
    private static final Long COMPANY_BRANCH_ID = 2L;
    private static final String COMPANY_BRANCH_NAME = "Company Branch";
    private static final String COMPANY_BRANCH_CNPJ = "14171534000195";

    @InjectMocks
    private CompanyService companyService;
    @Mock
    private CompanyRepository companyRepository;

    private CompanyRequest companyRequest;
    private Company company;
    private Optional<Company> optionalCompany;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.startCompany();
        this.startCompanyRequest();
    }

    @Test
    @DisplayName("When the list of companies is returned successfully")
    void findAllCompanies() {
        when(this.companyRepository.findAll()).thenReturn(List.of(company));

        List<CompanyResponse> response = this.companyService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(CompanyResponse.class, response.get(0).getClass());
        assertEquals(COMPANY_ID, response.get(0).getId());
    }

    @Test
    @DisplayName("When the list of companies distinct is returned successfully")
    void findAllDistinctId() {
        when(this.companyRepository.findAllDistinctCompaniesWithBranches()).thenReturn(List.of(company));

        List<CompanyResponse> response = this.companyService.findAllDistinctId();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(CompanyResponse.class, response.get(0).getClass());
        assertEquals(COMPANY_ID, response.get(0).getId());
    }

    @Test
    @DisplayName("When the search for id is returned successfully")
    void findByIdCompanyThenReturnSuccess() {

        when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        CompanyResponse response = this.companyService.findById(1L);

        assertNotNull(response);
        assertNotNull(response.getAddress());
        assertNotNull(response.getCompanyBranches());
        assertEquals(CompanyResponse.class, response.getClass());
        assertEquals(COMPANY_ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(CNPJ, response.getCnpj());
    }

    @Test
    @DisplayName("When the search for id returns ObjectNotFoundException")
    void findByIdThenReturnObjectNotFoundException() {
        when(this.companyRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            this.companyService.findById(COMPANY_ID);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the company is created successfully")
    void createCompanyThenReturnSuccess() {
        when(this.companyRepository.save(any())).thenReturn(company);
        when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        CompanyResponse response = this.companyService.create(companyRequest);

        assertNotNull(response);
        assertNotNull(response.getAddress());
        assertNotNull(response.getCompanyBranches());
        assertEquals(CompanyResponse.class, response.getClass());
        assertEquals(COMPANY_ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(CNPJ, response.getCnpj());
    }

    @Test
    @DisplayName("When the company is created DataIntegrityViolationException")
    void createCompanyThenReturnDataIntegrityViolationException() {
        when(this.companyRepository.findByCnpj(anyString())).thenReturn(Optional.of(company));

        try {
            Optional.of(company).get().setId(2L);
            this.companyService.create(companyRequest);
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals(CNPJ_ALREADY_EXIST, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the company is updated successfully")
    void updateCompanyThenReturnSuccess() {
        when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        when(this.companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyResponse response = this.companyService.update(COMPANY_ID, companyRequest);

        assertNotNull(response);
        assertNotNull(response.getAddress());
        assertNotNull(response.getCompanyBranches());
        assertEquals(CompanyResponse.class, response.getClass());
        assertEquals(COMPANY_ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(CNPJ, response.getCnpj());
    }

    @Test
    @DisplayName("When the company is updated DataIntegrityViolationException")
    void updateCompanyThenReturnDataIntegrityViolationException() {
        when(this.companyRepository.findByCnpj(anyString())).thenReturn(Optional.of(company));

        try {
            Optional.of(company).get().setId(2L);
            this.companyService.update(COMPANY_ID, companyRequest);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND + " Id: " + COMPANY_ID + " Type " + Company.class.getSimpleName(), e.getMessage());
        }
    }


    @Test
    @DisplayName("When the company is deleted successfully")
    void deleteCompanySuccess() {
        when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        doNothing().when(this.companyRepository).deleteById(anyLong());
        this.companyService.delete(COMPANY_ID);
        verify(this.companyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("When the company is not deleted returns ObjectNotFoundException")
    void deleteCompanyWithObjectNotFoundException() {
        when(this.companyRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            this.companyService.delete(COMPANY_ID);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    private void startCompany() {
        // Address instantiation
        Address address = new Address(STREET, CITY, STATE, ZIP);
        // Company Branch instantiation
        Company companyBranch = new Company();
        companyBranch.setId(COMPANY_BRANCH_ID);
        companyBranch.setName(COMPANY_BRANCH_NAME);
        companyBranch.setCnpj(COMPANY_BRANCH_CNPJ);
        companyBranch.setAddress(address);

        // Company instantiation
        company = new Company();
        company.setId(COMPANY_ID);
        company.setName(NAME);
        company.setCnpj(CNPJ);
        company.setAddress(address);
        company.setCompanyBranches(Set.of(companyBranch));
    }

    private void startCompanyRequest() {
        // Address instantiation
        AddressResponse address = new AddressResponse(STREET, CITY, STATE, ZIP);
        // Company Branch instantiation
        CompanyResponse companyBranch = new CompanyResponse();
        companyBranch.setId(COMPANY_BRANCH_ID);
        companyBranch.setName(COMPANY_BRANCH_NAME);
        companyBranch.setCnpj(COMPANY_BRANCH_CNPJ);
        companyBranch.setAddress(address);

        // Company instantiation
        companyRequest = new CompanyRequest();
        companyRequest.setName(NAME);
        companyRequest.setCnpj(CNPJ);
        companyRequest.setAddress(address);
        companyRequest.setCompanyBranches(Set.of(companyBranch));
    }
}
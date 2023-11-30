package br.com.contact.api.controller;

import br.com.contact.api.entity.model.request.CompanyRequest;
import br.com.contact.api.entity.model.response.AddressResponse;
import br.com.contact.api.entity.model.response.CompanyResponse;
import br.com.contact.api.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class CompanyControllerTest {
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
    private CompanyController companyController;
    @Mock
    private CompanyService companyService;

    @Mock
    private ModelMapper modelMapper;

    private CompanyRequest companyRequest;
    private CompanyResponse companyResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.startCompanyResponse();
        this.startCompanyRequest();
    }

    @Test
    @DisplayName("When the list of companies is returned successfully")
    void findAllCompanies() {
        when(this.companyService.findAll()).thenReturn(List.of(companyResponse));
        when(modelMapper.map(any(), any())).thenReturn(this.companyResponse);
        ResponseEntity<List<CompanyResponse>> response = this.companyController.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CompanyResponse.class, response.getBody().get(0).getClass());

        assertEquals(COMPANY_ID, response.getBody().get(0).getId());
    }


    @Test
    @DisplayName("When the list of companies distinct is returned successfully")
    void findAllDistinctId() {
        when(this.companyService.findAllDistinctId()).thenReturn(List.of(companyResponse));
        when(modelMapper.map(any(), any())).thenReturn(this.companyResponse);
        ResponseEntity<List<CompanyResponse>> response = this.companyController.findAllDistinctId();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CompanyResponse.class, response.getBody().get(0).getClass());

        assertEquals(COMPANY_ID, response.getBody().get(0).getId());
    }

    @Test
    @DisplayName("When the search for id is returned successfully")
    void findByIdCompanyThenReturnSuccess() {
        when(this.companyService.findById(anyLong())).thenReturn(companyResponse);
        when(modelMapper.map(any(), any())).thenReturn(companyResponse);

        ResponseEntity<CompanyResponse> response = this.companyController.findById(COMPANY_ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CompanyResponse.class, response.getBody().getClass());
        assertEquals(COMPANY_ID, response.getBody().getId());
    }

    @Test
    @DisplayName("When the company is created successfully")
    void createCompanyThenReturnSuccess() {
        when(this.companyService.create(any())).thenReturn(companyResponse);

        ResponseEntity<CompanyResponse> response = this.companyController.create(companyRequest);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("When the company is updated successfully")
    void updateCompanyThenReturnSuccess() {
        when(this.companyService.update(COMPANY_ID, companyRequest)).thenReturn(companyResponse);
        when(modelMapper.map(any(), any())).thenReturn(companyResponse);

        ResponseEntity<CompanyResponse> response = this.companyController.update(COMPANY_ID, companyRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CompanyResponse.class, response.getBody().getClass());

        assertEquals(COMPANY_ID, response.getBody().getId());
    }

    @Test
    @DisplayName("When the company is deleted successfully")
    void deleteCompanySuccess() {
        doNothing().when(this.companyService).delete(anyLong());

        ResponseEntity<Void> response = this.companyController.delete(COMPANY_ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        verify(this.companyService, times(1)).delete(anyLong());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private void startCompanyResponse() {
        // Address instantiation
        AddressResponse address = new AddressResponse(STREET, CITY, STATE, ZIP);
        // Company Branch instantiation
        CompanyResponse companyBranch = new CompanyResponse();
        companyBranch.setId(COMPANY_BRANCH_ID);
        companyBranch.setName(COMPANY_BRANCH_NAME);
        companyBranch.setCnpj(COMPANY_BRANCH_CNPJ);
        companyBranch.setAddress(address);

        // Company instantiation
        companyResponse = new CompanyResponse();
        companyResponse.setId(COMPANY_ID);
        companyResponse.setName(NAME);
        companyResponse.setCnpj(CNPJ);
        companyResponse.setAddress(address);
        companyResponse.setCompanyBranches(Set.of(companyBranch));
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
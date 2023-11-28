package br.com.contact.api.service;

import br.com.contact.api.entity.Company;
import br.com.contact.api.entity.model.request.CompanyRequest;
import br.com.contact.api.entity.model.response.CompanyResponse;
import br.com.contact.api.mapper.ModelMapperConfig;
import br.com.contact.api.repository.CompanyRepository;
import br.com.contact.api.service.exceptions.DataIntegrityViolationException;
import br.com.contact.api.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Logger logger = Logger.getLogger(CompanyService.class.getName());

    private final CompanyRepository companyRepository;


    public List<CompanyResponse> findAll() {
        logger.info("Finding all companies!");
        return this.companyRepository.findAll()
                .stream()
                .map(company -> ModelMapperConfig.parseObject(company, CompanyResponse.class))
                .toList();
    }

    public Page<CompanyResponse> findAllPageable(PageRequest pageable) {
        logger.info("Finding all companies pageable!");
        var companyPage = this.companyRepository.findAll(pageable);
        return companyPage.map(company -> ModelMapperConfig.parseObject(companyPage, CompanyResponse.class));
    }

    public List<CompanyResponse> findAllDistinctId() {
        logger.info("Finding all companies distinct!");
        return this.companyRepository.findAllDistinctCompaniesWithBranches()
                .stream()
                .map(company -> ModelMapperConfig.parseObject(company, CompanyResponse.class))
                .toList();
    }

    public CompanyResponse findById(Long id) {
        logger.info("Finding one company!");
        var companyResponse = this.companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object Not Found! Id: %s Type %s"
                        .formatted(id, Company.class.getSimpleName())));

        return ModelMapperConfig.parseObject(companyResponse, CompanyResponse.class);
    }


    public CompanyResponse create(CompanyRequest companyRequest) {
        logger.info("Creating one company!");
        var companyResponse = ModelMapperConfig.parseObject(companyRequest, CompanyResponse.class);
        this.verifyCnpjExists(companyResponse.getCnpj());
        this.verifyForeignKey(companyResponse);
        companyResponse.setCompanyBranches(companyResponse.getCompanyBranches());
        var companySaved = this.companyRepository.save(ModelMapperConfig.parseObject(companyResponse, Company.class));
        return ModelMapperConfig.parseObject(companySaved, CompanyResponse.class);
    }

    public CompanyResponse update(Long id, CompanyRequest companyRequest) {
        logger.info("Updating one company!");
        var companyResponse = this.findById(id);
        var companyData = ModelMapperConfig.parseObject(companyRequest, CompanyResponse.class);

        this.updateData(companyResponse, companyData);

        this.verifyCnpjUpdate(companyResponse, companyData);
        this.verifyForeignKey(companyResponse);
        companyResponse.setCompanyBranches(companyResponse.getCompanyBranches());

        var company = this.companyRepository.save(ModelMapperConfig.parseObject(companyResponse, Company.class));
        return ModelMapperConfig.parseObject(company, CompanyResponse.class);
    }

    private void updateData(CompanyResponse response, CompanyResponse request) {
        response.setName(request.getName());
        response.setCnpj(request.getCnpj());
        response.setAddress(request.getAddress());
        response.setCompanyBranches(request.getCompanyBranches());
    }

    public void delete(Long id) {
        logger.info("Deleting one company!");
        var companyResponse = this.findById(id);
        this.companyRepository.deleteById(companyResponse.getId());
    }

    private void verifyCnpjExists(String cnpj) {
        this.companyRepository.findByCnpj(cnpj).ifPresent(company -> {
            throw new DataIntegrityViolationException("CNPJ already exist.");
        });
    }

    private void verifyCnpjUpdate(CompanyResponse request, CompanyResponse response) {
        this.companyRepository.findByCnpj(response.getCnpj()).ifPresent(cnpjVerify -> {
            if (!cnpjVerify.getCnpj().equals(request.getCnpj())) {
                throw new DataIntegrityViolationException("CNPJ already exist.");
            }
        });
    }

    private void verifyForeignKey(CompanyResponse companyResponse) {
        var companyBranches = companyResponse.getCompanyBranches();
        if (!companyBranches.isEmpty()) {
            var setCompanyBranches = companyBranches.stream()
                    .map(companyBranch -> this.findById(companyBranch.getId()))
                    .collect(Collectors.toSet());
            companyResponse.setCompanyBranches(setCompanyBranches);
        }
    }


}

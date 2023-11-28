package br.com.contact.api.repository;

import br.com.contact.api.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCnpj(String cnpj);

    @Query("SELECT c FROM Company c WHERE c.id NOT IN (SELECT DISTINCT cb.id FROM Company c JOIN c.companyBranches cb)")
    List<Company> findAllDistinctCompaniesWithBranches();
}

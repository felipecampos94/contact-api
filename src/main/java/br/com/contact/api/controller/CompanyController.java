package br.com.contact.api.controller;

import br.com.contact.api.entity.model.request.CompanyRequest;
import br.com.contact.api.entity.model.response.CompanyResponse;
import br.com.contact.api.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CompanyResponse>> findAll() {
        return ResponseEntity.ok().body(this.companyService.findAll());
    }

    @GetMapping("/pageable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<CompanyResponse>> findAllPageable(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok().body(this.companyService.findAllPageable(pageable));
    }

    @GetMapping("/distinct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CompanyResponse>> findAllDistinctId() {
        return ResponseEntity.ok().body(this.companyService.findAllDistinctId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.companyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.create(companyRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@PathVariable Long id, @Valid @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.ok().body(this.companyService.update(id, companyRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
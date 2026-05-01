package com.example.testProject.controller;

import com.example.testProject.dto.business.BusinessRequestDTO;
import com.example.testProject.dto.business.BusinessResponseDTO;
import com.example.testProject.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessCrudController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessResponseDTO> create(@RequestBody BusinessRequestDTO dto) {
        return ResponseEntity.ok(businessService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BusinessResponseDTO>> getAll() {
        return ResponseEntity.ok(businessService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDTO> update(@PathVariable Long id, @RequestBody BusinessRequestDTO dto) {
        return ResponseEntity.ok(businessService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        businessService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


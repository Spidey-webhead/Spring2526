package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.VehicleCategoryConfig;
import com.umcsuser.carrent.repositories.VehicleCategoryRepository;import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VehicleCategoryService {
    private final VehicleCategoryRepository configRepository;
    public VehicleCategoryService(VehicleCategoryRepository configRepository){
        this.configRepository = configRepository;
    }

    public VehicleCategoryConfig getByCategory(String category) {
        return configRepository.findByCategory(category)
                .orElseThrow(() -> new IllegalArgumentException("Nieznana kategoria pojazdu "+ category));
    }

    public boolean categoryExists(String category) {
        return configRepository.findByCategory(category).isPresent();
    }

    public List<VehicleCategoryConfig> findAllCategories() {
        return configRepository.findAll();
    }
}

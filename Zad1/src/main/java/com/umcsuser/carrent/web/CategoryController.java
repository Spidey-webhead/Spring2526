package com.umcsuser.carrent.web;

import com.umcsuser.carrent.models.VehicleCategoryConfig;
import com.umcsuser.carrent.services.impl.VehicleCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final VehicleCategoryService categoryService;

    public CategoryController(VehicleCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<VehicleCategoryConfig> list() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/{category}")
    public VehicleCategoryConfig get(@PathVariable String category) {
        return categoryService.getByCategory(category);
    }
}
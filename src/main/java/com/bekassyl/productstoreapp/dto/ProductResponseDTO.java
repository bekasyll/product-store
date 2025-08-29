package com.bekassyl.productstoreapp.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is required!")
    @Size(max = 100, message = "Name ≤ 100 chars")
    private String name;

    @NotBlank(message = "Brand is required!")
    @Size(max = 100, message = "Brand ≤ 100 chars")
    private String brand;

    @NotBlank(message = "Category is required!")
    @Size(max = 100, message = "Category ≤ 100 chars")
    private String category;

    @NotNull(message = "Price is required!")
    @Max(value = 100000000, message = "Price ≤ 100000000$")
    @Min(value = 0, message = "Price ≥ 0$")
    private BigDecimal price;

    @NotBlank(message = "Description is required!")
    @Size(min = 10, message = "Description ≥ 10")
    @Size(max = 2000, message = "Description ≤ 2000")
    private String description;

    @NotNull(message = "Image is required!")
    private String imageFileName;

    public ProductResponseDTO() {
    }
}


package com.bekassyl.productstoreapp.controller;

import com.bekassyl.productstoreapp.dto.ProductRequestDTO;
import com.bekassyl.productstoreapp.dto.ProductResponseDTO;
import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.models.Product;
import com.bekassyl.productstoreapp.services.CustomerService;
import com.bekassyl.productstoreapp.services.FileStorageService;
import com.bekassyl.productstoreapp.services.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper, CustomerService customerService, FileStorageService fileStorageService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
        this.fileStorageService = fileStorageService;
    }

    private Product convertToProduct(ProductRequestDTO productRequestDTO) {
        return modelMapper.map(productRequestDTO, Product.class);
    }

    private ProductResponseDTO convertToProductResponseDTO(Product product) {
        return modelMapper.map(product, ProductResponseDTO.class);
    }


    @GetMapping()
    public String showProducts(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ProductResponseDTO> productsPage = productService.getPage(page).map(this::convertToProductResponseDTO);

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.getCustomerByUsername(username).get();

        if (customer.getRole().equals("ROLE_ADMIN") || customer.getRole().equals("ROLE_SUPERVISOR")) {
            model.addAttribute("role", true);
        }

        return "products/productsList";
    }

    @GetMapping("/{id}")
    public String showProductById(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", convertToProductResponseDTO(productService.getProductById(id)));

        return "products/aboutProduct";
    }

    @GetMapping("/new")
    public String showCreatePage(Model model) {
        model.addAttribute("product", new ProductRequestDTO());

        return "products/newProduct";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") ProductRequestDTO productRequestDTO, BindingResult bindingResult) {
        if (productRequestDTO.getImageFileName().isEmpty()) {
            bindingResult.addError(new FieldError("productRequestDTO", "imageFileName", "Image is required!"));
        }
        if (bindingResult.hasErrors()) {
            return "products/newProduct";
        } else {
            // Save image
            String storageFileName = fileStorageService.saveProductImage(productRequestDTO);

            Product product = convertToProduct(productRequestDTO);
            product.setImageFileName(storageFileName);
            productService.saveProduct(product);

            return "redirect:/products";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);

        model.addAttribute("product", product);
        model.addAttribute("productDTO", convertToProductResponseDTO(product));


        return "products/editProduct";
    }

    @PutMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") int id, @Valid @ModelAttribute("product") ProductRequestDTO productRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "products/editProduct";
        }
        Product productToBeUpdated = productService.getProductById(id);

        Product updatedProduct = convertToProduct(productRequestDTO);
        updatedProduct.setId(productToBeUpdated.getId());
        updatedProduct.setCreatedAt(productToBeUpdated.getCreatedAt());
        updatedProduct.setImageFileName(productToBeUpdated.getImageFileName());

        if (!productRequestDTO.getImageFileName().isEmpty()) {
            // Delete old product image
            fileStorageService.deleteProductImage(id);

            // Save image
            String storageFileName = fileStorageService.saveProductImage(productRequestDTO);
            updatedProduct.setImageFileName(storageFileName);
        }
        productService.editProduct(updatedProduct);

        return "redirect:/products";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") int id) {
        // Delete product image
        fileStorageService.deleteProductImage(id);

        productService.deleteProduct(id);

        return "redirect:/products";
    }
}

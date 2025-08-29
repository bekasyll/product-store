package com.bekassyl.productstoreapp.controller;

import com.bekassyl.productstoreapp.dto.CustomerDTO;
import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.services.CustomerService;
import com.bekassyl.productstoreapp.validation.CustomerEmailValidator;
import com.bekassyl.productstoreapp.validation.CustomerUsernameValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final CustomerUsernameValidator customerUsernameValidator;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final CustomerEmailValidator customerEmailValidator;

    @Autowired
    public AuthController(CustomerUsernameValidator customerUsernameValidator, ModelMapper modelMapper, CustomerService customerService, CustomerEmailValidator customerEmailValidator) {
        this.customerUsernameValidator = customerUsernameValidator;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
        this.customerEmailValidator = customerEmailValidator;
    }

    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("customer") CustomerDTO customerDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("customer") @Valid CustomerDTO customerDTO, BindingResult bindingResult) {
        Customer customer = (convertToCustomer(customerDTO));

        customerUsernameValidator.validate(customer, bindingResult);
        customerEmailValidator.validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        } else {
            customerService.saveCustomer(customer);
            return "redirect:/auth/login";
        }
    }
}

package com.bekassyl.productstoreapp.validation;


import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerUsernameValidator implements Validator {
    private final CustomerService customerService;

    @Autowired
    public CustomerUsernameValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;

        if (customerService.getCustomerByUsername(customer.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already taken!");
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }
}

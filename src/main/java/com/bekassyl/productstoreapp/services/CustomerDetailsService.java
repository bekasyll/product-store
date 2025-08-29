package com.bekassyl.productstoreapp.services;

import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.security.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerDetailsService implements UserDetailsService {
    private final CustomerService customerService;

    @Autowired
    public CustomerDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByUsername(username);

        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new CustomerDetails(customer.get());
    }
}

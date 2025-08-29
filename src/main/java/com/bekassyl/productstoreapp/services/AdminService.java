package com.bekassyl.productstoreapp.services;

import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {
    private final CustomerRepository customerRepository;

    @Autowired
    public AdminService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getPage(int page) {
        return customerRepository.findAll(PageRequest.of(page - 1, 10, Sort.by("id")));
    }

    @Transactional
    public void changeRole(int id) {
        Customer customer = customerRepository.findById(id).get();

        if (customer.getRole().equals("ROLE_USER")) {
            customer.setRole("ROLE_ADMIN");
        } else {
            customer.setRole("ROLE_USER");
        }

        customerRepository.save(customer);
    }

    @Transactional
    public void deleteAccount(int id) {
        customerRepository.deleteById(id);
    }
}

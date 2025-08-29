package com.bekassyl.productstoreapp.controller;

import com.bekassyl.productstoreapp.models.Customer;
import com.bekassyl.productstoreapp.services.AdminService;
import com.bekassyl.productstoreapp.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final CustomerService customerService;

    @Autowired
    public AdminController(AdminService adminService, CustomerService customerService) {
        this.adminService = adminService;
        this.customerService = customerService;
    }

    @GetMapping
    public String showAllCustomers(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<Customer> customersPage = adminService.getPage(page);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.getCustomerByUsername(username).get();
        boolean isSupervisor = customer.getRole().equals("ROLE_SUPERVISOR");

        model.addAttribute("isSupervisor", isSupervisor);
        model.addAttribute("customers", customersPage.getContent());
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "admin/customersList";
    }

    @PutMapping("/{id}")
    public String changeRole(@PathVariable("id") int id) {
        adminService.changeRole(id);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable("id") int id) {
        adminService.deleteAccount(id);
        return "redirect:/admin";
    }
}

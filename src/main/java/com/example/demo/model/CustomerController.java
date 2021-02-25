package com.example.demo.model;

import java.util.ArrayList;

import org.slf4j.*; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomerController {
	
	//private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired 
	CustomerRepository repository ; 
	
	@GetMapping("/customer")
	public String customer(Model model) {
		
		  //CustomerRepository c = new CustomerRepository(); 
		  // save a few customers
	      repository.save(new Customer("Jack", "Bauer"));
	      repository.save(new Customer("Chloe", "O'Brian"));
	      repository.save(new Customer("Kim", "Bauer"));
	      repository.save(new Customer("David", "Palmer"));
	      repository.save(new Customer("Michelle", "Dessler"));

	      // fetch all customers
	      log.info("Customers found with findAll():");
	      log.info("-------------------------------");
	      
	      
	      ArrayList<Customer> customers = new ArrayList<>(  );
	      
	      for (Customer customer : repository.findAll() ) {
	    	  customers.add( customer );
	    	  log.info(customer.toString());
	      }
	      log.info("");

	      // fetch an individual customer by ID
	      Customer customer = repository.findById(1L);
	      log.info("Customer found with findById(1L):");
	      log.info("--------------------------------");
	      log.info( "" + customer );
	      log.info("");

	      // fetch customers by last name
	      log.info("Customer found with findByLastName('Bauer'):");
	      log.info("--------------------------------------------");
	      repository.findByLastName("Bauer").forEach(bauer -> {
	        log.info(bauer.toString());
	      });
	      
	      log.info("");
	      
	      model.addAttribute( "result", "Success" );
	      
	      model.addAttribute( "customers", customers ); 
		
		return "Customer.html"; 
	}
}

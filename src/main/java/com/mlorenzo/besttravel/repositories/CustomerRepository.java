package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
}

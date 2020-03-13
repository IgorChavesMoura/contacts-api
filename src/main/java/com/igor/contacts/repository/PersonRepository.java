package com.igor.contacts.repository;

import com.igor.contacts.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByNameContaining(String name);

}

package com.igor.contacts.repository;

import com.igor.contacts.model.Contact;
import com.igor.contacts.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact> findAllByPerson(Person person);



    @Query("SELECT c FROM Contact c WHERE c.person = ?1 AND (lower(c.name) LIKE lower(concat('%',?2,'%')) " +
                                                            "OR(lower(c.email) LIKE lower(concat('%',?2,'%'))) " +
                                                            "OR(c.phone LIKE %?2%) " +
                                                            "OR(c.whatsapp LIKE %?2%)) ")
    List<Contact> findAllByPersonAndInfoContaining(Person person, String query);



}

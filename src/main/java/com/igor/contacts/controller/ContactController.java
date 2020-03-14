package com.igor.contacts.controller;

import com.igor.contacts.model.Contact;
import com.igor.contacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/{id}")
    public ResponseEntity<Contact> findById(@PathVariable("id") Long id){

        Optional<Contact> contact = contactService.findById(id);

        //Se foi encontrada um contato com o id, retorna ok com o resultado. Senão retorna not found - Igor
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact){

        Optional<Contact> dbContact = contactService.findById(contact.getId());

        return dbContact.map(value -> ResponseEntity.ok(contactService.save(contact,value.getPerson()))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable("id") Long id){

        contactService.delete(id);

        return ResponseEntity.ok("Contact Removed");

    }


}

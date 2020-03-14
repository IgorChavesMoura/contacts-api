package com.igor.contacts.controller;

import com.igor.contacts.model.Contact;
import com.igor.contacts.model.Person;
import com.igor.contacts.service.ContactService;
import com.igor.contacts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public ResponseEntity<List<Person>> findPersons(@RequestParam(value = "q", required = false) Optional<String> query){

        List<Person> persons = personService.searchPersons(query);

        if(persons.size() > 0){

            return ResponseEntity.ok(persons);

        }

       return ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable("id") Long id){

        Optional<Person> person = personService.findById(id);

        //Se foi encontrada uma pessoa com o id, retorna ok com o resultado. Senão retorna not found - Igor
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<Contact>> findPersonContacts(@PathVariable("id") Long id){

        Optional<Person> person = personService.findById(id);

        //Se foi encontrada uma pessoa com o id, retorna ok com o resultado. Senão retorna not found - Igor
        return person.map(value -> ResponseEntity.ok(value.getContacts())).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<Contact> saveContactToPerson(@PathVariable("id") Long id, @RequestBody Contact contact){

         Optional<Person> person = personService.findById(id);

         //Se foi encontrada uma pessoa com o id, salva o novo contato nela e retorna o resultado. Senão retorna not found - Igor
         return person.map(value -> ResponseEntity.ok(contactService.save(contact,value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/")
    public ResponseEntity<Person> savePerson(@RequestBody Person person){

        return ResponseEntity.ok(personService.save(person));

    }

    @PutMapping("/")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person){

        return ResponseEntity.ok(personService.save(person));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") Long id){

        personService.delete(id);

        return ResponseEntity.ok("Person Removed");

    }


}

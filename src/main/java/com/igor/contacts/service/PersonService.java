package com.igor.contacts.service;

import com.igor.contacts.model.Person;
import com.igor.contacts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    public Optional<Person> findById(Long id){

        return Optional.of(personRepository.getOne(id));

    }

    public List<Person> searchPersons(Optional<String> query){

        //Checa se foi passado o parametro de filtragem para busca, se sim busca pelo nome, e retorna todos os registros caso contrário
        return query.isPresent() ? personRepository.findAllByNameContaining(query.get()) : personRepository.findAll();

    }

    public Person save(Person person){

        if(person.getId() != null){

            Optional<Person> dbPerson = findById(person.getId());

            if(dbPerson.isPresent()){

                dbPerson.get().setName(person.getName());

                return personRepository.save(dbPerson.get());

            }

            //Se por alguma razão o id que foi passado no objeto não existir no banco de dados, apenas remove o id inválido
            //e salva o objeto como novo registro
            person.setId(null);


        }

        return personRepository.save(person);

    }

    public void delete(Long id){

        personRepository.deleteById(id);

    }

}
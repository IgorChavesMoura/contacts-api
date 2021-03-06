package com.igor.contacts.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.igor.contacts.model.Person;
import com.igor.contacts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactService contactService;


    public Optional<Person> findById(Long id){

        return Optional.of(personRepository.getOne(id));

    }

    public Boolean personExists(Long id){

        return personRepository.existsById(id);

    }

    public List<Person> searchPersons(Optional<String> query){

        //Checa se foi passado o parametro de filtragem para busca, se sim busca pelo nome, e retorna todos os registros caso contrário - Igor
        return query.isPresent() ? personRepository.findAllByNameContainingIgnoreCase(query.get()) : personRepository.findAll();

    }

    public Person save(Person person){

        if(person.getId() != null){

            if(personExists(person.getId())){


                Optional<Person> dbPerson = findById(person.getId());

                if(dbPerson.isPresent()){

                    if(person.getName() != null && !person.getName().isEmpty()){
                        dbPerson.get().setName(person.getName());
                    }

                    if(person.getContacts() != null){

                        //Se algum dos contatos tiver uma informação para atualizar, executa a atualização - Igor
                        person.getContacts().forEach(c -> contactService.save(c,dbPerson.get()));

                    }

                    return personRepository.save(dbPerson.get());

                }



            }

            //Se por alguma razão o id que foi passado no objeto não existir no banco de dados, apenas remove o id inválido
            //e salva o objeto como novo registro - Igor
            person.setId(null);


        }

        return personRepository.save(person);

    }

    public void delete(Long id){

        Optional<Person> person = findById(id);

        if(person.isPresent()){

            //Se a pessoa tiver contatos associados a ela, precisa deletar os contatos primeiro.
            //Caso contrário o banco não deixará deletar por causa da chave estrangeira e da constraint - Igor
            person.get().getContacts().forEach(c -> contactService.delete(c.getId()));

            personRepository.deleteById(id);

        }



    }

}

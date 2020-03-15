package com.igor.contacts.service;

import com.igor.contacts.model.Contact;
import com.igor.contacts.model.Person;
import com.igor.contacts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;


    public Optional<Contact> findById(Long id){

        return Optional.of(contactRepository.getOne(id));

    }

    public Boolean contactExists(Long id){

        return contactRepository.existsById(id);

    }

    public List<Contact> findByPerson(Person person){

        return contactRepository.findAllByPerson(person);

    }

    public List<Contact> findByPersonAndInfo(Person person, String query){

        return contactRepository.findAllByPersonAndInfoContaining(person,query);

    }

    public Contact save(Contact contact, Person person){

        if(contact.getId() != null){


            if(contactExists(contact.getId())){

                Optional<Contact> dbContact = findById(contact.getId());

                if(dbContact.isPresent()){

                    if(contact.getName() != null && !contact.getName().isEmpty()){

                        dbContact.get().setName(contact.getName());

                    }

                    if(contact.getEmail() != null && !contact.getEmail().isEmpty()){

                        dbContact.get().setEmail(contact.getEmail());

                    }

                    if(contact.getPhone() != null && !contact.getPhone().isEmpty()){

                        dbContact.get().setPhone(contact.getPhone());

                    }

                    if(contact.getWhatsapp() != null && !contact.getWhatsapp().isEmpty()){

                        dbContact.get().setWhatsapp(contact.getWhatsapp());

                    }

                    return contactRepository.save(dbContact.get());

                }


            }

            //Se por alguma razão o id que foi passado no objeto não existir no banco de dados, apenas remove o id inválido
            //e salva o objeto como novo registro - Igor
            contact.setId(null);

        }

        contact.setPerson(person);

        return contactRepository.save(contact);

    }

    public void delete(Long id){

        contactRepository.deleteById(id);

    }
    

}

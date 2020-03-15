package com.igor.contacts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.contacts.controller.PersonController;
import com.igor.contacts.model.Contact;
import com.igor.contacts.model.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContactsApplication.class)
class ContactsApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllPersons(){

		Person personIgor = Person.from(1L,"Igor Chaves");
		Person personZe = Person.from(2L,"Ze Fulano");
		Person personClaudio = Person.from(3L,"Claudio Beltrano");

		//Testa se está retornando todos os registro da tabela person - Igor
		List<Person> persons = Arrays.asList(restTemplate.getForObject("/person",Person[].class));



		assertThat(persons, contains(personIgor,personZe,personClaudio));


	}

	@Test
	public void testGetPersonById(){

		Person personIgor = Person.from(1L,"Igor Chaves");

		Person person = restTemplate.getForObject("/person/1",Person.class);

		assertThat(person, is(personIgor));

	}

	@Test
	public void testSearchPersonByName(){

		Person personClaudio = Person.from(3L, "Claudio Beltrano");

		List<Person> persons = Arrays.asList(restTemplate.getForObject("/person?q=beltr",Person[].class));

		assertThat(persons, iterableWithSize(1));
		assertThat(persons.get(0), is(personClaudio));



	}

	@Test
	public void testGetPersonContacts(){

		List<Contact> igorContacts = new ArrayList<Contact>(){{

			add(Contact.from(5L,"Ze Fulano"));
			add(Contact.from(6L, "Claudio Beltrano"));

		}};

		List<Contact> contacts = Arrays.asList(restTemplate.getForObject("/person/1/contacts",Contact[].class));

		assertThat(contacts.size(), is(igorContacts.size()));
		assertThat(contacts, contains(igorContacts.toArray()));

	}

	@Test
	public void testSearchInPersonContacts(){

		Contact zeIgorContact = Contact.from(8L, "Igor Chaves");

		List<Contact> contacts = Arrays.asList(restTemplate.getForObject("/person/2/contacts?q=ig",Contact[].class));

		assertThat(contacts, iterableWithSize(1));

		assertThat(contacts.get(0),is(zeIgorContact));

	}

	@Test
	public void testCreatePerson() throws JsonProcessingException {

		Person personMauro = Person.from("Mauro Silcrano");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String personMauroJson = objectMapper.writeValueAsString(personMauro);

		HttpEntity<String> request = new HttpEntity<>(personMauroJson,headers);

		Person person = restTemplate.postForObject("/person", request, Person.class);

		assertThat(person.getId(), is(4L));

	}

	@Test
	public void testUpdatePerson() throws JsonProcessingException {

		Person personIgor = Person.from(1L,"Igor Moura");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String personIgorJson = objectMapper.writeValueAsString(personIgor);

		HttpEntity<String> request = new HttpEntity<>(personIgorJson,headers);

		Person person = restTemplate.postForObject("/person", request, Person.class);


		assertThat(person.getName(), is(personIgor.getName()));

	}

	@Test
	public void testUpdatePersonWithPut() throws  JsonProcessingException {


		Person personIgor = Person.from("Igor Chaves Moura");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String personIgorJson = objectMapper.writeValueAsString(personIgor);

		HttpEntity<String> request = new HttpEntity<>(personIgorJson,headers);

		ResponseEntity<Person> response = restTemplate.exchange("/person/1",HttpMethod.PUT,request,Person.class);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getId(), is(notNullValue()));
		assertThat(response.getBody().getName(), is(personIgor.getName()));

	}

	@Test
	public void testRemovePerson() throws JsonProcessingException {

		Person personMauro = Person.from("Mauro Silcrano");

		personMauro.setContacts(new ArrayList<Contact>(){{

			add(Contact.from("Igor Moura","+5511996455615","+5511996455615"));
			add(Contact.from("Ze Fulano", "+5511912345678", "+5511912345678"));

		}});

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String personMauroJson = objectMapper.writeValueAsString(personMauro);

		HttpEntity<String> request = new HttpEntity<>(personMauroJson,headers);

		Person person = restTemplate.postForObject("/person", request, Person.class);

		assertThat(person.getId(), is(notNullValue()));
		assertThat(person.getContacts(), iterableWithSize(2));

		ResponseEntity<String> response = restTemplate.exchange("/person/4",HttpMethod.DELETE,new HttpEntity<>("",headers),String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is("Person Removed"));

	}

	@Test
	public void testAddContactToPerson() throws  JsonProcessingException {

		Contact zeContact = Contact.from("Ze Fulano", "+5511912345678", "+5511912345678");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String zeContactJson = objectMapper.writeValueAsString(zeContact);

		HttpEntity<String> request = new HttpEntity<>(zeContactJson,headers);

		Contact contact = restTemplate.postForObject("/person/3/contacts",request,Contact.class);

		assertThat(contact.getId(), is(notNullValue()));

	}

	@Test
	public void testUpdatePersonContact() throws  JsonProcessingException {

		Contact zeContact = Contact.from("Ze Fulano", "+5511912345678", "+5511912345678");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String zeContactJson = objectMapper.writeValueAsString(zeContact);

		HttpEntity<String> request = new HttpEntity<>(zeContactJson,headers);

		Contact contact = restTemplate.postForObject("/person/3/contacts",request,Contact.class);

		assertThat(contact.getId(), is(11L));

		contact.setEmail("zefulano@email.com");

		zeContactJson = objectMapper.writeValueAsString(contact);

		request = new HttpEntity<>(zeContactJson,headers);

		contact = restTemplate.postForObject("/person/3/contacts",request,Contact.class);

		assertThat(contact.getEmail(), is("zefulano@email.com"));

	}

	@Test
	public void testUpdatePersonContactWithPut() throws JsonProcessingException {

		Contact contactZe = Contact.from("Zezim fulanim");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String contactZeJson = objectMapper.writeValueAsString(contactZe);

		HttpEntity<String> request = new HttpEntity<>(contactZeJson,headers);

		ResponseEntity<Contact> response = restTemplate.exchange("/contact/5",HttpMethod.PUT,request,Contact.class);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getId(), is(notNullValue()));
		assertThat(response.getBody().getName(), is(contactZe.getName()));
	}

	@Test
	public void testRemoveContactFromPerson() throws JsonProcessingException {

		Person personMauro = Person.from("Mauro Silcrano");

		personMauro.setContacts(new ArrayList<Contact>(){{

			add(Contact.from("Igor Moura","+5511996455615","+5511996455615"));
			add(Contact.from("Ze Fulano", "+5511912345678", "+5511912345678"));

		}});

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String personMauroJson = objectMapper.writeValueAsString(personMauro);

		HttpEntity<String> request = new HttpEntity<>(personMauroJson,headers);

		Person person = restTemplate.postForObject("/person", request, Person.class);

		assertThat(person.getId(), is(notNullValue()));
		assertThat(person.getContacts(), iterableWithSize(2));

		assertThat(person.getContacts().get(1).getId(),is(notNullValue()));

		ResponseEntity<String> response = restTemplate.exchange("/contact/" + person.getContacts().get(1).getId(),HttpMethod.DELETE,new HttpEntity<>("",headers),String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is("Contact Removed"));

	}



}

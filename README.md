# Contacts API

## API made with Java 8, Spring Boot and MySQL

##  Requirements: Docker and Docker Compose

## How to run the container cluster

> docker-compose build && docker-compose up
### This api  has 2 containers, one containing the Tomcat server and another containing the MySQL server
### Docker Compose will handle the build and run process and will also create the cluster network and link the containers
## This api has full CRUD to both person and contact entities

### Endpoints:
- GET /person -> Get all records from person entity
	Parameters:
	- q -> Filter persons that name contains the given value (optional)
- GET /person/{id} -> Get person with given id
- GET /person/{id}/contacts -> Get all contacts from the person with given id
	Parameters:
	- q -> Filter contacts that name,phone,email or whatsapp contains the given value (optional)
- GET /contact/{id} -> Get contact with given id
- POST /person -> Create new person record or update if the body has a VALID id
- POST /person/{id}/contacts -> Add a new contact to a person with the given id or update if the body has a VALID id
- PUT /person -> Update person record with given id field value
- PUT /person/{id} -> Update person with given id
- PUT /contact/{id} -> Update contact with given id
- DELETE /person/{id} -> Delete person with given id and ALL OF PERSONS CONTACTS
- DELETE /contact/{id} -> Delete contact with given id


### This api has a test class that runs some automatic tests and assertions when maven builds the jar file
### These tests are
- Get all persons
- Get person by id
- Search person by name
- Get person contacts
- Search in person contacts
- Create person
- Update person using POST
- Update person using PUT
- Remove person
- Add contact to a person
- Update a persons contact using POST
- Update a persons contact using PUT
- Remove contact from person
##### You can check and add more tests editing the test class file: src/test/java/com/igor/contacts/ContactsApplicationTests.java

### If you wanna build the jar file and run the tests above automatically you can run:
> mvn clean install package
#### This command will use maven the build the spring boot app, run the tests and pack it with the dependencies in jar file
#### If you wanna to build the jar file, build and run the container cluster you can run:
> ./build_run_all.sh

#### Obs: You need to have maven installed to build the jar file

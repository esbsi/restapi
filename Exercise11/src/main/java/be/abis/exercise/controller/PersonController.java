package be.abis.exercise.controller;

import be.abis.exercise.exception.ApiError;
import be.abis.exercise.exception.ApiKeyNotCorrectException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.exception.PersonNotFoundException;
import be.abis.exercise.model.Login;
import be.abis.exercise.model.Password;
import be.abis.exercise.model.Person;
import be.abis.exercise.model.Persons;
import be.abis.exercise.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController // returns json response body (@Controller returns webpage)
@EnableGlobalMethodSecurity(jsr250Enabled=true)
public class PersonController {

    @Autowired
    PersonService personService;

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/persons/query")
    public ResponseEntity<? extends Object> findPersonByMailAndPwd(@RequestParam String mail, @RequestParam(name= "pwd") String password){
        try{Person person = personService.findPerson(mail, password);
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiError apiError = new ApiError("Person not found.", status.value(), e.getMessage());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            return new ResponseEntity<ApiError>(apiError, responseHeaders, status);
        }
    }

    @PostMapping("/persons/postlogin")
    public ResponseEntity<? extends Object> findPersonByMailAndPwd2(@RequestBody Login login) {
        try{Person person = personService.findPerson(login.getEmailAddress(), login.getPassword());
            HttpHeaders responseHeaders = new HttpHeaders();
            // ToDo
            responseHeaders.add("api-key", "1aqw"); ////////////////-------------------
            return new ResponseEntity<Person>(person, responseHeaders, HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiError apiError = new ApiError("Person not found.", status.value(), e.getMessage());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            return new ResponseEntity<ApiError>(apiError, responseHeaders, status);
        }
    }

    @GetMapping("persons/{id}") // add header to request: accept application/xml
    public ResponseEntity<? extends Object> findPerson(@PathVariable int id) {
        try{Person person = personService.findPerson(id);
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiError apiError = new ApiError("Person not found.", status.value(), e.getMessage());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            return new ResponseEntity<ApiError>(apiError, responseHeaders, status);
        }
    }

    @GetMapping("persons")
    public ArrayList<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping(path = "persons")
    public ResponseEntity<? extends Object> addPerson(@Valid @RequestBody Person p) throws IOException, PersonAlreadyExistsException {
        personService.addPerson(p);
        return new ResponseEntity<>(Void.class, HttpStatus.OK);
    }

    @DeleteMapping("persons/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public void deletePerson(@PathVariable int id) {
        personService.deletePerson(id);
    }

/*    @PutMapping("persons/{id}")
    public void changePassword(@PathVariable int id, @RequestBody String newPswd) throws IOException, PersonNotFoundException {
        Person p = personService.findPerson(id);
        personService.changePassword(p, newPswd);
    }
*/

    @PatchMapping("persons/{id}")
    public ResponseEntity<? extends Object> changePassword(@PathVariable int id, @Valid @RequestBody Password newPswd, @RequestHeader MultiValueMap<String, String> headers) throws IOException, PersonNotFoundException, ApiKeyNotCorrectException {
        boolean keyOK = false;
        if (headers.containsKey("api-key")){
            String auth = headers.get("api-key").get(0);
            System.out.println("key-passed: " + auth);
            boolean tokenOK = this.checkToken(auth);
            if (tokenOK) keyOK = true;
        } if (keyOK) {
            Person person = personService.findPerson(id);
            personService.changePassword(person, newPswd.getPassword());
            return new ResponseEntity<>(Void.class, HttpStatus.OK);
        } else throw new ApiKeyNotCorrectException("Please provide a valid API key.");
    }

    private boolean checkToken(String token){
        boolean tokenOK = false;
        List<String> acceptedTokens = new ArrayList<>();
        acceptedTokens.add("1aqw");
        acceptedTokens.add("2zsx");
        acceptedTokens.add("3edc");
        acceptedTokens.add(UUID.randomUUID().toString());
        if (token != null && acceptedTokens.contains(token)) tokenOK = true;
        return tokenOK;
    }

//    @GetMapping(path = "persons/company", produces = MediaType.APPLICATION_XML_VALUE)
    @GetMapping(path = "persons/company") // add header to request: accept application/xml
    public Persons findPersonsByCompanyName(@RequestParam String companyName){
        Persons persons = new Persons();
        persons.setPersons(personService.findPersonsByCompanyName(companyName));
        System.out.println(persons.getPersons().size());
        return persons;
    }

}

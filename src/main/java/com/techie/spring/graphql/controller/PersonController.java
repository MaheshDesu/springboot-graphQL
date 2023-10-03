package com.techie.spring.graphql.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techie.spring.graphql.dao.PersonRepository;
import com.techie.spring.graphql.entity.Person;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;


@RestController
public class PersonController {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Value("classpath:graphql/person.graphqls")
	private Resource schemaResource;
	
	private GraphQL graphQL;
	
	@PostConstruct
	public void loadSchema() throws IOException {
		File schemaFile = schemaResource.getFile();
		TypeDefinitionRegistry registy = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring =  buildWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registy, wiring);
		graphQL =  GraphQL.newGraphQL(schema).build();
	}
	
	private RuntimeWiring buildWiring() {

		DataFetcher<List<Person>> fetcher1 = data-> {
			return (List<Person>) personRepository.findAll();
		};
		
		DataFetcher<Person> fetcher2 =  data-> {
			return personRepository.findByEmail(data.getArgument("email"));
		};
		
		return RuntimeWiring.newRuntimeWiring().type("Query", typeWriting -> 
			typeWriting.dataFetcher("getAllPersons", fetcher1).dataFetcher("findPerson", fetcher2)).build();
	}

	@PostMapping("/addPersons")
	public String addPersons(@RequestBody List<Person> persons) {
		personRepository.saveAll(persons);
		return "Record Inserted";
	}
	
	@GetMapping("/findAllPersons")
	public List<Person> getPersons() {
		return (List<Person>) personRepository.findAll();
	}
	
	@PostMapping("/getAll")
	public ResponseEntity<Object> getAll(@RequestBody String query) {
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	@PostMapping("/personByEmal")
	public ResponseEntity<Object> personByEmal(@RequestBody String query) {
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

}

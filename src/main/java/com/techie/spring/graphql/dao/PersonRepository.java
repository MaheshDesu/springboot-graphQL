package com.techie.spring.graphql.dao;

import org.springframework.data.repository.CrudRepository;

import com.techie.spring.graphql.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Integer>{
	
	public Person findByEmail(String email);

}

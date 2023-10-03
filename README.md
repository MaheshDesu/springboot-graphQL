# springboot-graphQL
 
API urls and payload:

1. Add Persons call
http://localhost:8080/addPersons

[
    {
        "id": 11035,
        "name": "Mahesh",
        "mobile": "816-286-1234",
        "email": "mahesh1@gmail.com",
        "address": [
            "gydf"
        ]
    },
    {
        "id": 1815,
        "name": "Mahesh",
        "mobile": "816-286-1234",
        "email": "mahesh2@gmail.com",
        "address": [
            "gydf"
        ]
    }
]

2. Find All Persons
http://localhost:8080/findAllPersons

3. Get all persons via **GraphQL**
http://localhost:8080/getAll

{
    getAllPersons {
        id
        mobile
        address
    }
}

4. Find Person obj by email via **GraphQL**
http://localhost:8080/personByEmal

{
    findPerson(email: "mahesh1@gmail.com") {
        id
        mobile
        name
    }
}

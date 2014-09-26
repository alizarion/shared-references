Generic DAO
=============

Author : Selim Bensenouci

What is that?
--------------

Simple java toolkit which makes DAO manager creating easier,<br/>
currently only CRUD jpa operations are implemented(persist, merge, find, findAll ...)

How to use it?
--------------

for your JPA DAO manager class you need to extent class JpaDao, and pass the entity manager to the construtor.
<br/>
<code>

public class PersonDao extends JpaDao<Long,Person> {
<br/>
public PersonDao(EntityManager entityManager) {
<br/>
        super(entityManager);
        <br/>
    }<br/>
}

</code> 


<br/>
And now you can use it in your ejb service.
<br/>

<code>

@Statefull
public class PersonService{

        @PersistenceContext
        private EntityManager entityManager;
    
        private PersonDao personDao; 
    
        @PostConstruct
         public void setUp(){
        this.personDao =  new PersonDao(this.entityManager);
        }
        
        public List<Person> getAllRegistredPerson(){
        return personDao.findAll();
        }
}

</code> 
<br/>



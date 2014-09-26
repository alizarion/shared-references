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

    public ResourceDao(EntityManager entityManager) {
        super(entityManager);
    }
    
}</code> 
<br/>

email
=====

Author : Selim Bensenouci

what is that?
-------------

this provides mail rendering and loggin system, it also offers a email  
provider interface that must be implemented by all kind of mail provider.

how to use it?
--------------

the abstract class Email must be extended by every kind of emails,  
each of them has specific folder that contain StringTemplate files by language.  

exemple:  

we want to create Resgister validation email  

1. we extend Email class to define specific email type  

'GenericRegisterEmail' extend 'Email'

```java
@Entity
@DiscriminatorValue(value = GenericRegisterEmail.TYPE)
public class GenericRegisterEmail extends Email {

    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";
    
    .......... email specific properties that contain data for rendering 
    
    
}
```
2. creation of 'register-email' folder, that containt 3 files (subject_en.stg, bodyHTML_en.stg,  
bodyText_en.stg) that contain subject mime html and mime text email template.

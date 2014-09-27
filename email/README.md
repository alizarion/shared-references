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

'GenericRegisterEmail' extend 'Email'


'''java
@Entity
@DiscriminatorValue(value = GenericRegisterEmail.TYPE)
public class GenericRegisterEmail extends Email {

    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";
    
    .......... email specific properties that contain data for rendering 
    
    
}

'''

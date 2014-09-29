email
=====

Author : Selim Bensenouci

what is that?
-------------

this provides mail rendering and loggin system, it also offers a email  
provider interface that must be implemented by all kind of mail provider.

sent email are mapped using JPA with database as loggin system.

how to use it?
--------------

This library allows one hand the management of email rendering `by extending Email`, and the other,    
publishing transmission services to sent them `using EmailProvider services`.

The abstract class Email must be extended by every kind of emails,  
each of them has specific folder that contain StringTemplate files by language.  

example:    

We want to create Resgister validation email, that will send to new users,   
email which contain token to activate their account.  

1\ we extend Email class to define specific email type
`GenericRegisterEmail` extend `Email`

```java
@Entity
@DiscriminatorValue(value = GenericRegisterEmail.TYPE)
public class GenericRegisterEmail extends Email {

    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";
    
    .......... email specific properties that contain data for rendering 
    
    
}
```  

2\ Creation of `register-email` folder, that containt 3 files ( subject_en.stg, bodyHTML_en.stg,  
bodyText_en.stg).  

3\ The constructor of your email must take data to be contained in the email, they must   
be inserted into a `Map<String, Object>`, each part of the email has its own Map  
objects allowing them exposed diffrentes data.   

Pass to the new email constructor specific fields, that will be used in rendering as   
collection of `Map<String,Oject>`, some fields in the parent constructor are required:   

    * from => sender email   
    * to => primary recipient  
    * locale => language that will be used to render the message
    * you can extend the parent constructor to pass specific email fields.   


```java


 public GenericRegisterEmail(RegisterEmailBuilder builder) {
        super(builder.getFrom(),
                builder.getTo(),
                builder.getTemplateRoot(),
                builder.getLocale());
        this.emailToken = builder.getToken();
        super.setCc(builder.getCc());
        super.setCci(builder.getCci());

        // email subject data map
        Map<String,Object> subject = new HashMap<>();
                
        // email html body data map
        Map<String,Object> bodyHTML = new HashMap<>();
        
        // email text body data map
        Map<String,Object> bodyText = new HashMap<>();

        subject.put("emailToken",builder.getToken());
        bodyHTML.put("emailToken",builder.getToken());
        bodyText.put("emailToken",builder.getToken());

        this.params.put(MAIL_SUBJECT_TEMPLATE,subject);
        this.params.put(MAIL_HTML_BODY_TEMPLATE,bodyHTML);
        this.params.put(MAIL_TEXT_BODY_TEMPLATE,bodyText);
    }
    
````  

4\  send your email to `EmailProvider`   

* example with `SimpleJavaMailProvider`:

```java

    GenericRegisterEmail genericRegisterEmail = new GenericRegisterEmail(registerEmailData);
    SimpleJavaMailProvider javaMailProvider =  new SimpleJavaMailProvider(fom,to,locale,dataToSend);
    javaMailProvider.sendMail(this.registerEmail);

```

* with `EmailProviderService` EJB using Jboss mail resource present in email-services-ejb.

```java

    // Inject EJB provider service 
    @Ejb 
    private EmailProviderService provider;
    
    public void sendGenericRegistredEmail(XXXX dataToSend){
    
    // you can use generic email builder to create email with required fields
    GenericRegisterEmail email = new GenericRegisterEmail.  
    GenericRegisterEmailBuilder(from,to,Locale.getDefault(), dataToSend).build();   
    provider.send(email);
    
    }
```





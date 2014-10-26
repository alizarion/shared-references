Security rest services
----------------------

Do not forget to add oauth realm in your `standalone.xml file

```xml
<security-domain name="oauth">
    <authentication>
        <login-module code="org.jboss.security.auth.spi.DatabaseServerLoginModule" flag="required">
            <module-option name="dsJndiName" value="java:/oauthDS"/>
            <module-option name="principalsQuery" value="SELECT security_credential.password FROM security_credential WHERE security_credential.user_name = ? and security_credential.state = 'A' and security_credential.logon = 'P'"/>
            <module-option name="rolesQuery" value="select security_roles.unique_key, 'Roles' from security_roles left join security_credential_roles on security_roles.id = security_credential_roles.role_id left join security_credential on security_credential_roles.credential_id = security_credential.id  where security_credential.user_name = ?"/>
            <module-option name="hashAlgorithm" value="SHA"/>
            <module-option name="hashEncoding" value="hex"/>
        </login-module>
    </authentication>
</security-domain>
``

and data source

```xml
<datasource jta="true" jndi-name="java:/testDS" pool-name="test_pool" enabled="true" use-java-context="true" use-ccm="true">
    <connection-url>jdbc:mysql://localhost:3306/myDataBase</connection-url>
     <driver>mysql</driver>
      <security>
        <user-name>root</user-name>
         </security>
       <statement>
           <prepared-statement-cache-size>100</prepared-statement-cache-size>
           <share-prepared-statements>true</share-prepared-statements>
       </statement>
</datasource>
``
                
   
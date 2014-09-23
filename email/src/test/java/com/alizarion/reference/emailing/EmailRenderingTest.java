package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.helper.EmailTestHelper;
import com.alizarion.reference.person.entities.ValidateEmailToken;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
public class EmailRenderingTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    File subject;
    File bodyHTML;
    File bodyText;

    @Before
    public void before() throws IOException {

        subject  =  temporaryFolder.newFile(Email.MAIL_SUBJECT_TEMPLATE+".stg");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(subject));
        out1.write("subject(name) ::= \"Hello subject, $name$\"");
        out1.close();
        bodyHTML = temporaryFolder.newFile(Email.MAIL_HTML_BODY_TEMPLATE+".stg");
        BufferedWriter out2 = new BufferedWriter(new FileWriter(bodyHTML));
        out2.write("bodyHTML(token) ::= \" hello $token.person.firstName$ " +
                " $token.person.lastName$ click on this token " +
                " $token.token$ to activate your account  \"");
        out2.close();
        bodyText = temporaryFolder.newFile(Email.MAIL_TEXT_BODY_TEMPLATE + ".stg");
        BufferedWriter out3 = new BufferedWriter(new FileWriter(bodyText));
        out3.write("Hello bodyText, $name$");
        out3.close();


    }

    @Test
    public void test() {
        STGroupFile stGroupFile =  new STGroupFile(bodyHTML.getAbsolutePath(),'$','$');
        ST st =  stGroupFile.getInstanceOf("bodyHTML");
        ValidateEmailToken validateEmailToken = EmailTestHelper.getValidateEmailToken();
        System.out.println(Locale.getDefault().getLanguage());
        st.add("token",validateEmailToken);
        System.out.println(st.render());

    }
}


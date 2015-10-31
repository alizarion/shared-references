package com.alizarion.reference.emailing.entities;

import com.alizarion.reference.emailing.exception.EmailRenderingException;
import com.alizarion.reference.emailing.helper.EmailTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GenericRegisterEmailTest  {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    GenericRegisterEmail registerEmail;

    GenericResetPasswordEmail resetPasswordEmail;

    @Before
    public void setUp() throws URISyntaxException, EmailRenderingException, IOException, AddressException {
        this.registerEmail = EmailTestHelper.getGenericRegisterEmail(
                new URI(temporaryFolder.getRoot().getAbsolutePath()));
        this.resetPasswordEmail =  EmailTestHelper.
                getGenericResetPasswordEmail(
                        new URI(temporaryFolder.getRoot().getAbsolutePath()));
        EmailTestHelper.createTemplateFiles(this.registerEmail);
        EmailTestHelper.createTemplateFiles(this.resetPasswordEmail);
    }

    @Test
    public void templatePathRendering() throws EmailRenderingException {
        Assert.assertNotNull("subject" + this.registerEmail.getSubject());
        Assert.assertNotNull("getHTMLBody" + this.registerEmail.getHTMLBody());
        Assert.assertNotNull("getTextBody" + this.registerEmail.getTextBody());

        System.out.println(this.resetPasswordEmail.getTextBody());
        System.out.println(this.registerEmail.getTextBody());

    }

}
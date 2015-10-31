package com.alizarion.reference.emailing.provider;

import com.alizarion.reference.emailing.entities.GenericRegisterEmail;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.helper.EmailTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;

public class SimpleJavaMailProviderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    public GenericRegisterEmail registerEmail;

    public File file1;

    public File file2;


    @Before
    public void setUp() throws Exception {
        //TODO corrigé le test
        file1 = temporaryFolder.newFile("attachment1.txt");

        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        out1.write("blablalbalbla in attachment1\n");
        out1.close();

        file2 =  temporaryFolder.newFile("attachment2.txt") ;
        BufferedWriter out2 =  new BufferedWriter(new FileWriter(file2));
        out2.write("blablabla in attachment2");
        out2.close();
        this.registerEmail = EmailTestHelper.getGenericRegisterEmail(
                       new URI(temporaryFolder.getRoot().getAbsolutePath()));
        EmailTestHelper.createTemplateFiles(this.registerEmail);

    }

    @Test
    public void sendMailTest() throws EmailException {
        SimpleJavaMailProvider javaMailProvider =  new SimpleJavaMailProvider();
        this.registerEmail.addAttachment(file1);
        this.registerEmail.addAttachment(file2);
        this.registerEmail = (GenericRegisterEmail) javaMailProvider.sendMail(this.registerEmail);
    }

}
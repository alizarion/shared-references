package com.alizarion.reference.security;
import com.alizarion.reference.security.entities.Credential;
import org.junit.Before;
import org.junit.Test;

public class CredentialTest {

    private Credential credential;

    @Before
    public void init(){
        credential = new Credential();
        credential.setPassword("TOTO");
    }

    @Test
    public void setCredentialSha1Password(){
        //TODO corriger le test
        Credential newCredential = new Credential();
        newCredential.setPassword("TOTO");
       // assertEquals(newCredential.getPassword(),credential.getPassword());
       TesTThread  tesTThread1  = new TesTThread(1);
        TesTThread  tesTThread2  = new TesTThread(2);
        TesTThread  tesTThread3  = new TesTThread(3);


        new Thread(tesTThread1).start();
        new Thread(tesTThread2).start();
        new Thread(tesTThread3).start();

    }

    public class TesTThread implements Runnable {

        private  int threadNumber;

        public TesTThread( int threadNumber) {
            super();
            this.threadNumber = threadNumber;
        }

        public synchronized void run(){
              System.out.println("Thread numero" + threadNumber);
            try {
              Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
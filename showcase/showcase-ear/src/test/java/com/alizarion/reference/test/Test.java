package com.alizarion.reference.test;

/**
 * @author selim@openlinux.fr.
 */
public class Test {
    public class TITI{
        private TOTO toto;

        public boolean isTotoAlive(){
            return this.toto != null && toto.isAlive();
        }
    }

    public class TOTO{

        public boolean isAlive(){
            return true;
        }
    }

    @org.junit.Test
    public void simpleJavaTest(){

        System.out.println(new TITI().isTotoAlive());
    }
}

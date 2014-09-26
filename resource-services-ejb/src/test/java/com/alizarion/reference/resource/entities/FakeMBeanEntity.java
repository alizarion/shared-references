package com.alizarion.reference.resource.entities;
import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import com.alizarion.reference.resource.mbean.PersistentMBean;

/**
 * @author selim@openlinux.fr.
 */
public class FakeMBeanEntity extends PersistentMBean{

    public final static String KEY = "fake-key";

    public FakeMBeanEntity() {

    }

    @Override
    public String getCategory() {
        return "fake.proprieties.for.unit.test";
    }

    public String getKeyValue() throws PersistentResourceNotFoundException {
        return getValue(KEY);
    }
}

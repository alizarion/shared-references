package com.alizarion.reference.location.dao;

import com.alizarion.reference.dao.jpa.JpaDao;
import com.alizarion.reference.location.entities.Address;
import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.location.entities.PhysicalAddress;
import com.alizarion.reference.location.entities.WebAddress;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
public class AddressJpaDao extends JpaDao<Long,Address> {

    protected AddressJpaDao(EntityManager entityManager) {
        super(entityManager);
    }

    @SuppressWarnings(value = "unchecked")
    public List<ElectronicAddress> findElectronicAddressLike(
            String electronicPart){
        return getEntityManager().
                createNamedQuery(ElectronicAddress.
                        FIND_BY_PART).
                setParameter("emailPart",
                        "%"+electronicPart+"%").getResultList();
    }

    @SuppressWarnings(value = "unchecked")
    public ElectronicAddress findElectronicAddressByValue(String email){
        List<ElectronicAddress> electronicAddresses = getEntityManager().
                createNamedQuery(ElectronicAddress.
                        FIND_BY_EMAIL).
                setParameter("email",
                        email).getResultList();
        ElectronicAddress result =  null;
        if (!electronicAddresses.isEmpty()){
            result = electronicAddresses.get(0);
        }
        return result;
    }


    @SuppressWarnings(value = "unchecked")
    public List<PhysicalAddress> findPhysicalAddressByPart(String part){
        List<PhysicalAddress> physicalAddresses = new ArrayList<>();
        if (part.length()>4) {
            physicalAddresses = getEntityManager().
                    createNamedQuery(PhysicalAddress.
                            FIND_BY_PART).
                    setParameter("part",
                            "%" + part + "%").getResultList();
        }
        return physicalAddresses;
    }

    @SuppressWarnings(value = "unchecked")
    public List<PhysicalAddress> findPhysicalAddressByPart(
            final String part,
            final String country,
            final String zip){
        List<PhysicalAddress> physicalAddresses = new ArrayList<>();
        if (part.length()>3) {
            physicalAddresses = getEntityManager().
                    createNamedQuery(PhysicalAddress.
                            FIND_BY_PART_WITH_COUNTRY_ZIP).
                    setParameter("part",
                            "%" + part + "%").
                    setParameter("countryId",country).
                    setParameter("zipCode",zip).getResultList();
        }
        return physicalAddresses;
    }

    @SuppressWarnings(value = "unchecked")
    public List<WebAddress> findWebAddressByPart(
            final String part){
        List<WebAddress> webAddresses = new ArrayList<>();
        if (part.length()>3) {
            webAddresses = getEntityManager().
                    createNamedQuery(WebAddress.
                            FIND_BY_PART).
                    setParameter("part",
                            "%" + part + "%").getResultList();
        }
        return webAddresses;
    }


}

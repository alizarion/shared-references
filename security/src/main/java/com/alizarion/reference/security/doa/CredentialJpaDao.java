package com.alizarion.reference.security.doa;

import com.alizarion.reference.dao.jpa.JpaDao;
import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.ResetPasswordToken;
import com.alizarion.reference.security.entities.Role;
import com.alizarion.reference.security.entities.ValidateEmailToken;
import com.alizarion.reference.security.exception.InvalidSecurityTokenException;
import com.alizarion.reference.security.exception.InvalidUsernameException;
import com.alizarion.reference.security.exception.RoleNotFoundException;
import com.alizarion.reference.security.oauth.oauth2.exception.InvalidTokenException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
public class CredentialJpaDao extends JpaDao<Long,Credential> {

    public CredentialJpaDao(EntityManager entityManager) {
        super(entityManager);
    }

    @SuppressWarnings(value = "unchecked")
    public Credential getCredentialByUserName(final String username)
            throws InvalidUsernameException {
        List<Credential> result  = em.
                createNamedQuery(Credential.FIND_BY_USERNAME_NAMED_QUERY).
                setParameter("username", username).getResultList();
        if (!result.isEmpty()){
            return result.get(0);
        } else {
            throw new InvalidUsernameException(username);
        }
    }

    @SuppressWarnings("unchecked")
    public Role getRoleByKey(final String roleKey) throws RoleNotFoundException {
        List<Role> roles =  this.em.createNamedQuery(Role.FIND_BY_KEY)
                .setParameter("roleKey", roleKey).getResultList();

        if (!roles.isEmpty()){
            return roles.get(0);
        }  else {
            throw new RoleNotFoundException(roleKey);
        }
    }

    @SuppressWarnings("unchecked")
    public ValidateEmailToken findValidateEmailTokenByValue(
            final String tokenValue)
            throws InvalidSecurityTokenException {
        List<ValidateEmailToken> emailTokens =
                this.em.createNamedQuery(ValidateEmailToken.FIND_TOKEN_BY_VALUE)
                        .setParameter("tokenValue", tokenValue)
                        .getResultList();

        if (!emailTokens.isEmpty()){
            return emailTokens.get(0);
        }  else {
            throw new InvalidSecurityTokenException(tokenValue);
        }
    }

    @SuppressWarnings("unchecked")
    public ResetPasswordToken findResetPasswordTokenByValue(
            final String tokenValue)
            throws InvalidTokenException {
        List<ResetPasswordToken> emailTokens =
                this.em.createNamedQuery(ResetPasswordToken.FIND_TOKEN_BY_VALUE)
                        .setParameter("tokenValue", tokenValue)
                        .getResultList();

        if (!emailTokens.isEmpty()){
            return emailTokens.get(0);
        }  else {
            throw new InvalidTokenException(tokenValue);
        }
    }
}

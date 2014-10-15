package com.alizarion.reference.security.oauth.ws;

import com.alizarion.reference.security.oauth.exception.InvalidAccessTokenException;
import com.alizarion.reference.security.oauth.toolkit.TokenInfoDTO;
import com.alizarion.reference.security.services.oauth.OAuthServerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author selim@openlinux.fr.
 */
@Path("/tokeninfo")
public class TokenInfoEndPoint {

    @EJB
    private OAuthServerService oauthService;

    public static final String ACCESS_TOKEN_PARAM ="access_token";
    public static final String ID_TOKEN_PARAM ="id_token";


    @GET
    @Produces("application/json")
    public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException {
        try {
            if (!StringUtils.isEmpty(request.getParameter(ACCESS_TOKEN_PARAM))) {

                TokenInfoDTO tokenInfoDTO =  new TokenInfoDTO(oauthService
                        .findAliveAccessToken(request
                                .getParameter(ACCESS_TOKEN_PARAM)));
               /* ObjectMapper
                JSONJAXBContext jc = JAXBContext.newInstance(TokenInfoDTO.class);
                StringWriter writer =  new StringWriter();
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
             marshaller.marshal(tokenInfoDTO,writer); */
                return Response.ok(tokenInfoDTO, MediaType.APPLICATION_JSON).build();


            } else if(!StringUtils.isEmpty(request.getParameter(ID_TOKEN_PARAM))){
                //TODO implement id_token flow
                return buildInvalidTokenResponse();
            } else {
                return buildInvalidTokenResponse();
            }

        } catch (InvalidAccessTokenException  e) {
            return buildInvalidTokenResponse();
        }

    }

    public Response buildInvalidTokenResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .setErrorDescription("invalid value")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }
}

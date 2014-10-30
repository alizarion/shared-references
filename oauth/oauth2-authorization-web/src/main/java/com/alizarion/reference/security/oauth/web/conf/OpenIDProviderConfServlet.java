package com.alizarion.reference.security.oauth.web.conf;

import org.stringtemplate.v4.ST;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author selim@openlinux.fr.
 */
@WebServlet("/.well-known/openid-configuration")
public class OpenIDProviderConfServlet extends HttpServlet {
    private static final long serialVersionUID = 169314174973091492L;

    private final static String OPENID_PROVIDER_CONFIG_TEMPLATE =
            "/openid-configuration.stg";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(getConfiguration(request.getServerName()));
    }


    public String getConfiguration(final String issuer) throws IOException {
        byte[] encoded = Files.readAllBytes(
                Paths.get(getClass()
                        .getResource(OPENID_PROVIDER_CONFIG_TEMPLATE)
                        .getPath()));
        ST providerConfigTemplate = new ST(new String(encoded));
        providerConfigTemplate.add("issuer", issuer);
        return providerConfigTemplate.render();
    }


}

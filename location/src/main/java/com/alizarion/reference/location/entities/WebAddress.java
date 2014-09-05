package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Simple class to store and persist web address information
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_web_address")
@DiscriminatorValue(value = "web")
@PrimaryKeyJoinColumn(name = "web_address_id")
public class WebAddress extends Address implements Serializable {


    @Column(name = "hostname")
    private String host;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "uri",length = 2048)
    private String sourceURI;

    public WebAddress() {
    }

    public WebAddress(String url) throws
            URISyntaxException,
            MalformedURLException {
        URI uri = new URI(url);
        URL tempURL = uri.toURL();
        this.host =  tempURL.getHost();
        this.protocol = tempURL.getProtocol();
        this.sourceURI = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSourceURI() {
        return sourceURI;
    }

    public void setSourceURI(String sourceURI) {
        this.sourceURI = sourceURI;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WebAddress)) return false;

        WebAddress that = (WebAddress) o;

        if (host != null ? !host.equals(that.host) :
                that.host != null) return false;
        if (protocol != null ? !protocol.equals(that.protocol) :
                that.protocol != null) return false;
        if (sourceURI != null ? !sourceURI.equals(that.sourceURI) :
                that.sourceURI != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (sourceURI != null ? sourceURI.hashCode() : 0);
        return result;
    }
}

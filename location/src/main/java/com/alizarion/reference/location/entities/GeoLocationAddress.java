package com.alizarion.reference.location.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table( name = "location_geo_address")
@DiscriminatorValue(value = GeoLocationAddress.TYPE)
public class GeoLocationAddress extends Address {

    public final static String TYPE = "geo";

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    protected GeoLocationAddress(){

    }



    public GeoLocationAddress(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeoLocationAddress)) return false;
        if (!super.equals(o)) return false;

        GeoLocationAddress that = (GeoLocationAddress) o;

        return !(latitude != null ? !latitude.equals(that.latitude) :
                that.latitude != null) && !(longitude != null
                ? !longitude.equals(that.longitude) :
                that.longitude != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}

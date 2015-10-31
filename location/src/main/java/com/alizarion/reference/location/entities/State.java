package com.alizarion.reference.location.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table( name = "location_state")
public class State implements Serializable {

    private static final long serialVersionUID = 3654239825048094858L;

    @Id
    @Column(name = "state_id")
    private String state;

    public State() {
    }

    public State(final String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof State)) return false;

        State state1 = (State) o;

        return !(state != null ?
                !state.equals(state1.state) :
                state1.state != null);

    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }
}

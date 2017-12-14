/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author thanh
 */
@Embeddable
public class TicketReadsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ticket_id")
    private int ticketId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reader_id")
    private int readerId;

    public TicketReadsPK() {
    }

    public TicketReadsPK(int ticketId, int readerId) {
        this.ticketId = ticketId;
        this.readerId = readerId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ticketId;
        hash += (int) readerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketReadsPK)) {
            return false;
        }
        TicketReadsPK other = (TicketReadsPK) object;
        if (this.ticketId != other.ticketId) {
            return false;
        }
        if (this.readerId != other.readerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TicketReadsPK[ ticketId=" + ticketId + ", readerId=" + readerId + " ]";
    }
    
}

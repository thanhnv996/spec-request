/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thanh
 */
@Entity
@Table(name = "ticket_reads")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketReads.findAll", query = "SELECT t FROM TicketReads t"),
    @NamedQuery(name = "TicketReads.findByTicketId", query = "SELECT t FROM TicketReads t WHERE t.ticketReadsPK.ticketId = :ticketId"),
    @NamedQuery(name = "TicketReads.findByReaderId", query = "SELECT t FROM TicketReads t WHERE t.ticketReadsPK.readerId = :readerId"),
    @NamedQuery(name = "TicketReads.findByStatus", query = "SELECT t FROM TicketReads t WHERE t.status = :status")})
public class TicketReads implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TicketReadsPK ticketReadsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private short status;
    @JoinColumn(name = "reader_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Employees employees;
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tickets tickets;

    public TicketReads() {
    }

    public TicketReads(TicketReadsPK ticketReadsPK) {
        this.ticketReadsPK = ticketReadsPK;
    }

    public TicketReads(TicketReadsPK ticketReadsPK, short status) {
        this.ticketReadsPK = ticketReadsPK;
        this.status = status;
    }

    public TicketReads(int ticketId, int readerId) {
        this.ticketReadsPK = new TicketReadsPK(ticketId, readerId);
    }

    public TicketReadsPK getTicketReadsPK() {
        return ticketReadsPK;
    }

    public void setTicketReadsPK(TicketReadsPK ticketReadsPK) {
        this.ticketReadsPK = ticketReadsPK;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    public Tickets getTickets() {
        return tickets;
    }

    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ticketReadsPK != null ? ticketReadsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketReads)) {
            return false;
        }
        TicketReads other = (TicketReads) object;
        if ((this.ticketReadsPK == null && other.ticketReadsPK != null) || (this.ticketReadsPK != null && !this.ticketReadsPK.equals(other.ticketReadsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TicketReads[ ticketReadsPK=" + ticketReadsPK + " ]";
    }
    
}

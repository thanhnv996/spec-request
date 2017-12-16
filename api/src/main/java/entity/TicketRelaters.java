/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "ticket_relaters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketRelaters.findAll", query = "SELECT t FROM TicketRelaters t"),
    @NamedQuery(name = "TicketRelaters.findById", query = "SELECT t FROM TicketRelaters t WHERE t.id = :id"),
    @NamedQuery(name = "TicketRelaters.findByStatusRead", query = "SELECT t FROM TicketRelaters t WHERE t.statusRead = :statusRead")})
public class TicketRelaters implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_read")
    private int statusRead;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employees employeeId;
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tickets ticketId;

    public TicketRelaters() {
    }

    public TicketRelaters(Integer id) {
        this.id = id;
    }

    public TicketRelaters(Integer id, int statusRead) {
        this.id = id;
        this.statusRead = statusRead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStatusRead() {
        return statusRead;
    }

    public void setStatusRead(int statusRead) {
        this.statusRead = statusRead;
    }

    public Employees getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employees employeeId) {
        this.employeeId = employeeId;
    }

    public Tickets getTicketId() {
        return ticketId;
    }

    public void setTicketId(Tickets ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketRelaters)) {
            return false;
        }
        TicketRelaters other = (TicketRelaters) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TicketRelaters[ id=" + id + " ]";
    }
    
}

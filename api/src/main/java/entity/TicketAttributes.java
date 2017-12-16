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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thanh
 */
@Entity
@Table(name = "ticket_attributes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketAttributes.findAll", query = "SELECT t FROM TicketAttributes t"),
    @NamedQuery(name = "TicketAttributes.findById", query = "SELECT t FROM TicketAttributes t WHERE t.id = :id"),
    @NamedQuery(name = "TicketAttributes.findByStatus", query = "SELECT t FROM TicketAttributes t WHERE t.status = :status"),
    @NamedQuery(name = "TicketAttributes.findByPriority", query = "SELECT t FROM TicketAttributes t WHERE t.priority = :priority"),
    @NamedQuery(name = "TicketAttributes.findByRating", query = "SELECT t FROM TicketAttributes t WHERE t.rating = :rating"),
    @NamedQuery(name = "TicketAttributes.findByReopened", query = "SELECT t FROM TicketAttributes t WHERE t.reopened = :reopened")})
public class TicketAttributes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 191)
    @Column(name = "status")
    private String status;
    @Size(max = 191)
    @Column(name = "priority")
    private String priority;
    @Size(max = 191)
    @Column(name = "rating")
    private String rating;
    @Size(max = 191)
    @Column(name = "reopened")
    private String reopened;
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tickets ticketId;

    public TicketAttributes() {
    }

    public TicketAttributes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReopened() {
        return reopened;
    }

    public void setReopened(String reopened) {
        this.reopened = reopened;
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
        if (!(object instanceof TicketAttributes)) {
            return false;
        }
        TicketAttributes other = (TicketAttributes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TicketAttributes[ id=" + id + " ]";
    }
    
}

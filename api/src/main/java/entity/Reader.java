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
@Table(name = "reader")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reader.findAll", query = "SELECT r FROM Reader r"),
    @NamedQuery(name = "Reader.findById", query = "SELECT r FROM Reader r WHERE r.id = :id"),
    @NamedQuery(name = "Reader.findByTicketId", query = "SELECT r FROM Reader r WHERE r.ticketId = :ticketId"),
    @NamedQuery(name = "Reader.findByEmployeeId", query = "SELECT r FROM Reader r WHERE r.employeeId = :employeeId"),
    @NamedQuery(name = "Reader.findByStatusRead", query = "SELECT r FROM Reader r WHERE r.statusRead = :statusRead")})
public class Reader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ticket_id")
    private int ticketId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employee_id")
    private int employeeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_read")
    private int statusRead;

    public Reader() {
    }

    public Reader(Integer id) {
        this.id = id;
    }

    public Reader(Integer id, int ticketId, int employeeId, int statusRead) {
        this.id = id;
        this.ticketId = ticketId;
        this.employeeId = employeeId;
        this.statusRead = statusRead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getStatusRead() {
        return statusRead;
    }

    public void setStatusRead(int statusRead) {
        this.statusRead = statusRead;
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
        if (!(object instanceof Reader)) {
            return false;
        }
        Reader other = (Reader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reader[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thanh
 */
@Entity
@Table(name = "ticket_thread")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketThread.findAll", query = "SELECT t FROM TicketThread t"),
    @NamedQuery(name = "TicketThread.findById", query = "SELECT t FROM TicketThread t WHERE t.id = :id"),
    @NamedQuery(name = "TicketThread.findByType", query = "SELECT t FROM TicketThread t WHERE t.type = :type"),
    @NamedQuery(name = "TicketThread.findByNote", query = "SELECT t FROM TicketThread t WHERE t.note = :note"),
    @NamedQuery(name = "TicketThread.findByCreatedAt", query = "SELECT t FROM TicketThread t WHERE t.createdAt = :createdAt"),
    @NamedQuery(name = "TicketThread.findByUpdatedAt", query = "SELECT t FROM TicketThread t WHERE t.updatedAt = :updatedAt")})
public class TicketThread implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Column(name = "type")
    private Short type;
    @Size(max = 191)
    @Column(name = "note")
    private String note;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employees employeeId;
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tickets ticketId;

    public TicketThread() {
    }

    public TicketThread(Integer id) {
        this.id = id;
    }

    public TicketThread(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
        if (!(object instanceof TicketThread)) {
            return false;
        }
        TicketThread other = (TicketThread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TicketThread[ id=" + id + " ]";
    }
    
}

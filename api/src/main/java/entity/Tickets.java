/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thanh
 */
@Entity
@Table(name = "tickets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tickets.findAll", query = "SELECT t FROM Tickets t"),
    @NamedQuery(name = "Tickets.findById", query = "SELECT t FROM Tickets t WHERE t.id = :id"),
    @NamedQuery(name = "Tickets.findBySubject", query = "SELECT t FROM Tickets t WHERE t.subject = :subject"),
    @NamedQuery(name = "Tickets.findByStatus", query = "SELECT t FROM Tickets t WHERE t.status = :status"),
    @NamedQuery(name = "Tickets.findByPriority", query = "SELECT t FROM Tickets t WHERE t.priority = :priority"),
    @NamedQuery(name = "Tickets.findByDeadline", query = "SELECT t FROM Tickets t WHERE t.deadline = :deadline"),
    @NamedQuery(name = "Tickets.findByRating", query = "SELECT t FROM Tickets t WHERE t.rating = :rating"),
    @NamedQuery(name = "Tickets.findByResolvedAt", query = "SELECT t FROM Tickets t WHERE t.resolvedAt = :resolvedAt"),
    @NamedQuery(name = "Tickets.findByClosedAt", query = "SELECT t FROM Tickets t WHERE t.closedAt = :closedAt"),
    @NamedQuery(name = "Tickets.findByCreatedAt", query = "SELECT t FROM Tickets t WHERE t.createdAt = :createdAt"),
    @NamedQuery(name = "Tickets.findByUpdatedAt", query = "SELECT t FROM Tickets t WHERE t.updatedAt = :updatedAt"),
    @NamedQuery(name = "Tickets.findByDeletedAt", query = "SELECT t FROM Tickets t WHERE t.deletedAt = :deletedAt")})
public class Tickets implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 191)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Size(max = 25)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priority")
    private short priority;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @Column(name = "rating")
    private Short rating;
    @Column(name = "resolved_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedAt;
    @Column(name = "closed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedAt;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employees assignedTo;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employees createdBy;
    @JoinColumn(name = "partcode", referencedColumnName = "partcode")
    @ManyToOne(optional = false)
    private PartIt partcode;
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teams teamId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketId")
    private Collection<TicketThread> ticketThreadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketId")
    private Collection<TicketRelaters> ticketRelatersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketId")
    private Collection<TicketAttributes> ticketAttributesCollection;

    public Tickets() {
    }

    public Tickets(Integer id) {
        this.id = id;
    }

    public Tickets(Integer id, String subject, String content, short priority, Date deadline) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.priority = priority;
        this.deadline = deadline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Employees getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employees assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Employees getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employees createdBy) {
        this.createdBy = createdBy;
    }

    public PartIt getPartcode() {
        return partcode;
    }

    public void setPartcode(PartIt partcode) {
        this.partcode = partcode;
    }

    public Teams getTeamId() {
        return teamId;
    }

    public void setTeamId(Teams teamId) {
        this.teamId = teamId;
    }

    @XmlTransient
    public Collection<TicketThread> getTicketThreadCollection() {
        return ticketThreadCollection;
    }

    public void setTicketThreadCollection(Collection<TicketThread> ticketThreadCollection) {
        this.ticketThreadCollection = ticketThreadCollection;
    }

    @XmlTransient
    public Collection<TicketRelaters> getTicketRelatersCollection() {
        return ticketRelatersCollection;
    }

    public void setTicketRelatersCollection(Collection<TicketRelaters> ticketRelatersCollection) {
        this.ticketRelatersCollection = ticketRelatersCollection;
    }

    @XmlTransient
    public Collection<TicketAttributes> getTicketAttributesCollection() {
        return ticketAttributesCollection;
    }

    public void setTicketAttributesCollection(Collection<TicketAttributes> ticketAttributesCollection) {
        this.ticketAttributesCollection = ticketAttributesCollection;
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
        if (!(object instanceof Tickets)) {
            return false;
        }
        Tickets other = (Tickets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Tickets[ id=" + id + " ]";
    }
    
}

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
@Table(name = "employees")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employees.findAll", query = "SELECT e FROM Employees e"),
    @NamedQuery(name = "Employees.findById", query = "SELECT e FROM Employees e WHERE e.id = :id"),
    @NamedQuery(name = "Employees.findByName", query = "SELECT e FROM Employees e WHERE e.name = :name"),
    @NamedQuery(name = "Employees.findByEmail", query = "SELECT e FROM Employees e WHERE e.email = :email"),
    @NamedQuery(name = "Employees.findByPassword", query = "SELECT e FROM Employees e WHERE e.password = :password"),
    @NamedQuery(name = "Employees.findByUrlImage", query = "SELECT e FROM Employees e WHERE e.urlImage = :urlImage"),
    @NamedQuery(name = "Employees.findByRememberToken", query = "SELECT e FROM Employees e WHERE e.rememberToken = :rememberToken"),
    @NamedQuery(name = "Employees.findByCreatedAt", query = "SELECT e FROM Employees e WHERE e.createdAt = :createdAt"),
    @NamedQuery(name = "Employees.findByUpdatedAt", query = "SELECT e FROM Employees e WHERE e.updatedAt = :updatedAt")})
public class Employees implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 191)
    @Column(name = "name")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 191)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 191)
    @Column(name = "password")
    private String password;
    @Size(max = 191)
    @Column(name = "url_image")
    private String urlImage;
    @Size(max = 100)
    @Column(name = "remember_token")
    private String rememberToken;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignedTo")
    private Collection<Tickets> ticketsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Collection<Tickets> ticketsCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<TicketThread> ticketThreadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<TicketRelaters> ticketRelatersCollection;
    @JoinColumn(name = "partcode", referencedColumnName = "partcode")
    @ManyToOne(optional = false)
    private PartIt partcode;
    @JoinColumn(name = "rolecode", referencedColumnName = "rolecode")
    @ManyToOne(optional = false)
    private Role rolecode;
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teams teamId;

    public Employees() {
    }

    public Employees(Integer id) {
        this.id = id;
    }

    public Employees(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
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

    @XmlTransient
    public Collection<Tickets> getTicketsCollection() {
        return ticketsCollection;
    }

    public void setTicketsCollection(Collection<Tickets> ticketsCollection) {
        this.ticketsCollection = ticketsCollection;
    }

    @XmlTransient
    public Collection<Tickets> getTicketsCollection1() {
        return ticketsCollection1;
    }

    public void setTicketsCollection1(Collection<Tickets> ticketsCollection1) {
        this.ticketsCollection1 = ticketsCollection1;
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

    public PartIt getPartcode() {
        return partcode;
    }

    public void setPartcode(PartIt partcode) {
        this.partcode = partcode;
    }

    public Role getRolecode() {
        return rolecode;
    }

    public void setRolecode(Role rolecode) {
        this.rolecode = rolecode;
    }

    public Teams getTeamId() {
        return teamId;
    }

    public void setTeamId(Teams teamId) {
        this.teamId = teamId;
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
        if (!(object instanceof Employees)) {
            return false;
        }
        Employees other = (Employees) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employees[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thanh
 */
@Entity
@Table(name = "part_it")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PartIt.findAll", query = "SELECT p FROM PartIt p"),
    @NamedQuery(name = "PartIt.findByPartcode", query = "SELECT p FROM PartIt p WHERE p.partcode = :partcode"),
    @NamedQuery(name = "PartIt.findByPartdesc", query = "SELECT p FROM PartIt p WHERE p.partdesc = :partdesc")})
public class PartIt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "partcode")
    private String partcode;
    @Size(max = 254)
    @Column(name = "partdesc")
    private String partdesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partcode")
    private Collection<Tickets> ticketsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partcode")
    private Collection<Permission> permissionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partcode")
    private Collection<Employees> employeesCollection;

    public PartIt() {
    }

    public PartIt(String partcode) {
        this.partcode = partcode;
    }

    public String getPartcode() {
        return partcode;
    }

    public void setPartcode(String partcode) {
        this.partcode = partcode;
    }

    public String getPartdesc() {
        return partdesc;
    }

    public void setPartdesc(String partdesc) {
        this.partdesc = partdesc;
    }

    @XmlTransient
    public Collection<Tickets> getTicketsCollection() {
        return ticketsCollection;
    }

    public void setTicketsCollection(Collection<Tickets> ticketsCollection) {
        this.ticketsCollection = ticketsCollection;
    }

    @XmlTransient
    public Collection<Permission> getPermissionCollection() {
        return permissionCollection;
    }

    public void setPermissionCollection(Collection<Permission> permissionCollection) {
        this.permissionCollection = permissionCollection;
    }

    @XmlTransient
    public Collection<Employees> getEmployeesCollection() {
        return employeesCollection;
    }

    public void setEmployeesCollection(Collection<Employees> employeesCollection) {
        this.employeesCollection = employeesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partcode != null ? partcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartIt)) {
            return false;
        }
        PartIt other = (PartIt) object;
        if ((this.partcode == null && other.partcode != null) || (this.partcode != null && !this.partcode.equals(other.partcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PartIt[ partcode=" + partcode + " ]";
    }
    
}

package entity;

import entity.Employees;
import entity.Permission;
import entity.Tickets;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(PartIt.class)
public class PartIt_ { 

    public static volatile CollectionAttribute<PartIt, Employees> employeesCollection;
    public static volatile SingularAttribute<PartIt, String> partcode;
    public static volatile CollectionAttribute<PartIt, Permission> permissionCollection;
    public static volatile CollectionAttribute<PartIt, Tickets> ticketsCollection;
    public static volatile SingularAttribute<PartIt, String> partdesc;

}
package entity;

import entity.Employees;
import entity.Permission;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-14T19:25:01")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile SingularAttribute<Role, String> rolecode;
    public static volatile CollectionAttribute<Role, Employees> employeesCollection;
    public static volatile SingularAttribute<Role, String> roledesc;
    public static volatile CollectionAttribute<Role, Permission> permissionCollection;

}
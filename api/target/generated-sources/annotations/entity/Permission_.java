package entity;

import entity.PartIt;
import entity.Role;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(Permission.class)
public class Permission_ { 

    public static volatile SingularAttribute<Permission, Role> rolecode;
    public static volatile SingularAttribute<Permission, PartIt> partcode;
    public static volatile SingularAttribute<Permission, String> permission;
    public static volatile SingularAttribute<Permission, Integer> id;

}
package entity;

import entity.Tickets;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-14T19:25:01")
@StaticMetamodel(Teams.class)
public class Teams_ { 

    public static volatile SingularAttribute<Teams, Date> createdAt;
    public static volatile SingularAttribute<Teams, String> name;
    public static volatile SingularAttribute<Teams, String> description;
    public static volatile SingularAttribute<Teams, Integer> id;
    public static volatile CollectionAttribute<Teams, Tickets> ticketsCollection;
    public static volatile SingularAttribute<Teams, Integer> leaderId;
    public static volatile SingularAttribute<Teams, Date> updatedAt;

}
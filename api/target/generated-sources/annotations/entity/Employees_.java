package entity;

import entity.PartIt;
import entity.Role;
import entity.Teams;
import entity.TicketRelaters;
import entity.TicketThread;
import entity.Tickets;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(Employees.class)
public class Employees_ { 

    public static volatile SingularAttribute<Employees, Role> rolecode;
    public static volatile CollectionAttribute<Employees, Tickets> ticketsCollection;
    public static volatile SingularAttribute<Employees, Date> createdAt;
    public static volatile SingularAttribute<Employees, String> password;
    public static volatile CollectionAttribute<Employees, Tickets> ticketsCollection1;
    public static volatile SingularAttribute<Employees, Teams> teamId;
    public static volatile SingularAttribute<Employees, String> name;
    public static volatile CollectionAttribute<Employees, TicketRelaters> ticketRelatersCollection;
    public static volatile SingularAttribute<Employees, PartIt> partcode;
    public static volatile SingularAttribute<Employees, Integer> id;
    public static volatile SingularAttribute<Employees, String> rememberToken;
    public static volatile SingularAttribute<Employees, String> email;
    public static volatile SingularAttribute<Employees, String> urlImage;
    public static volatile SingularAttribute<Employees, Date> updatedAt;
    public static volatile CollectionAttribute<Employees, TicketThread> ticketThreadCollection;

}
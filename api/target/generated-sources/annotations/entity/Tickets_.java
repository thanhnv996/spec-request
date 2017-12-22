package entity;

import entity.Employees;
import entity.PartIt;
import entity.Teams;
import entity.TicketAttributes;
import entity.TicketRelaters;
import entity.TicketThread;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(Tickets.class)
public class Tickets_ { 

    public static volatile SingularAttribute<Tickets, Date> resolvedAt;
    public static volatile CollectionAttribute<Tickets, TicketAttributes> ticketAttributesCollection;
    public static volatile SingularAttribute<Tickets, String> subject;
    public static volatile SingularAttribute<Tickets, Short> rating;
    public static volatile SingularAttribute<Tickets, Short> priority;
    public static volatile SingularAttribute<Tickets, String> content;
    public static volatile SingularAttribute<Tickets, Employees> assignedTo;
    public static volatile SingularAttribute<Tickets, Date> createdAt;
    public static volatile SingularAttribute<Tickets, Date> deletedAt;
    public static volatile SingularAttribute<Tickets, Employees> createdBy;
    public static volatile SingularAttribute<Tickets, Teams> teamId;
    public static volatile CollectionAttribute<Tickets, TicketRelaters> ticketRelatersCollection;
    public static volatile SingularAttribute<Tickets, PartIt> partcode;
    public static volatile SingularAttribute<Tickets, Integer> id;
    public static volatile SingularAttribute<Tickets, Date> deadline;
    public static volatile SingularAttribute<Tickets, Date> closedAt;
    public static volatile SingularAttribute<Tickets, String> status;
    public static volatile SingularAttribute<Tickets, Date> updatedAt;
    public static volatile CollectionAttribute<Tickets, TicketThread> ticketThreadCollection;

}
package entity;

import entity.Tickets;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(TicketAttributes.class)
public class TicketAttributes_ { 

    public static volatile SingularAttribute<TicketAttributes, String> reopened;
    public static volatile SingularAttribute<TicketAttributes, String> rating;
    public static volatile SingularAttribute<TicketAttributes, Integer> id;
    public static volatile SingularAttribute<TicketAttributes, String> priority;
    public static volatile SingularAttribute<TicketAttributes, Tickets> ticketId;
    public static volatile SingularAttribute<TicketAttributes, String> status;

}
package entity;

import entity.Employees;
import entity.TicketReadsPK;
import entity.Tickets;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-14T19:25:01")
@StaticMetamodel(TicketReads.class)
public class TicketReads_ { 

    public static volatile SingularAttribute<TicketReads, Tickets> tickets;
    public static volatile SingularAttribute<TicketReads, TicketReadsPK> ticketReadsPK;
    public static volatile SingularAttribute<TicketReads, Employees> employees;
    public static volatile SingularAttribute<TicketReads, Short> status;

}
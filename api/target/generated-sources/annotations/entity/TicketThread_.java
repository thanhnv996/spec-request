package entity;

import entity.Employees;
import entity.Tickets;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T17:45:58")
@StaticMetamodel(TicketThread.class)
public class TicketThread_ { 

    public static volatile SingularAttribute<TicketThread, String> note;
    public static volatile SingularAttribute<TicketThread, Date> createdAt;
    public static volatile SingularAttribute<TicketThread, Employees> employeeId;
    public static volatile SingularAttribute<TicketThread, Integer> id;
    public static volatile SingularAttribute<TicketThread, Short> type;
    public static volatile SingularAttribute<TicketThread, String> content;
    public static volatile SingularAttribute<TicketThread, Tickets> ticketId;
    public static volatile SingularAttribute<TicketThread, Date> updatedAt;

}
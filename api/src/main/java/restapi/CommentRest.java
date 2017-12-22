/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapi;

import config.Config;
import entity.Employees;
import entity.PartIt;
import entity.Teams;
import entity.TicketRelaters;
import entity.TicketThread;
import entity.Tickets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import manager.CommonBusiness;
import manager.SessionManager;
import z11.rs.exception.RestException;

/**
 *
 * @author thanh
 */
@Path("comment")
@Stateless
public class CommentRest {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = Config.PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    SessionManager sessionManager;
    @EJB
    CommonBusiness commonBusiness;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createCommentTicket(
            @FormParam("ticket_id") @NotNull short ticket_id,
            @FormParam("content") @NotNull String content,
            @FormParam("type") short type,
            @FormParam("note") String note,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = em.find(Employees.class, userId);

            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            if (commonBusiness.checkTicketRelater(employee, ticket)
                    || commonBusiness.checkPermissionBoolean(employee, Config.PMS_ALL)) {
                TicketThread comment = new TicketThread();
                comment.setContent(content);
                comment.setEmployeeId(employee);
                comment.setTicketId(ticket);
                comment.setCreatedAt(new Date());
                em.persist(comment);
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "OK");
            } else {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
            }

        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}

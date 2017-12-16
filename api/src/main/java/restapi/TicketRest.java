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
import entity.Tickets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import manager.CommonBusiness;
import manager.SessionManager;
import z11.rs.exception.RestException;

/**
 *
 * @author thanh
 */
@Path("ticket")
@Stateless
public class TicketRest {

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
    public Response createTicketByForm(
            @FormParam("subject") @NotNull String subject,
            @FormParam("priority") @NotNull short priority,
            @FormParam("deadline") @NotNull String deadline,
            @FormParam("partcode") @NotNull String partcode,
            @FormParam("assigned_to") @NotNull int assigned_to,
            @FormParam("stringListRelaterId") String stringListRelaterId,
            @FormParam("content") @NotNull String content,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees createdBy = em.find(Employees.class, userId);
            Employees assignedTo = commonBusiness.getUserById(assigned_to);
            PartIt partIt = commonBusiness.getPartByCode(partcode);
            Teams team = commonBusiness.getTeamsByMemberId(userId);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date deadlineDate;
            try {
                deadlineDate = df.parse(deadline);
            } catch (ParseException e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "DATE_CANT_FORMAT");
            }

            Tickets ticket = new Tickets(subject, content, priority, deadlineDate, assignedTo, createdBy, partIt, team);
            em.persist(ticket);

            try {
                List<String> listRelatersId = new ArrayList<>(Arrays.asList(stringListRelaterId.split(",")));
                try {
                    for (String idStr : listRelatersId) {
                        int relaterId = Integer.valueOf(idStr);
                        Employees relater = commonBusiness.getUserById(relaterId);
                        TicketRelaters relaters = new TicketRelaters();
                        relaters.setEmployeeId(relater);
                        relaters.setTicketId(ticket);
                        em.persist(relaters);
                        System.out.println("add relaters");
                    }
                } catch (Exception e) {
                    return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "INVALID LIST RELATER ID");
                }
            } finally {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "SUCCESS");
            }
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }

    }
}

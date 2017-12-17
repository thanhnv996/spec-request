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
import javax.ws.rs.PUT;
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

            Tickets ticket = new Tickets();
            ticket.setSubject(subject);
            ticket.setContent(content);
            ticket.setPriority(priority);
            ticket.setDeadline(deadlineDate);
            ticket.setAssignedTo(assignedTo);
            ticket.setCreatedBy(createdBy);
            ticket.setPartcode(partIt);
            ticket.setTeamId(team);

            ticket.setStatus(Config.STATUS_NEW);
            try {
                em.persist(ticket);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "INVALID PARAM");
            }

            /**
             * thêm người liên quan
             */
            try {
                List<String> listRelatersId = new ArrayList<>(Arrays.asList(stringListRelaterId.split(",")));
                try {
                    listRelatersId.add(assignedTo.getId().toString());
                    System.out.println("list relater id" + listRelatersId.toString());
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

    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateTicketByForm(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("subject") String subject,
            @FormParam("priority") short priority,
            @FormParam("reason_change_priority") String reason_change_priority,
            @FormParam("deadline") String deadline,
            @FormParam("partcode") String partcode,
            @FormParam("assigned_to") int assigned_to,
            @FormParam("content") String content,
            @FormParam("list_relater_id") String list_relater_id,
            @Context HttpServletRequest request) {
        try {

            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            try {
                commonBusiness.checkPermission(userId, Config.PMS_PUT_REQUEST_TEAM);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
            }

            try {
                commonBusiness.checkTicketInTeam(employee, ticket_id);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_IN_TEAM");
            }

            try {
                commonBusiness.checkStatusOfTicket(ticket_id);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "CANNOT_TICKET_CLOSED/RESOLVED/CANCELLED");
            }

            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            Date deadlineDate;
            try {
                List<String> myList = new ArrayList<String>(Arrays.asList(list_relater_id.split(",")));
                if (myList.size() > 0) {
                    commonBusiness.removeAllRelater(ticket_id);
                }
                /**
                 * Thay dổi người liên quan
                 */
                for (String relaterIDString : myList) {
                    int relaterID = Integer.valueOf(relaterIDString);
                    Employees relater = commonBusiness.getUserById(relaterID);
                    TicketRelaters tr = new TicketRelaters();
                    tr.setEmployeeId(relater);
                    tr.setTicketId(ticket);
                    em.persist(tr);
                }

                /**
                 * thay đổi deadline
                 */
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    deadlineDate = df.parse(deadline);
                    ticket.setDeadline(deadlineDate);
                } catch (ParseException e) {
                    return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "DEADLINE_INVALID");
                }
            } catch (Exception e) {
                System.out.println("");
            }

            if (subject != null) {
                ticket.setSubject(subject);
            }

            /**
             * Thay đổi mức độ ưu tiên
             */
            try {
                /**
                 * Nếu thay đổi priority bắt buộc phải có lý do
                 */
                if (reason_change_priority != null) {
                    /**
                     * Thêm nguyên nhân vào bảng comment
                     */
                    TicketThread comment = new TicketThread();
                    comment.setContent(reason_change_priority);
                    comment.setEmployeeId(employee);
                    comment.setTicketId(ticket);
                    em.persist(comment);

                    // Thay đổi mức độ ưu tiên
                    ticket.setPriority(priority);
                }

            } catch (NullPointerException e) {

            }

            /**
             * thay đổi assignto - người được giao việc
             */
            try {
                ticket.setAssignedTo(commonBusiness.getUserById(assigned_to));
            } catch (NullPointerException e) {

            }

            if (partcode != null) {
                ticket.setPartcode(commonBusiness.getPartByCode(partcode));
            }

            if (content != null) {
                ticket.setContent(content);
            }

            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.EXPECTATION_FAILED, "Error");
        }
    }
}

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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
            @FormParam("reason_change_deadline") String reason_change_deadline,
            @FormParam("partcode") String partcode,
            @FormParam("assigned_to") int assigned_to,
            @FormParam("content") String content,
            @FormParam("list_relater_id") String list_relater_id,
            @FormParam("rating") short rating,
            @FormParam("comment_rating") String comment_rating,
            @Context HttpServletRequest request) {
        try {

            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            /*
             Kiểm tra quyền thay đổi ticket
             */
            try {
                commonBusiness.checkPermission(userId, Config.PMS_PUT_REQUEST_TEAM);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
            }

            /*
             kiểm tra xem có ở trong team không
             */
            try {
                /*
                 nếu là toàn quyền công ty thì không cần check cái này 
                 */
                if (!commonBusiness.checkPermissionBoolean(userId, Config.PMS_ALL)) {
                    commonBusiness.checkTicketInTeam(employee, ticket_id);
                }

            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_IN_TEAM");
            }

            /**
             * kiểm tra trạng thái của công việc nếu là closed hoặc cancell thì
             * không thể thay đỏi attribute
             */
            try {
                commonBusiness.checkStatusOfTicket(ticket_id);
            } catch (Exception e) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "CANNOT_TICKET_CLOSED/RESOLVED/CANCELLED");
            }

            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            Date deadlineDate;
            try {
                List<String> myList = new ArrayList<String>(Arrays.asList(list_relater_id.split(",")));
                /**
                 * Xóa các người liên quan cũ
                 */
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
                 * thay đổi deadline bắt buộc phải có lý do
                 */
                if (reason_change_deadline != null) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        deadlineDate = df.parse(deadline);
                        /**
                         * Thêm nguyên nhân vào bảng comment
                         */
                        TicketThread comment = new TicketThread();
                        String ratingstr = "\nThay đổi deadline : từ" + df.format(ticket.getDeadline())
                                + " -> " + df.format(deadlineDate) + "\n";
                        comment.setContent(ratingstr + comment_rating);
                        comment.setEmployeeId(employee);
                        comment.setTicketId(ticket);
                        em.persist(comment);

                        // Thay đổi deadline
                        ticket.setDeadline(deadlineDate);

                    } catch (ParseException e) {
                        return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "DEADLINE_INVALID");
                    }
                } else {
//                    throw new Exception("REQUIRED_REASON_CHANGE_DEADLINE");
                    return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "REQUIRED_REASON_CHANGE_DEADLINE");
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
                    String historyChangePriority = "\nThay đổi mức độ ưu tiên : "
                            + commonBusiness.convertPriorityToString(ticket.getPriority()) + ">"
                            + commonBusiness.convertPriorityToString(priority) + "\n";
                    comment.setContent(historyChangePriority + reason_change_priority);
                    comment.setEmployeeId(employee);
                    comment.setTicketId(ticket);
                    em.persist(comment);

                    //Kiểm tra độ ưu tiên có nằm từ 1-4 không
                    commonBusiness.checkPriority(priority);
                    // Thay đổi mức độ ưu tiên
                    ticket.setPriority(priority);
                }

            } catch (NullPointerException e) {

            }

            /**
             * thay đổi assignto - người được giao việc
             */
            try {
                // Xóa người assigned cũ
                commonBusiness.removeAssignerInRelater(ticket.getAssignedTo().getId(), ticket.getId());
                // thêm người assigned mới
                TicketRelaters tr = new TicketRelaters();
                tr.setEmployeeId(commonBusiness.getUserById(assigned_to));
                tr.setTicketId(ticket);
                em.persist(tr);
                ticket.setAssignedTo(commonBusiness.getUserById(assigned_to));
            } catch (NullPointerException e) {

            }

            if (partcode != null) {
                ticket.setPartcode(commonBusiness.getPartByCode(partcode));
            }

            if (content != null) {
                ticket.setContent(content);
            }

            /**
             * đánh giá
             */
            if (comment_rating != null) {
                commonBusiness.checkRating(rating);
                try {
                    ticket.setRating(rating);
                    /**
                     * Thêm bình luận vào bảng comment
                     */
                    TicketThread comment = new TicketThread();
                    String ratingstr = "\nĐánh giá : "
                            + commonBusiness.convertRatingToString(rating) + "\n";
                    comment.setContent(ratingstr + comment_rating);
                    comment.setEmployeeId(employee);
                    comment.setTicketId(ticket);
                    em.persist(comment);
                } catch (Exception e) {

                }
            } else {
                throw new Exception("REQUIRED_COMMENT_RATING");
            }

            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("changestatus")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response changeStatusTicket(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("status") String statusdesc,
            @Context HttpServletRequest request) {
        try {

            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            commonBusiness.checkStatusOfTicketToChangeStatus(ticket_id);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            boolean changed = false;
            /**
             * Thay doi trang thai voi nguoi co toan quyen cong ty
             */
            if (commonBusiness.checkPermissionBoolean(employee, Config.PMS_ALL)) {
                System.out.println("toàn quyền");
                if (ticket.getStatus().equals(Config.STATUS_NEW)
                        && (statusdesc.equals(Config.STATUS_INPROGRESS)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_INPROGRESS)
                        && (statusdesc.equals(Config.STATUS_RESOLVED)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_RESOLVED)
                        && (statusdesc.equals(Config.STATUS_FEEDBACK)
                        || statusdesc.equals(Config.STATUS_CLOSED)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_FEEDBACK)
                        && (statusdesc.equals(Config.STATUS_INPROGRESS)
                        || statusdesc.equals(Config.STATUS_CLOSED)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
            } /**
             * Thay doi trang thai voi nguoi co quyen team
             */
            else if (commonBusiness.checkPermissionBoolean(employee, Config.PMS_PUT_REQUEST_TEAM)
                    && commonBusiness.checkTicketInTeamBoolean(employee, ticket_id)) {
                System.out.println("quyền team");
                if (ticket.getStatus().equals(Config.STATUS_NEW)
                        && statusdesc.equals(Config.STATUS_INPROGRESS)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_INPROGRESS)
                        && statusdesc.equals(Config.STATUS_RESOLVED)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_RESOLVED)
                        && statusdesc.equals(Config.STATUS_FEEDBACK)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_FEEDBACK)
                        && statusdesc.equals(Config.STATUS_INPROGRESS)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
            } /**
             * Thay doi trang thai voi nguoi duoc assign
             */
            else if (employee.equals(ticket.getAssignedTo())) {
                System.out.println("được assgin");
                if (ticket.getStatus().equals(Config.STATUS_NEW)
                        && statusdesc.equals(Config.STATUS_INPROGRESS)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_INPROGRESS)
                        && statusdesc.equals(Config.STATUS_RESOLVED)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_FEEDBACK)
                        && statusdesc.equals(Config.STATUS_INPROGRESS)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_FEEDBACK)
                        && statusdesc.equals(Config.STATUS_INPROGRESS)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
            } /**
             * Thay doi trang thai voi nguoi tao
             */
            else if (employee.equals(ticket.getCreatedBy())) {
                System.out.println("người tạo");
                if (ticket.getStatus().equals(Config.STATUS_NEW)
                        && statusdesc.equals(Config.STATUS_CANCELLED)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_INPROGRESS)
                        && statusdesc.equals(Config.STATUS_CANCELLED)) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_RESOLVED)
                        && (statusdesc.equals(Config.STATUS_FEEDBACK)
                        || statusdesc.equals(Config.STATUS_CLOSED)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
                if (ticket.getStatus().equals(Config.STATUS_FEEDBACK)
                        && (statusdesc.equals(Config.STATUS_CLOSED)
                        || statusdesc.equals(Config.STATUS_CANCELLED))) {
                    ticket.setStatus(statusdesc);
                    changed = true;
                }
            } else {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "METHOD_NOT_ALLOWED,NOT_PERMISSION");
            }

            if (changed) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "SUCCESS");
            } else {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "CHANGE_STATUS_INCORRECT");
            }
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    /**
     * Công việc tôi yêu cầu
     *
     * @param request
     * @return
     */
    @GET
    @Path("me/created")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketMeCreate(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            GenericEntity<List<Tickets>> entity = commonBusiness.getTicketMeCreate(employee);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }

    /**
     * Công việc tôi liên quan
     */
    @GET
    @Path("me/related")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketMeRelate(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            GenericEntity<List<TicketRelaters>> entity = commonBusiness.getTicketMeRelate(employee);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }

    /**
     * Công việc tôi được giao
     *
     * @param request
     * @return
     */
    @GET
    @Path("me/requested")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketForMe(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            GenericEntity<List<Tickets>> entity = commonBusiness.getAllAssignedTicket(employee);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }

    /**
     * Công việc của team
     *
     * @param request
     * @return
     */
    @GET
    @Path("team")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketForTeam(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            try {
                commonBusiness.checkPermission(employee.getId(), Config.PMS_GET_REQUEST_TEAM);
            } catch (Exception ex) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
            }

            GenericEntity<List<Tickets>> entity = commonBusiness.getTicketOfTeam(employee.getTeamId().getId());

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }

    /**
     * Công việc của bộ phận IT
     *
     * @param request
     * @return
     */
    @GET
    @Path("part/{partcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketPartIT(
            @PathParam("partcode") String partcode,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            try {
                commonBusiness.checkPermission(employee.getId(), Config.PMS_GET_REQUEST_PART);
            } catch (Exception ex) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.METHOD_NOT_ALLOWED, "NOT_PERMISSION");
            }

            GenericEntity<List<Tickets>> entity = commonBusiness.getTicketOfPartIt(partcode);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }
    
    /**
     * Công việc của bộ phận IT
     *
     * @param request
     * @return
     */
    @GET
    @Path("part")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketPart(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            try {
                commonBusiness.checkPermission(employee.getId(), Config.PMS_GET_REQUEST_PART);
            } catch (Exception ex) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
            }

            GenericEntity<List<Tickets>> entity = commonBusiness.getTicketOfPartIt(null);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }
    }
}

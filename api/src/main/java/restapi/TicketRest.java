/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapi;

import config.Config;
import entity.Employees;
import entity.PartIt;
import entity.Reader;
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
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
            }
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        }

    }

    @PUT
    @Path("update/sub-attribute")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateSubAttribute(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("subject") String subject,      
            @FormParam("partcode") String partcode,    
            @FormParam("content") String content,
            @Context HttpServletRequest request) {
        try {

            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            if (subject != null) {
                ticket.setSubject(subject);
            }
            

            if (partcode != null) {
                if (partcode.contains("hanoi") || partcode.contains("danang")) {
                    ticket.setPartcode(commonBusiness.getPartByCode(partcode));
                } else {
                    return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "code bộ phận IT không hợp lệ");
                }
            }

            if (content != null) {
                ticket.setContent(content);
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
    @Path("update/relater")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateRelater(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("list_relater_id") String list_relater_id,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
          if (list_relater_id != null) {
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

            } else {
                // System.out.println("không thực hiện thay đổi người liên quan");
               return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "không thực hiện thay đổi người liên quan");
            }
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("update/assignto")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateAssignto(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("assigned_to") int assigned_to,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
           /**
             * thay đổi assignto - người được giao việc
             */
            // Xóa người assigned cũ
            if (assigned_to > 0) {
                commonBusiness.removeAssignerInRelater(ticket.getAssignedTo().getId(), ticket.getId());
                // thêm người assigned mới
                TicketRelaters tr = new TicketRelaters();
                tr.setEmployeeId(commonBusiness.getUserById(assigned_to));
                tr.setTicketId(ticket);
                em.persist(tr);
                ticket.setAssignedTo(commonBusiness.getUserById(assigned_to));
            } else if (assigned_to == 0) {
                System.out.println("Không thực hiện thay đổi assign");
            } else {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "ID assign không hợp lệ");
            }
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("update/priority")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updatePriority(
            @FormParam("ticket_id") @NotNull int ticket_id,
             @FormParam("priority") short priority,
            @FormParam("reason_change_priority") String reason_change_priority,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
           /**
             * Thay đổi mức độ ưu tiên bắt buộc phải có lý do
             */
            if (reason_change_priority != null && priority > 0) {
                /**
                 * Thêm nguyên nhân vào bảng comment
                 */
                TicketThread reason = new TicketThread();
                String historyChangePriority = "\nThay đổi mức độ ưu tiên : "
                        + commonBusiness.convertPriorityToString(ticket.getPriority()) + ">"
                        + commonBusiness.convertPriorityToString(priority) + "\n";
                reason.setContent(historyChangePriority + reason_change_priority);
                reason.setEmployeeId(employee);
                reason.setTicketId(ticket);
                reason.setCreatedAt(new Date());
                em.persist(reason);

                //Kiểm tra độ ưu tiên có nằm từ 1-4 không
                commonBusiness.checkPriority(priority);
                // Thay đổi mức độ ưu tiên
                ticket.setPriority(priority);
            } else if (reason_change_priority == null && priority > 0) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "REQUIRED_REASON_CHANGE_PRIORITY");
            } else {
                // System.out.println("Không thực hiện thay đổi mức độ ưu tiên");
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "Không thực hiện thay đổi mức độ ưu tiên");
            }
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("update/deadline")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateDeadline(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("deadline") String deadline,
            @FormParam("reason_change_deadline") String reason_change_deadline,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
           /**
             * thay đổi deadline bắt buộc phải có lý do
             */
            Date deadlineDate;
            if (reason_change_deadline != null && deadline != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    deadlineDate = df.parse(deadline);
                    /**
                     * Thêm nguyên nhân vào bảng comment
                     */
                    TicketThread comment = new TicketThread();
                    String ratingstr = "\nThay đổi deadline : từ" + df.format(ticket.getDeadline())
                            + " -> " + df.format(deadlineDate) + "\n";
                    comment.setContent(ratingstr + reason_change_deadline);
                    comment.setEmployeeId(employee);
                    comment.setTicketId(ticket);
                    comment.setCreatedAt(new Date());
                    em.persist(comment);

                    // Thay đổi deadline
                    ticket.setDeadline(deadlineDate);

                } catch (ParseException e) {
                    return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "DEADLINE_INVALID");
                }
            } else if (reason_change_deadline == null && deadline != null) {
//                    throw new Exception("REQUIRED_REASON_CHANGE_DEADLINE");
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "REQUIRED_REASON_CHANGE_DEADLINE");
            } else {
                // System.out.println("không thực hiện thay đổi deadline");
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "không thực hiện thay đổi deadline");
            }
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("update/rating")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateRating(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("rating") short rating,
            @FormParam("comment_rating") String comment_rating,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            commonBusiness.checkAllConditionToUpdate(userId,ticket_id);
            
            Employees employee = commonBusiness.getUserById(userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
            /**
             * đánh giá
             */
            if (comment_rating != null && (rating == 1 || rating == 0)) {
                commonBusiness.checkRating(rating);
                try {
                    ticket.setRating(rating);
                    /**
                     * Thêm bình luận vào bảng comment
                     */
                    TicketThread rating_comment = new TicketThread();
                    String ratingstr = "\nĐánh giá : "
                            + commonBusiness.convertRatingToString(rating) + "\n";
                    rating_comment.setContent(ratingstr + comment_rating);
                    rating_comment.setEmployeeId(employee);
                    rating_comment.setTicketId(ticket);
                    rating_comment.setCreatedAt(new Date());
                    em.persist(rating_comment);
                } catch (Exception e) {
                    // System.out.println(e.getMessage());
                     return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "e.getMessage()");
                }
            } else if (comment_rating != null) {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "REQUIRED_COMMENT_RATING");
            } else {
                // System.out.println("Không thực hiện đánh giá");
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "Không thực hiện đánh giá");
            }
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.OK, "SUCCESS");
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @PUT
    @Path("update/status")
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
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "NOT_PERMISSION");
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

    /**
     * Đánh dấu hoặc bỏ đánh dấu
     *
     * @param request
     * @return
     */
    @POST
    @Path("mark")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response markReadTicket(
            @FormParam("ticket_id") @NotNull int ticket_id,
            @FormParam("status") @NotNull short status,
            @Context HttpServletRequest request) throws Exception {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            commonBusiness.markRead(ticket_id, userId, status);

            return Response.status(Response.Status.OK).entity("OK").build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Danh sách các ticket đã đọc
     *
     * @param request
     * @return
     */
    @GET
    @Path("me/read")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicketMeRead(
            @Context HttpServletRequest request) throws Exception {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = commonBusiness.getUserById(userId);

            GenericEntity<List<Reader>> entity = commonBusiness.getReaderByEmployeeId(userId);

            return Response.status(Response.Status.OK).entity(entity).build();
        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Get comment của một ticket nào đó
     *
     * @param ticket_id
     * @param request
     * @return
     */
    @GET
    @Path("{ticket_id}/comment")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getCommentTicket(
            @PathParam("ticket_id") @NotNull short ticket_id,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = em.find(Employees.class, userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            if (commonBusiness.checkTicketRelater(employee, ticket)
                    || commonBusiness.checkPermissionBoolean(employee, Config.PMS_ALL)) {
                List<TicketThread> listTicketThread = commonBusiness.getComment(ticket_id);
                for (int i = 0; i < listTicketThread.size(); i++) {
                    listTicketThread.get(i).setCreatedAt(listTicketThread.get(i).getCreatedAt());
                    System.out.println(listTicketThread.get(i).toString());
                }
                GenericEntity<List<TicketThread>> entity = new GenericEntity<List<TicketThread>>(listTicketThread) {
                };
                return Response.status(Response.Status.OK).entity(entity).build();
            } else {
                return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "YOU NOT RELATE THIS TICKET!");
            }

        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "YOU NOT RELATE THIS TICKET!!");
        }
    }

    /**
     * Get một ticket nào đó
     *
     * @param ticket_id
     * @param request
     * @return
     */
    @GET
    @Path("id/{ticket_id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getTicket(
            @PathParam("ticket_id") @NotNull short ticket_id,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = em.find(Employees.class, userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);

            return Response.status(Response.Status.OK).entity(ticket).build();

        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "YOU NOT RELATE THIS TICKET!!");
        }
    }
    
    /**
     * Get những người liên quan tới một ticket nào đó
     *
     * @param ticket_id
     * @param request
     * @return
     */
    @GET
    @Path("id/{ticket_id}/relaters")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRelaters(
            @PathParam("ticket_id") @NotNull short ticket_id,
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = em.find(Employees.class, userId);
            Tickets ticket = commonBusiness.getTicketById(ticket_id);
            GenericEntity<List<TicketRelaters>> entity = commonBusiness.getRelaterByTicketId(ticket_id);
            return Response.status(Response.Status.OK).entity(entity).build();

        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, "YOU NOT RELATE THIS TICKET!!");
        }
    }
}

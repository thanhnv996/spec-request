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
@Path("employee")
@Stateless
public class EmployeeRest {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = Config.PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    SessionManager sessionManager;
    @EJB
    CommonBusiness commonBusiness;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createCommentTicket(
            @Context HttpServletRequest request) {
        try {
            Integer userId = sessionManager.getSessionUserId(request);
            Employees employee = em.find(Employees.class, userId);

            GenericEntity<List<Employees>> entity = commonBusiness.getAllEmployee();

            return Response.status(Response.Status.OK).entity(entity).build();

        } catch (RestException restException) {
            return restException.makeHttpResponse();
        } catch (Exception e) {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}

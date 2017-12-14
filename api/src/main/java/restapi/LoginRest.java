/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapi;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import manager.SessionManager;

import config.Config;
import entity.Employees;
import entity.Employees_;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import manager.CommonBusiness;

/**
 * REST Web Service
 *
 * @author vietduc
 */
@Path("login")
@Stateless
public class LoginRest {

    @PersistenceContext(unitName = Config.PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    SessionManager sessionManager;
    @EJB
    CommonBusiness commonBusiness;

    /**
     * Creates a new instance of LoginREST
     */
    public LoginRest() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String sessionId = z11.rs.auth.AuthUtil.getAuthorization(request, response);
        return Response.status(Response.Status.OK).entity(sessionId).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response postLogin(
            @FormParam("login_id") @NotNull String login_id,
            @FormParam("password") @NotNull String pass,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {
//        return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "LOGGED");
        String token = z11.rs.auth.AuthUtil.getAuthorization(request, response);
        if (sessionManager.checkSession(token)) {
            sessionManager.removeSession(token);
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Employees> u = cq.from(Employees.class);
        cq.select(u);
        cq.where(
                cb.and(cb.equal(u.get(Employees_.email), login_id), cb.equal(u.get(Employees_.password), pass))
        );

        List<Employees> listUser = em.createQuery(cq).getResultList();

        if (listUser.size() > 0) {
            Employees userObj = listUser.get(0);

            sessionManager.addSession(token, userObj.getId());
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.ACCEPTED, "LOGGED");

        } else {
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.NOT_ACCEPTABLE, "Wrong user or password!");
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(@Context HttpServletRequest request) {
        String token = z11.rs.auth.AuthUtil.checkAuthorization(request);
        if (sessionManager.checkSession(token)) {
            sessionManager.removeSession(token);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.GONE, "LOGOUT");
        }
        return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.EXPECTATION_FAILED, "EXPECTATION_FAILED");
    }

    @DELETE
    @Path("all")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logoutAll(@Context HttpServletRequest request) {
        String token = z11.rs.auth.AuthUtil.checkAuthorization(request);
        if (sessionManager.checkSession(token)) {
            sessionManager.removeAllSession(token);
            return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.GONE, "LOGOUT ALL");
        }
        return z11.rs.auth.AuthUtil.makeTextResponse(Response.Status.EXPECTATION_FAILED, "EXPECTATION_FAILED");
    }

}

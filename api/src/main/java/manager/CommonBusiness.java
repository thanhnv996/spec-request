/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import config.Config;
import entity.Employees;
import entity.PartIt;
import entity.Teams;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import z11.rs.exception.ExistException;
import z11.rs.exception.LowBalanceException;
import z11.rs.exception.NoPermissionException;
import z11.rs.exception.NotFoundException;
import z11.rs.exception.RestException;
import z11.rs.exception.UnauthorizedException;
import z11.rs.exception.RequestParamNotValidException;

/**
 *
 * @author vietduc
 */
@Singleton
public class CommonBusiness {
    @PersistenceContext(unitName = Config.PERSISTENCE_UNIT_NAME) 
    private EntityManager em;
    
    @EJB
    SessionManager sessionManager;

    public CommonBusiness() {
        z11.F_ile.createFolder(Config.PROFILE_PICTURE_DIRECTORY);
    }

    public <T> List<T> getItems(Class<T> entityClass) throws RequestParamNotValidException {
        return getItems(entityClass, 0, Config.MAX_SEARCH_RESUTL);
    }
    
    public <T> List<T> getItems(Class<T> entityClass, int from, int to) throws RequestParamNotValidException {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        return getObjectRange(cq, from, to);
    }
    
    public <T> List<T> getObjectRange(CriteriaQuery cq, int from, int to) throws RequestParamNotValidException {
        to = Math.min(from + Config.MAX_SEARCH_RESUTL, to);
        if (from > to || to > from + Config.MAX_SEARCH_RESUTL) {
            throw new RequestParamNotValidException("from/to parameter not valid!");
        }
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(to - from + 1);
        q.setFirstResult(from);
        return q.getResultList();
    }
    
    public<T> int getCount(Class<T> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Employees getUserById(int userid) throws NotFoundException {
        Employees user = em.find(Employees.class, userid);
        if (user == null) {
            throw new NotFoundException("Not found userid:" + userid);
        }
        return user;
    }
    
    public PartIt getPartByCode(String code) throws NotFoundException {
        PartIt partIt = em.find(PartIt.class, code);
        if (partIt == null) {
            throw new NotFoundException("Not found userid:" + partIt);
        }
        return partIt;
    }
    
    public Teams getTeamsById(int id) throws NotFoundException {
        Teams team = em.find(Teams.class,id);
        if (team == null) {
            throw new NotFoundException("Not found team with teamid :" + id);
        }
        return team;
    }
    
    public Teams getTeamsByMemberId(int memberid) throws NotFoundException {
        Employees member = em.find(Employees.class, memberid);
        Teams team = member.getTeamId();
        if (team == null) {
            throw new NotFoundException("Not found team with teamid :" + team.getId());
        }
        return team;
    }
}

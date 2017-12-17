/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import config.Config;
import entity.Employees;
import entity.PartIt;
import entity.Permission;
import entity.Permission_;
import entity.Role;
import entity.Teams;
import entity.TicketRelaters;
import entity.TicketRelaters_;
import entity.Tickets;
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

    public <T> int getCount(Class<T> entityClass) {
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
        Teams team = em.find(Teams.class, id);
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

    public Tickets getTicketById(int userid) throws NotFoundException {
        Tickets tk = em.find(Tickets.class, userid);
        if (tk == null) {
            throw new NotFoundException("Not found userid:" + tk);
        }
        return tk;
    }

    public void checkPermission(int userId, String pms) throws Exception {
        Employees e = getUserById(userId);
        Role role = e.getRolecode();

        PartIt part = e.getPartcode();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Permission> root = cq.from(Permission.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(Permission_.partcode), part),
                        cb.equal(root.get(Permission_.rolecode), role),
                        cb.equal(root.get(Permission_.permission), pms)
                )
        );
        List<Permission> listPermission = em.createQuery(cq).getResultList();
        if (listPermission.isEmpty()) {
            throw new Exception("NOT PERMISSION");
        }
    }

    /**
     * Xóa mối quan hệ người liên quan đến ticket
     *
     * @param ticket_id
     * @throws NotFoundException
     */
    public void removeAllRelater(int ticket_id) throws NotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
        cq.select(root);
        cq.where(
                cb.equal(root.get(TicketRelaters_.ticketId), getTicketById(ticket_id))
        );
        List<TicketRelaters> listTR = em.createQuery(cq).getResultList();
        for (TicketRelaters tr : listTR) {
            em.remove(tr);
        }
    }

    public void checkTicketInTeam(Employees emp, int ticket_id) throws NotFoundException, Exception {
        Teams teamOfEmployee = emp.getTeamId();

        Tickets ticketUpdate = getTicketById(ticket_id);

        if (ticketUpdate.getTeamId().equals(teamOfEmployee)) {
            return;
        } else {
            throw new Exception("TICKET_NOT_IN_TEAM");
        }
    }
//
//    public Status getStatusByDesc(String desc) throws NotFoundException {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//        Root<Status> root = cq.from(Status.class);
//        cq.select(root);
//        cq.where(
//                cb.equal(root.get(Status_.description), desc)
//        );
//        List<Status> listStatus = em.createQuery(cq).getResultList();
//        if (listStatus.size() > 0) {
//            return listStatus.get(0);
//        } else {
//            throw new NotFoundException("CANT_FIND_STATUS");
//        }
//    }
//

    /**
     * Kiểm tra xem Ticket có thể thay đổi không nếu ở trạng thái resolved /
     * closed / cancelled thì không thể thay đổi được
     *
     * @param ticket_id
     * @throws NotFoundException
     * @throws Exception
     */
    public void checkStatusOfTicket(int ticket_id) throws NotFoundException, Exception {
        Tickets ticket = getTicketById(ticket_id);
        String statusTicket = ticket.getStatus();
        if (statusTicket.equals(Config.STATUS_RESOLVED)
                || statusTicket.equals(Config.STATUS_CLOSED)
                || statusTicket.equals(Config.STATUS_CANCELLED)) {
            throw new Exception("TICKET_CAN'T_CHANGE");
        }
    }
//
////    public void checkStatusOfTicketToChangeStatus(int ticket_id) throws NotFoundException, Exception {
////        Tickets ticket = getTicketById(ticket_id);
////        String statusTicket = ticket.getStatus().getDescription();
////        if (statusTicket.equals(Config.STATUS_CLOSED)
////                || statusTicket.equals(Config.STATUS_CANCELLED)) {
////            throw new Exception("TICKET_CAN'T_CHANGE_STATUS");
////        }
////    }

    /**
     * Kiểm tra quyền của user .
     *
     * @param e : nhân viên cần kiểm tra quyền
     * @param pms : quyền
     * @return true/false
     * @throws Exception
     */
    public boolean checkPermissionBoolean(Employees e, String pms) throws Exception {
        Role role = e.getRolecode();

        PartIt part = e.getPartcode();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Permission> root = cq.from(Permission.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(Permission_.partcode), part),
                        cb.equal(root.get(Permission_.rolecode), role),
                        cb.equal(root.get(Permission_.permission), pms)
                )
        );
        List<Permission> listPermission = em.createQuery(cq).getResultList();
        if (listPermission.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Kiểm tra xem Ticket này có ở trong team của nhân viên không
     *
     * @param emp Nhân viên trong team
     * @param ticket_id id ticket
     * @return true/false
     * @throws NotFoundException
     * @throws Exception
     */
    public boolean checkTicketInTeamBoolean(Employees emp, int ticket_id) throws NotFoundException, Exception {
        Teams teamOfEmployee = emp.getTeamId();

        Tickets ticketUpdate = getTicketById(ticket_id);

        if (teamOfEmployee.getId().equals(ticketUpdate.getTeamId())) {
            return true;
        } else {
            return false;
        }
    }
//    
//    public List<TicketThread> getComment(int ticket_id) throws NotFoundException{
//        Tickets ticket = getTicketById(ticket_id);
//        
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//        Root<TicketThread> root = cq.from(TicketThread.class);
//        cq.select(root);
//        cq.where(
//                cb.and(
//                        cb.equal(root.get(TicketThread_.ticketId),ticket)
//                )
//        );
//        List<TicketThread> list = em.createQuery(cq).getResultList();
//        return list;
//    }
//    
//    public TicketReads getTicketReadByTicketId(int ticket_id) throws NotFoundException{
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//        Root<TicketReads> root = cq.from(TicketReads.class);
//        cq.select(root);
//        cq.where(
//                cb.and(
////                        cb.equal(root.get(TicketReads_.ticketId),getTicketById(ticket_id))
//                )
//        );
//        List<TicketReads> list = em.createQuery(cq).getResultList();
//        if(list.size()>0){
//            return list.get(0);
//        }
//        else{
//            throw new NotFoundException("NOT FOUND TICKET IN TICKET MARK READ");
//        }
//    }
//    
//    public boolean checkTicketRelater(Employees relater , Tickets ticket){
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
//        cq.select(root);
//        cq.where(
//                cb.and(
//                        cb.equal(root.get(TicketRelaters_.employeeId),relater),
//                        cb.equal(root.get(TicketRelaters_.ticketId),ticket)
//                )
//        );
//        List<TicketReads> list = em.createQuery(cq).getResultList();
//        if(list.size()>0){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
}

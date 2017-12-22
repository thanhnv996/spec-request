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
import entity.Reader;
import entity.Reader_;
import entity.Role;
import entity.Teams;
import entity.TicketRelaters;
import entity.TicketRelaters_;
import entity.TicketThread;
import entity.TicketThread_;
import entity.Tickets;
import entity.Tickets_;
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
            throw new NotFoundException("Not found partIt:" + code);
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

    public boolean checkPermissionBoolean(int userId, String pms) throws NotFoundException {
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
            return false;
        }
        return true;
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

    /**
     * Xóa mối quan hệ người được giao việc với ticket
     *
     * @param ticket_id
     * @throws NotFoundException
     */
    public void removeAssignerInRelater(int assignerId, int ticket_id) throws NotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(TicketRelaters_.ticketId), getTicketById(ticket_id)),
                        cb.equal(root.get(TicketRelaters_.employeeId), getUserById(assignerId))
                )
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

    public void checkStatusOfTicketToChangeStatus(int ticket_id) throws NotFoundException, Exception {
        Tickets ticket = getTicketById(ticket_id);
        String statusTicket = ticket.getStatus();
        if (statusTicket.equals(Config.STATUS_CLOSED)
                || statusTicket.equals(Config.STATUS_CANCELLED)) {
            throw new Exception("TICKET_CAN'T_CHANGE_STATUS");
        }
    }

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

    public List<TicketThread> getComment(int ticket_id) throws NotFoundException {
        Tickets ticket = getTicketById(ticket_id);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketThread> root = cq.from(TicketThread.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(TicketThread_.ticketId), ticket)
                )
        );
        List<TicketThread> list = em.createQuery(cq).getResultList();
        return list;
    }
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

    public boolean checkTicketRelater(Employees relater, Tickets ticket) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(TicketRelaters_.employeeId), relater),
                        cb.equal(root.get(TicketRelaters_.ticketId), ticket)
                )
        );
        List<TicketRelaters> list = em.createQuery(cq).getResultList();
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Map từ mức độ ưu tiển kiểu short sang string
     */
    public String convertPriorityToString(short priority) throws Exception {
        if (priority == 1) {
            return Config.PRIORITY_1;
        } else if (priority == 2) {
            return Config.PRIORITY_2;
        } else if (priority == 3) {
            return Config.PRIORITY_3;
        } else if (priority == 4) {
            return Config.PRIORITY_4;
        } else {
            throw new Exception("NOT_EXIST_PRIORITY :" + priority);
        }
    }

    /**
     * Map từ mức độ ưu tiển kiểu short sang string
     */
    public String convertRatingToString(short rating) throws Exception {
        if (rating == 0) {
            return Config.RATING_0;
        } else if (rating == 1) {
            return Config.RATING_1;
        } else {
            throw new Exception("NOT_EXIST_PRIORITY :" + rating);
        }
    }

    /**
     * Kiểm tra rating có phải là 0 hoặc 1 không
     */
    public void checkRating(short rating) throws Exception {
        if (rating == 0 || rating == 1) {
            return;
        } else {
            throw new Exception("RATING MUST IS 0(GOOD) OR 1(BAD) ");
        }
    }

    /**
     * Kiểm tra priority có phải là 0 hoặc 1 không
     */
    public void checkPriority(short priority) throws Exception {
        if (priority == 1 || priority == 2 || priority == 3 || priority == 4) {
            return;
        } else {
            throw new Exception("RATING MUST 1->4 ");
        }
    }

    /**
     * GET các công việc được giao
     */
    public GenericEntity<List<Tickets>> getAllAssignedTicket(Employees employee) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Tickets> root = cq.from(Tickets.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Tickets_.assignedTo), employee));
        List<Tickets> listTicket = em.createQuery(cq).getResultList();
        GenericEntity<List<Tickets>> entity = new GenericEntity<List<Tickets>>(listTicket) {
        };
        return entity;
    }

    /**
     * GET các công việc đã tạo
     */
    public GenericEntity<List<Tickets>> getTicketMeCreate(Employees employee) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Tickets> root = cq.from(Tickets.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Tickets_.createdBy), employee));
        List<Tickets> listTicket = em.createQuery(cq).getResultList();
        GenericEntity<List<Tickets>> entity = new GenericEntity<List<Tickets>>(listTicket) {
        };
        return entity;
    }

    /**
     * GET các công việc liên quan
     */
    public GenericEntity<List<TicketRelaters>> getTicketMeRelate(Employees employee) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
        cq.select(root);
        cq.where(cb.equal(root.get(TicketRelaters_.employeeId), employee));
        List<TicketRelaters> listTicket = em.createQuery(cq).getResultList();
        GenericEntity<List<TicketRelaters>> entity = new GenericEntity<List<TicketRelaters>>(listTicket) {
        };
        return entity;
    }

    /**
     * GET các công việc trong team
     */
    public GenericEntity<List<Tickets>> getTicketOfTeam(int teamId) throws NotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Tickets> root = cq.from(Tickets.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Tickets_.teamId), getTeamsById(teamId)));
        List<Tickets> listTicket = em.createQuery(cq).getResultList();
        GenericEntity<List<Tickets>> entity = new GenericEntity<List<Tickets>>(listTicket) {
        };
        return entity;
    }

    /**
     * GET các công việc của partIT
     */
    public GenericEntity<List<Tickets>> getTicketOfPartIt(String partcode) throws NotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Tickets> root = cq.from(Tickets.class);
        cq.select(root);
        if (partcode != null) {
            cq.where(cb.equal(root.get(Tickets_.partcode), getPartByCode(partcode)));
        }
        List<Tickets> listTicket = em.createQuery(cq).getResultList();

        GenericEntity<List<Tickets>> entity = new GenericEntity<List<Tickets>>(listTicket) {
        };
        return entity;
    }

    /**
     * Đánh dấu đọc hoặc chwua đọc
     */
    public void markRead(int ticket_id, int employee_id, short status) throws Exception {
        checkStatusRead(status);
        if (status == 1) {
            Reader reader = new Reader();
            reader.setEmployeeId(employee_id);
            reader.setStatusRead(status);
            reader.setTicketId(ticket_id);
            em.persist(reader);
        } else if (status == 0) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<Reader> root = cq.from(Reader.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get(Reader_.employeeId), employee_id),
                            cb.equal(root.get(Reader_.ticketId), ticket_id)
                    )
            );
            List<Reader> listReader = em.createQuery(cq).getResultList();
            if (listReader.size() > 0) {
                for (Reader r : listReader) {
                    em.remove(r);
                }
            }

        }
    }

    /**
     * Kiểm tra trạng thái của read có hợp lệ không ( phải là 0 - đã đọc hoặc 1-
     * chưa đọc)
     *
     * @param status
     * @throws Exception
     */
    public void checkStatusRead(short status) throws Exception {
        if (status == 0 || status == 1) {
            return;
        } else {
            throw new Exception("STATUS READ MUST BE 0 OR 1");
        }
    }

    /**
     * GET tất cả các employee
     */
    public GenericEntity<List<Employees>> getAllEmployee() throws NotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Employees> root = cq.from(Employees.class);
        cq.select(root);
        List<Employees> listTicket = em.createQuery(cq).getResultList();

        GenericEntity<List<Employees>> entity = new GenericEntity<List<Employees>>(listTicket) {
        };
        return entity;
    }

    /*
     Kiểm tra quyền thay đổi ticket
     */
    public void checkPermissonUpdate(int userId) throws Exception {
        try {
            checkPermission(userId, Config.PMS_PUT_REQUEST_TEAM);
        } catch (Exception e) {
            throw new Exception("Bạn không có quyền sửa");
        }
    }

    /*
     kiểm tra xem có ở trong team không
     */
    public void checkEmployeeInTeam(int userId, int ticket_id) throws Exception {
        Employees employee = getUserById(userId);
        try {
            /*
             nếu là toàn quyền công ty thì không cần check cái này 
             */
            if (!checkPermissionBoolean(userId, Config.PMS_ALL)) {
                checkTicketInTeam(employee, ticket_id);
            }

        } catch (Exception e) {

        }
    }

    /**
     * kiểm tra trạng thái của công việc nếu là closed hoặc cancell thì không
     * thể thay đỏi attribute
     */
    public void checkStatusToUpdate(int ticket_id) throws Exception {
        try {
            checkStatusOfTicket(ticket_id);
        } catch (Exception e) {
            throw new Exception("Không thể thay đổi công việc đã closed/resolved/cancelled");
        }
    }

    /**
     * kiểm tra tất cả điều kiện để có thể update ticket
     */

    public void checkAllConditionToUpdate(int userId,  int ticket_id) throws Exception {
        checkPermissonUpdate(userId);
        checkEmployeeInTeam(userId,ticket_id);
        checkStatusToUpdate(ticket_id);
    }
    
    /**
     * kiểm tra tất cả điều kiện để có thể update ticket
     */
    public GenericEntity<List<TicketRelaters>> getRelaterByTicketId(int ticket_id) throws Exception {
        Tickets ticket  = getTicketById(ticket_id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<TicketRelaters> root = cq.from(TicketRelaters.class);
        cq.select(root);
        cq.where(
                            cb.equal(root.get(TicketRelaters_.ticketId), ticket)

            );
        List<TicketRelaters> listTicket = em.createQuery(cq).getResultList();

        GenericEntity<List<TicketRelaters>> entity = new GenericEntity<List<TicketRelaters>>(listTicket) {
        };
        return entity;
    }
            
            /**
     * Lấy ra các ticket đã đọc theo userid
     */
    public GenericEntity<List<Reader>> getReaderByEmployeeId(int user_id) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Reader> root = cq.from(Reader.class);
        cq.select(root);
        cq.where(
                            cb.equal(root.get(Reader_.employeeId), user_id)

            );
        List<Reader> listTicket = em.createQuery(cq).getResultList();

        GenericEntity<List<Reader>> entity = new GenericEntity<List<Reader>>(listTicket) {
        };
        return entity;
    }

}

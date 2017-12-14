/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import config.Config;
import dataobject.SessionInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;
import z11.rs.exception.NotFoundException;
import z11.rs.exception.UnauthorizedException;
import z11.rs.exception.RequestParamNotValidException;
/**
 *
 * @author vietduc
 */

@Singleton
public class SessionManager {

    
    public SessionManager() {
        sessions = new TreeMap<>();
    }
    
    private final Map<String, SessionInfo> sessions;
    
    public boolean checkSession(String session) {
        return sessions.containsKey(session);
    }
    
    public int getUserFromSession(String session) {
        return sessions.get(session).getUserid();
    }
    
    public boolean addSession(String session, int userId) {
        if (sessions.containsKey(session)) {
            return false;
        } else {
            sessions.put(session, new SessionInfo(session, userId));
        }
        return true;
    }
    
    public void removeSession(String session) {
        sessions.remove(session);
    }
    
    public void removeAllSession(String session) {
        removeUser(sessions.get(session).getUserid());
    }
    
    public void removeUser(int userId) {
        Iterator<String> it = sessions.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            SessionInfo sessionInfo = sessions.get(key);
            if (sessionInfo.getUserid() == userId) {
                it.remove();
            }
        }
    }
    
    public void removeAllUser() {
        sessions.clear();
    }
    
    public int getSessionUserId(HttpServletRequest request) throws UnauthorizedException {
        try {
            String sessionId = z11.rs.auth.AuthUtil.checkAuthorization(request);
            int userId = getUserFromSession(sessionId);
            return userId;
        } catch (Exception e) {
            throw new UnauthorizedException("Unauthorized:" + e.getMessage());
        }
        
    }
    
    public List<SessionInfo> getAllSessions() throws RequestParamNotValidException {
        return getSessions(0, Config.MAX_SEARCH_RESUTL);
    }
    
    public List<SessionInfo> getSessions(int from, int to) throws RequestParamNotValidException {
        to = Math.min(from + Config.MAX_SEARCH_RESUTL, to);
        if (from > to || to > from + Config.MAX_SEARCH_RESUTL) {
            throw new RequestParamNotValidException("from/to parameter not valid!");
        }
        List<SessionInfo> sessionInfos = new ArrayList<>();
        
        List<String> listKeys = new ArrayList<>(sessions.keySet());
        
        try {
            for (int index = from; index <= to; index++) {
                sessionInfos.add(sessions.get(listKeys.get(index)));
            }
        } catch (Exception e) {
            // out of index
        }
        return sessionInfos;
    }
    
    public SessionInfo getSession(String sessionvalue) throws NotFoundException {
        try {
            return sessions.get(sessionvalue);
        } catch (Exception e) {
            throw new z11.rs.exception.NotFoundException("Not found session value: sessionvalue");
        }
    }
    
    
    public List<SessionInfo> findSessions(String patten) {
        List<SessionInfo> sessionInfos = new ArrayList<>();
        Iterator<String> it = sessions.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            SessionInfo sessionInfo = sessions.get(key);

            if (sessionInfo.getUserid() == z11.S_tring.parseInt(patten, 0)
                    || sessionInfo.getValue().contains(patten)
                    || sessionInfo.getClientinfo().contains(patten)
                    || sessionInfo.getClientip().contains(patten)) {
                sessionInfos.add(sessionInfo);
                if (sessionInfos.size() >= Config.MAX_SEARCH_RESUTL) {
                    break;
                }
            }
        }
        return sessionInfos;
    }
    
    public int getCount() {
        return sessions.size();
    }
}

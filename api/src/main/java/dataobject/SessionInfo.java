/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataobject;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class SessionInfo {
    public SessionInfo() {
        this.createdtime = new Date();
    }
    
    public SessionInfo(String value, int userid, String clientinfo, String clientip) {
        this();
        this.value = value;
        this.userid = userid;
        this.clientinfo = clientinfo;
        this.clientip = clientip;
    }
    
    public SessionInfo(String value, int userid) {
        this(value, userid, "", "");
    }
    
    @XmlElement
    private String value;
    @XmlElement
    private int userid;
    @XmlElement
    private String clientinfo;
    @XmlElement
    private String clientip;
    @XmlElement
    private Date createdtime;

    
//    /**
//     * @return the value
//     */
    public String getValue() {
        return value;
    }
//
//    /**
//     * @param value the value to set
//     */
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    /**
//     * @return the userid
//     */
    public int getUserid() {
        return userid;
    }
//
//    /**
//     * @param userid the userid to set
//     */
//    public void setUserid(int userid) {
//        this.userid = userid;
//    }
//
//    /**
//     * @return the clientinfo
//     */
    public String getClientinfo() {
        return clientinfo;
    }
//
//    /**
//     * @param clientinfo the clientinfo to set
//     */
//    public void setClientinfo(String clientinfo) {
//        this.clientinfo = clientinfo;
//    }
//
//    /**
//     * @return the clientip
//     */
    public String getClientip() {
        return clientip;
    }
//
//    /**
//     * @param clientip the clientip to set
//     */
//    public void setClientip(String clientip) {
//        this.clientip = clientip;
//    }
//
//    /**
//     * @return the createdtime
//     */
//    public Date getCreatedtime() {
//        return createdtime;
//    }
//
//    /**
//     * @param createdtime the createdtime to set
//     */
//    public void setCreatedtime(Date createdtime) {
//        this.createdtime = createdtime;
//    }
}

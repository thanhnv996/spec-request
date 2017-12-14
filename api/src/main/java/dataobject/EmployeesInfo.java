/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataobject;

import entity.Employees;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class EmployeesInfo {

    public EmployeesInfo() {

    }

    public EmployeesInfo(Integer id, String email, String name, String password, String role, Date createdtime) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.createdtime = createdtime;
    }

    @XmlElement
    private Integer id;
    @XmlElement
    private String email;
    @XmlElement
    private String name;
    @XmlElement
    private String password;
    @XmlElement
    private String role;
    @XmlElement
    private Date createdtime;

    public static EmployeesInfo createSimpleEmployeesInfo(Employees user) {
        EmployeesInfo userInfo = new EmployeesInfo(user.getId(), null, user.getName(), null, user.getRolecode().getRolecode(), user.getCreatedAt());
        return userInfo;
    }

    public static List<EmployeesInfo> createSimpleListEmployeesInfo(List<Employees> userList) {
        List<EmployeesInfo> userInfoList = new ArrayList<>();
        for (Employees user : userList) {
            EmployeesInfo userInfo = EmployeesInfo.createSimpleEmployeesInfo(user);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    public static EmployeesInfo createFullEmployeesInfo(Employees user) {
        EmployeesInfo userInfo = new EmployeesInfo(user.getId(), user.getEmail(), user.getName(), null,user.getRolecode().getRolecode(), user.getCreatedAt());
        return userInfo;
    }

    public static List<EmployeesInfo> createFullListEmployeesInfo(List<Employees> userList) {
        List<EmployeesInfo> userInfoList = new ArrayList<>();
        for (Employees user : userList) {
            EmployeesInfo userInfo = EmployeesInfo.createFullEmployeesInfo(user);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }
}

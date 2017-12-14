/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataobject;

/**
 *
 * @author vietduc
 */
public class PermissionInfo {
    public PermissionInfo(String code) {
        this.pmscode = code;
    }
    private String pmscode;


    public String getPmscode() {
        return pmscode;
    }

    public void setPmscode(String pmscode) {
        this.pmscode = pmscode;
    }
}

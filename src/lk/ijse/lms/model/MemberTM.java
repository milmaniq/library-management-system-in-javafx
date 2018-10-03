/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.model;

/**
 *
 * @author Ilman Iqbal
 */
public class MemberTM {

    private String nic;
    private String name;
    private String address;
    private String contactNum;
    private String history;
    private String edit;
    private String remove;

    public MemberTM() {
    }

    public MemberTM(String nic, String name, String address, String contactNum, String history, String edit, String remove) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.contactNum = contactNum;
        this.history = history;
        this.edit = edit;
        this.remove = remove;
    }

    
    /**
     * @return the nic
     */
    public String getNic() {
        return nic;
    }

    /**
     * @param nic the nic to set
     */
    public void setNic(String nic) {
        this.nic = nic;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the contactNum
     */
    public String getContactNum() {
        return contactNum;
    }

    /**
     * @param contactNum the contactNum to set
     */
    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    /**
     * @return the history
     */
    public String getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * @return the edit
     */
    public String getEdit() {
        return edit;
    }

    /**
     * @param edit the edit to set
     */
    public void setEdit(String edit) {
        this.edit = edit;
    }

    /**
     * @return the remove
     */
    public String getRemove() {
        return remove;
    }

    /**
     * @param remove the remove to set
     */
    public void setRemove(String remove) {
        this.remove = remove;
    }
    
    
}

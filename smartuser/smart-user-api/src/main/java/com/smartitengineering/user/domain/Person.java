/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

/**
 *
 * @author modhu7
 */
public class Person extends BasicPerson{
    private BasicPerson father;
    private BasicPerson mother;
    private BasicPerson spouse;
    private Address address;
    private String primaryEmail;
    private String secondaryEmail;
    private String phoneNumber;
    private String cellPhoneNumber;
    private String faxNumber;

    public Address getAddress() {
        if(address==null){
            return new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        if(address==null){
            return;
        }
        this.address = address;
    }

    public String getCellPhoneNumber() {
        if(cellPhoneNumber==null){
            return "";
        }
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        if(cellPhoneNumber==null){
            return;
        }
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public BasicPerson getFather() {
        if(father==null){
            return new BasicPerson();
        }
        return father;
    }

    public void setFather(BasicPerson father) {
        if(father==null){
            return;
        }
        this.father = father;
    }

    public String getFaxNumber() {
        if(faxNumber==null){
            return "";
        }
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        if(faxNumber==null){
            return;
        }
        this.faxNumber = faxNumber;
    }

    public BasicPerson getMother() {
        if(mother==null){
            return new BasicPerson();
        }
        return mother;
    }

    public void setMother(BasicPerson mother) {
        if(mother==null){
            return;
        }
        this.mother = mother;
    }

    public String getPhoneNumber() {
        if(phoneNumber==null){
            return "";
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber==null){
            return;
        }
        this.phoneNumber = phoneNumber;
    }

    public String getPrimaryEmail() {
        if(primaryEmail==null){
            return "";
        }
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        if(primaryEmail==null){
            return;
        }
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        if(secondaryEmail==null){
            return "";
        }
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        if(secondaryEmail==null){
            return;
        }
        this.secondaryEmail = secondaryEmail;
    }

    public BasicPerson getSpouse() {
        if(spouse==null){
            return new BasicPerson();
        }
        return spouse;
    }

    public void setSpouse(BasicPerson spouse) {
        if(spouse==null){
            return;
        }
        this.spouse = spouse;
    }    
    
}

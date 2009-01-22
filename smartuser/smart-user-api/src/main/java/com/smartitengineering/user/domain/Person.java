/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class Person extends AbstractPersistentDTO<Person> {

    private BasicIdentity father;
    private BasicIdentity mother;
    private BasicIdentity spouse;
    private BasicIdentity self;
    private Address address;
    private Date birthDay;
    private String primaryEmail;
    private String secondaryEmail;
    private String phoneNumber;
    private String cellPhoneNumber;
    private String faxNumber;

    public Date getBirthDay() {
        if (birthDay == null) {
            birthDay = new Date();
        }
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        if (birthDay == null) {
            return;
        }
        this.birthDay = birthDay;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            return;
        }
        this.address = address;
    }

    public String getCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            cellPhoneNumber = "";
        }
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return;
        }
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public BasicIdentity getFather() {
        if (father == null) {
            father = new BasicIdentity();
        }
        return father;
    }

    public void setFather(BasicIdentity father) {
        if (father == null) {
            return;
        }
        this.father = father;
    }

    public String getFaxNumber() {
        if (faxNumber == null) {
            faxNumber = "";
        }
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        if (faxNumber == null) {
            return;
        }
        this.faxNumber = faxNumber;
    }

    public BasicIdentity getMother() {
        if (mother == null) {
            mother = new BasicIdentity();
        }
        return mother;
    }

    public void setMother(BasicIdentity mother) {
        if (mother == null) {
            return;
        }
        this.mother = mother;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return;
        }
        this.phoneNumber = phoneNumber;
    }

    public String getPrimaryEmail() {
        if (primaryEmail == null) {
            primaryEmail = "";
        }
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        if (primaryEmail == null) {
            return;
        }
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        if (secondaryEmail == null) {
            secondaryEmail = "";
        }
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        if (secondaryEmail == null) {
            return;
        }
        this.secondaryEmail = secondaryEmail;
    }

    public BasicIdentity getSpouse() {
        if (spouse == null) {
            spouse = new BasicIdentity();
        }
        return spouse;
    }

    public void setSpouse(BasicIdentity spouse) {
        if (spouse == null) {
            return;
        }
        this.spouse = spouse;
    }

    public BasicIdentity getSelf() {
        if(self == null){
            self = new BasicIdentity();
        }
        return self;
    }

    public void setSelf(BasicIdentity self) {
        this.self = self;
    }

    @Override
    public boolean isValid() {
        if (StringUtils.isEmpty(primaryEmail) || StringUtils.isEmpty(this.self.getNationalID())) {
            return false;
        }
        return true;
    }
}

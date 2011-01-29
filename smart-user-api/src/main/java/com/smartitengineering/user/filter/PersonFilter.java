/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.filter;

import com.smartitengineering.user.domain.Name;

/**
 *
 * @author modhu7
 */
public class PersonFilter extends AbstractFilter{

    private Name name;
    private String email;
    private String nationalId;
    private String fatherName;
    private String sposeNmae;
    private String cellNo;

    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }

    public Name getName() {
        if (name == null) {
            return new Name();
        }
        return name;
    }

    public void setEmail(String email) {
        if (email == null) {
            return;
        }
        this.email = email;
    }

    public void setName(Name name) {
        if (name == null) {
            return;
        }
        this.name = name;
    }

  public String getCellNo() {
    return cellNo;
  }

  public void setCellNo(String cellNo) {
    this.cellNo = cellNo;
  }

  public String getFatherName() {
    return fatherName;
  }

  public void setFatherName(String fatherName) {
    this.fatherName = fatherName;
  }

  public String getNationalId() {
    return nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  public String getSposeNmae() {
    return sposeNmae;
  }

  public void setSposeNmae(String sposeNmae) {
    this.sposeNmae = sposeNmae;
  }

}

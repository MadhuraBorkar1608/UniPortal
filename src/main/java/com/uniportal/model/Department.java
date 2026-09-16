package com.uniportal.model;

import java.sql.Timestamp;

public class Department {
    private int id;
    private String deptCode;
    private String deptName;
    private String description;
    private Timestamp createdAt;

    public Department() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return deptName;
    }
}

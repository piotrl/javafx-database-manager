package net.piotrl.mvcrud.model.dto;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private Date registerDate;

    public User(String name) {
        this.name =  name;
        this.registerDate = new Date();
    }

    public User(int id, String name) {
        this(name);
        this.id =  id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "" + this.getName();
    }
}

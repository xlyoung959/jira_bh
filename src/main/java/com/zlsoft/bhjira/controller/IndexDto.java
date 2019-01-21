package com.zlsoft.bhjira.controller;

public class IndexDto {

    public IndexDto(String name,String home){
        this._name = name;
        this.home = home;
    }

    public IndexDto(){}


    private String _name;

    private String home;

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}

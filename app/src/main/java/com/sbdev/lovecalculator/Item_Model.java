package com.sbdev.lovecalculator;

public class Item_Model
{

    String key;
    String name;
    String partner;
    String percentage;
    String result;
    String date;

    public Item_Model(String key,String name, String partner, String percentage, String result,String date) {
        this.key=key;
        this.name = name;
        this.partner = partner;
        this.percentage = percentage;
        this.result = result;
        this.date=date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

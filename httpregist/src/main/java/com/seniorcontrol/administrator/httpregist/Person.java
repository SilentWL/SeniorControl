package com.seniorcontrol.administrator.httpregist;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class Person {
    private String name;
    private int age;
    private String url;

    public Person(int age, String name, SchoolInfo schoolInfo, String url) {
        this.age = age;
        this.name = name;
        this.schoolInfo = schoolInfo;
        this.url = url;
    }
    public Person() {
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private SchoolInfo schoolInfo;
}

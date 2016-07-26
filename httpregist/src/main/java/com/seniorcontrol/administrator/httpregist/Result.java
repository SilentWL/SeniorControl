package com.seniorcontrol.administrator.httpregist;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class Result {
    private int result;
    private List<Person> personData;

    public List<Person> getPersonData() {
        return personData;
    }

    public void setPersonData(List<Person> personData) {
        this.personData = personData;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

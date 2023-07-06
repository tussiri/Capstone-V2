package org.launchcode.LiftoffRecipeProject.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

public class SearchCriteria {
    private String key;
    private String operation;
    private List<Object> values;

    public SearchCriteria(String key, String operation, String value) {
        try {
            this.key = URLDecoder.decode(key, "UTF-8");
            this.operation = operation;
            this.values = Collections.singletonList(URLDecoder.decode(value.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
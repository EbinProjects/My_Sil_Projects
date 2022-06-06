package com.example.retrofitapi;

public class childRules {
    String rules,remdays,discounds;

    public childRules(String rules, String remdays, String discounds) {
        this.rules = rules;
        this.remdays = remdays;
        this.discounds = discounds;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRemdays() {
        return remdays;
    }

    public void setRemdays(String remdays) {
        this.remdays = remdays;
    }

    public String getDiscounds() {
        return discounds;
    }

    public void setDiscounds(String discounds) {
        this.discounds = discounds;
    }
}

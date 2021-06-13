package com.shipping.model;


import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class RuleRequest implements Serializable {



    private Long id;

    @NotEmpty(message = "validation.error.rule.key.empty")
    private String rule;

    @NotEmpty(message = "validation.error.rule.value.empty")
    private String value;

    public RuleRequest(Long id, String rule, String value) {
        this.rule = rule;
        this.value = value;
        this.id=id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

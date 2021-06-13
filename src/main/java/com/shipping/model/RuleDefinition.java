package com.shipping.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class RuleDefinition {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String key;

    private String value;

    public RuleDefinition(Long id, String key, String value) {
        this.key = key;
        this.value = value;
        this.id=id;
    }
    public RuleDefinition(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleDefinition)) return false;
        RuleDefinition that = (RuleDefinition) o;
        return getId().equals(that.getId()) &&
                getKey().equals(that.getKey()) &&
                getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getKey(), getValue());
    }

    public RuleDefinition() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

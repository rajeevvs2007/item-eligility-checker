package com.shipping.service;

import com.shipping.model.RuleDefinition;
import com.shipping.model.RuleRequest;
import com.shipping.model.RuleResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RegionalShippingRuleService {

    void save(RuleRequest rule);

    boolean update(RuleRequest rule);

    RuleResponse findById(long id);

    List<RuleResponse> findAll();

    void remove(long id);

}

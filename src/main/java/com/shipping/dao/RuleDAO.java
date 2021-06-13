package com.shipping.dao;

import com.shipping.model.RuleDefinition;
import com.shipping.model.RuleRequest;
import com.shipping.model.RuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RuleDAO {

    @Autowired
    RuleRepository ruleRepository;


    public void save(RuleRequest rule) {

        RuleDefinition definition = ruleRepository.save(new RuleDefinition(rule.getRule(),rule.getValue()));

    }

    public boolean update(RuleRequest rule) {
        Optional<RuleDefinition> ruleDefinition = ruleRepository.findById(rule.getId());
        if(ruleDefinition.isPresent()) {
            ruleRepository.save(new RuleDefinition(rule.getId(),rule.getRule(),rule.getValue()));
            return true;
        } else {
            return false;
        }

    }

    public RuleResponse findById(long id) {
        Optional<RuleDefinition> ruleDefinition =  ruleRepository.findById(id);
        if(ruleDefinition.isPresent()){
            RuleDefinition def = ruleDefinition.get();
            return new RuleResponse(def.getId(),def.getKey(),def.getValue());
        } else {
            return null;
        }
    }

    public List<RuleResponse> findAll() {
        List<RuleResponse> responseList = new ArrayList<>();
        Iterable<RuleDefinition> resultSet  = ruleRepository.findAll();
        resultSet.forEach(ruleDefinition->responseList.add(new RuleResponse(ruleDefinition.getId(),
                ruleDefinition.getKey(),ruleDefinition.getValue())));
        return responseList;
    }


    public RuleResponse findRuleByName(String key) {

        List<RuleDefinition> ruleDefinitionList =  ruleRepository.findByKey(key);

        return CollectionUtils.isEmpty(ruleDefinitionList)  ? null : new RuleResponse(ruleDefinitionList.get(0).getId(),
                        ruleDefinitionList.get(0).getKey(),ruleDefinitionList.get(0).getValue());
    }


    public void remove(long id) {

        ruleRepository.deleteById(id);

    }
}

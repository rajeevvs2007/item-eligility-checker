package com.shipping.controller;


import com.shipping.model.AuthRequest;
import com.shipping.model.AuthResponse;
import com.shipping.model.RuleRequest;
import com.shipping.model.RuleResponse;
import com.shipping.service.RegionalShippingRuleService;
import com.shipping.service.impl.UserManagementService;
import com.shipping.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.List;


@RestController
public class RegionalShippingRulesController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegionalShippingRuleService regionalShippingRuleService;

    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse authenticateUser(@RequestBody AuthRequest authenticationRequest) throws Exception {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));

        final UserDetails userDetails = userManagementService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthResponse(token);
    }

    @PostMapping(value = "/v1/rules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRule(@RequestBody RuleRequest ruleRequest) throws Exception {

        regionalShippingRuleService.save(ruleRequest);

        return  new ResponseEntity<>( HttpStatus.CREATED);
    }

    @GetMapping(value = "/v1/rules",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RuleResponse>> findAll() throws Exception {

        List<RuleResponse> ruleResponseList = regionalShippingRuleService.findAll();

        return  new ResponseEntity<List<RuleResponse>>(ruleResponseList, HttpStatus.OK);
    }

    @GetMapping(value = "v1/rules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RuleResponse> find(@PathVariable long id) throws Exception {

        RuleResponse ruleResponseList = regionalShippingRuleService.findById(id);

        if(ruleResponseList == null){
            throw new NoResultException("No record found");
        }

        return  new ResponseEntity<RuleResponse>(ruleResponseList, HttpStatus.OK);
    }

    @PutMapping(value = "v1/rules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RuleResponse> update(@PathVariable long id, @RequestBody RuleRequest ruleRequest) throws Exception {

        RuleResponse ruleResponse = regionalShippingRuleService.findById(id);

        if(ruleResponse == null){
            throw new NoResultException("No record found to update");
        }

        ruleRequest.setId(ruleResponse.getId());

        regionalShippingRuleService.update(ruleRequest);

        return  new ResponseEntity<RuleResponse>(HttpStatus.OK);
    }

    @DeleteMapping(value = "v1/rules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity remove(@PathVariable long id) {

        regionalShippingRuleService.remove(id);

        return  new ResponseEntity<RuleResponse>(HttpStatus.NO_CONTENT);
    }

}

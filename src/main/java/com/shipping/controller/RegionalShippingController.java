package com.shipping.controller;

import com.shipping.model.EligibilityResponse;
import com.shipping.model.EligibilityRequest;
import com.shipping.service.RegionalShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("v1/shipping")
public class RegionalShippingController {

    @Autowired
    RegionalShippingService shippingService;


    @PostMapping(value = "/item/eligible", produces = MediaType.APPLICATION_JSON_VALUE)
    public EligibilityResponse isItemEligible(HttpServletRequest request, @Valid @RequestBody EligibilityRequest eligibilityRequest){
        boolean  eligibility = shippingService.checkItemEligibility(eligibilityRequest);
        return new EligibilityResponse(eligibility);
    }

}

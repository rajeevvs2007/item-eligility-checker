package com.shipping.service;

import com.shipping.model.EligibilityRequest;
import org.springframework.stereotype.Component;

@Component
public interface RegionalShippingService {

    boolean checkItemEligibility(EligibilityRequest eligibilityRequest);
}

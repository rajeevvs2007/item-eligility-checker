package com.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipping.model.EligibilityRequest;
import com.shipping.service.RegionalShippingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = { "spring.profiles.active=test" })
@AutoConfigureMockMvc(addFilters = false)
class ItemEligilityCheckerApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RegionalShippingService shippingService;




	@Test
	void testItemEligibilty_returnsFalse() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","john",12,8.0));
			EligibilityRequest eligibilityRequest = new EligibilityRequest("iphone","john",12,8.0);

			Mockito.when(shippingService.checkItemEligibility(eligibilityRequest)).thenReturn(true);

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.itemEligible").value("true"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	void testItemEligibilty_returnsTrue() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","john",12,8.0));

			EligibilityRequest eligibilityRequest = new EligibilityRequest("iphone","john",12,8.0);

			Mockito.when(shippingService.checkItemEligibility(eligibilityRequest)).thenReturn(false);


			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.itemEligible").value("false"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testItemEligibilty_returnsValidationErrorEmptyTitle() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("","john",12,8.0));

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	void testItemEligibilty_returnsValidationErrorEmptySeller() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","",12,8.0));

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testItemEligibilty_returnsValidationErrorNegativePrice() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","",12,-1.0));

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	void testItemEligibilty_returnsValidationSuccess() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","john",12,1.0));

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

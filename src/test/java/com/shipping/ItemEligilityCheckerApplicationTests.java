package com.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipping.model.EligibilityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemEligilityCheckerApplicationTests {

	@Autowired
	private MockMvc mvc;


	@Test
	void testItemEligibilty() {
		try {
			String testInput = new ObjectMapper().writeValueAsString(
					new EligibilityRequest("iphone","john",12,8.0));

			mvc.perform(MockMvcRequestBuilders.post("/v1/shipping/item/eligible").content(testInput)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.itemEligible").value("true"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package se.esss.ics.masar.web.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.exception.ConfigNotFoundException;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.web.config.ControllersTestConfig;

@RunWith(SpringRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { ControllersTestConfig.class }) })
@WebMvcTest(ConfigurationController.class)

public class ConfigurationControllerTest {

	@Autowired
	private IServices services;

	@Autowired
	private MockMvc mockMvc;

	private Config configFromClient;

	private Config config1;

	private Snapshot snapshot;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String JSON = "application/json;charset=UTF-8";

	@Before
	public void setUp() {

		ConfigPv configPv = ConfigPv.builder().groupname("groupname").pvName("pvName").readonly(true).tags("tags")
				.build();

		configFromClient = Config.builder().active(true).configPvList(Arrays.asList(configPv))
				.description("description").system("system").build();

		config1 = Config.builder().active(true).configPvList(Arrays.asList(configPv))
				.description("description").system("system").build();

		SnapshotPv snapshotPv = SnapshotPv.builder().dtype(1).fetchStatus(true).severity(0).status(1).time(1000L)
				.timens(777).value(new Double(7.7)).build();

		snapshot = Snapshot.builder().approve(true).comment("comment")
				.snapshotPvList(Arrays.asList(snapshotPv)).build();

		when(services.createNewConfiguration(configFromClient)).thenReturn(config1);
		when(services.getConfigs()).thenReturn(Arrays.asList(config1));
		when(services.getConfig(1)).thenReturn(config1);
	}

	@Test
	public void testSaveConfig() throws Exception {

		MockHttpServletRequestBuilder request = put("/config").contentType(JSON)
				.content(objectMapper.writeValueAsString(configFromClient));

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();

		// Make sure response contains expected data
		objectMapper.readValue(result.getResponse().getContentAsString(), Config.class);

	}

	@Test
	public void testGetConfigs() throws Exception {
		MockHttpServletRequestBuilder request = get("/config").contentType(JSON);

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();

		// Make sure response contains expected data
		objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Config>>() {
		});

	}

	@Test
	public void testGetConfig() throws Exception {
		MockHttpServletRequestBuilder request = get("/config/1").contentType(JSON);

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();

		// Make sure response contains expected data
		objectMapper.readValue(result.getResponse().getContentAsString(), Config.class);

	}

	@Test
	public void testGetNonExistingConfig() throws Exception {

		when(services.getConfig(2)).thenThrow(new ConfigNotFoundException("lasdfk"));

		MockHttpServletRequestBuilder request = get("/config/2").contentType(JSON);

		mockMvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	public void testGetSnapshots() throws Exception {

		when(services.getSnapshots(1)).thenReturn(Arrays.asList(snapshot));

		MockHttpServletRequestBuilder request = get("/config/1/snapshots").contentType(JSON);

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();

		// Make sure response contains expected data
		objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Snapshot>>() {
		});
	}
	
	@Test
	public void testGetSnapshotsForNonExistingConfig() throws Exception {

		when(services.getSnapshots(2)).thenThrow(new ConfigNotFoundException("lasdfk"));

		MockHttpServletRequestBuilder request = get("/config/2/snapshots").contentType(JSON);

		mockMvc.perform(request).andExpect(status().isNotFound());
	}

}

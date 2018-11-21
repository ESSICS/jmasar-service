package se.esss.ics.masar.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.exception.SnapshotNotFoundException;
import se.esss.ics.masar.web.config.ControllersTestConfig;

@RunWith(SpringRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { ControllersTestConfig.class }) })
@WebMvcTest(SnapshotController.class)
public class SnapshotControllerTest {
	

	@Autowired
	private IServices services;

	@Autowired
	private MockMvc mockMvc;

	private Config configFromClient;

	private Config config1;

	private Snapshot snapshot;
	
	private SnapshotPv snapshotPv;

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
		config1.setId(1);

		snapshotPv = SnapshotPv.builder().dtype(1).fetchStatus(true).severity(0).status(1).time(1000L)
				.timens(777).value(new Double(7.7)).build();

		snapshot = Snapshot.builder().approve(true).comment("comment")
				.snapshotPvList(Arrays.asList(snapshotPv)).id(7).build();

		when(services.createNewConfiguration(configFromClient)).thenReturn(config1);
		when(services.getConfiguration(1)).thenReturn(config1);
		//when(services.takeSnapshot(1)).thenReturn(snapshot);
	}
	
	@Test
	public void testTakeSnapshot() throws Exception{
		
		when(services.takeSnapshot(1)).thenReturn(snapshot);
		
		MockHttpServletRequestBuilder request = put("/snapshot/1");

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();
		
		Snapshot snapshot = objectMapper.readValue(result.getResponse().getContentAsString(),
				Snapshot.class);
		
		assertEquals(7, snapshot.getId());
	}
	
	
	@Test
	public void testGetSnapshot() throws Exception{
		
		when(services.getSnapshot(7)).thenReturn(snapshot);
		
		MockHttpServletRequestBuilder request = get("/snapshot/7");

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType(JSON))
				.andReturn();
		
		// Make sure response contains expected data
		objectMapper.readValue(result.getResponse().getContentAsString(), Snapshot.class);
	}
	
	@Test
	public void testGetNonExistingSnapshot() throws Exception{
		
		when(services.getSnapshot(8)).thenThrow(new SnapshotNotFoundException("askdmdsf"));
		
		MockHttpServletRequestBuilder request = get("/snapshot/8");

		mockMvc.perform(request).andExpect(status().isNotFound());
	}
	
	@Test
	public void testDeleteSnapshot() throws Exception{
			
		MockHttpServletRequestBuilder request = delete("/snapshot/9");

		mockMvc.perform(request).andExpect(status().isOk());
	}
	
	@Test
	public void testCommitSnapshot() throws Exception{
			
		MockHttpServletRequestBuilder request = post("/snapshot/9?userName=a&comment=b");

		mockMvc.perform(request).andExpect(status().isOk());
	}
	
	@Test
	public void testCommitSnapshotBadRequest1() throws Exception{
			
		MockHttpServletRequestBuilder request = post("/snapshot/9?userName=a");

		mockMvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommitSnapshotBadRequest2() throws Exception{
			
		MockHttpServletRequestBuilder request = post("/snapshot/9?comment=a");

		mockMvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testNonExistingSnapshot() throws Exception{
		when(services.getSnapshot(88)).thenThrow(new SnapshotNotFoundException("lasdfk"));

		MockHttpServletRequestBuilder request = get("/snapshot/88").contentType(JSON);

		mockMvc.perform(request).andExpect(status().isNotFound());
	}
	
	@Test
	public void testCommitRequestNoUserNameOrComment() throws Exception{
		MockHttpServletRequestBuilder request = post("/snapshot/9");
		
		mockMvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommitRequestEmptyUserName() throws Exception{
		MockHttpServletRequestBuilder request = post("/snapshot/9?userName=&comment=comment");
		
		mockMvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommitRequestEmptyComment() throws Exception{
		MockHttpServletRequestBuilder request = post("/snapshot/9?userName=foo&comment=");
		
		mockMvc.perform(request).andExpect(status().isBadRequest());
	}

}

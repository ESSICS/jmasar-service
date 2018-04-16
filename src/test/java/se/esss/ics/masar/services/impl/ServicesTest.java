package se.esss.ics.masar.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.config.ServicesTestConfig;
import se.esss.ics.masar.services.exception.ConfigNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { ServicesTestConfig.class}) })
public class ServicesTest {
	
	@Autowired
	private IServices services;
	
	@Autowired
	private ConfigDAO configDAO;
	
	@Autowired
	private SnapshotDAO snapshotDAO;
	
	@Autowired
	private IEpicsService epicsServices;
		
	private Config configFromClient;
	
	private Config config1;
	
	private Config configWithParent;
	
	
	@Before
	public void setUp() throws Exception{
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName")
				.readonly(true)
				.tags("tags")
				.build();
		
		configFromClient = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv))
				.description("description")
				.system("system")
				.build();
		
		configFromClient.setId(1);
		configFromClient.setCreated(new Date());
		
		
		config1 = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv))
				.description("description")
				.system("system")
				.build();
		
		config1.setId(1);
		
		configWithParent = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv))
				.description("description")
				.system("system")
				.parent(new Node())
				.build();
		
		when(configDAO.createConfiguration(configFromClient)).thenReturn(configFromClient);
		when(configDAO.createConfiguration(configWithParent)).thenReturn(configWithParent);
	
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateConfigurationNoParent() {
		services.createNewConfiguration(configFromClient);
	}
	
	@Test
	public void testCreateConfiguration() {
		services.createNewConfiguration(configWithParent);
	}
	
	@Test
	public void testGetConfigNotNull() {
		
		when(configDAO.getConfiguration(1)).thenReturn(configFromClient);
		
		Config config = services.getConfiguration(1);
		assertEquals(1, config.getId());
	}

	
	@Test
	public void testTakeSnapshot() {
		when(configDAO.getConfiguration(1)).thenReturn(configFromClient);
		services.takeSnapshot(1);
	}
	
	@Test(expected = ConfigNotFoundException.class)
	public void testTakeSnapshotConfigNotFound() {
		
		services.takeSnapshot(2);
	}
	
	@Test
	public void testTakeSnapshotPvReadFailure() throws Exception{
		
		ConfigPv configPv1 = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName1")
				.readonly(true)
				.tags("tags")
				.build();
		
		ConfigPv configPv2 = ConfigPv.builder()
				.groupname("groupname")
				.pvName("fail")
				.readonly(true)
				.tags("tags")
				.build();
		
		Config config2 = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv1, configPv2))
				.description("description")
				.system("system")
				.build();
		
		when(configDAO.getConfiguration(3)).thenReturn(config2);
		
		when(epicsServices.getPv(any(ConfigPv.class))).thenAnswer(new Answer<SnapshotPv>() {
			
			@Override
			public SnapshotPv answer(InvocationOnMock invocationOnMock) throws Exception{
				ConfigPv configPv = (ConfigPv)invocationOnMock.getArguments()[0];
				if("fail".equals(configPv.getPvName())) {
					throw new PVReadException("PV Read Failure");
				}
				else {
					return mock(SnapshotPv.class);
				}
				
			}
		});
		
		
		when(configDAO.savePreliminarySnapshot(any(Snapshot.class))).thenAnswer(new Answer<Snapshot>() {
			
			@Override
			public Snapshot answer(InvocationOnMock invocationOnMock) {
				return (Snapshot)invocationOnMock.getArguments()[0];
			}
		});
		
		Snapshot snapshot1 = services.takeSnapshot(3);
		
		// Only one SnapshotPv in result due to one of them failing on PV read.
		assertEquals(1, snapshot1.getSnapshotPvList().size());
		
		reset(snapshotDAO);
	}
	
	@Test
	public void testDeleteSnapshot() {
		
		services.deleteSnapshot(1);
		
		verify(snapshotDAO, times(1)).deleteSnapshot(1);
		
		reset(snapshotDAO);
	}
	
	@Test
	public void testCommitSnapshot() {
		
		services.commitSnapshot(anyInt(), anyString(), anyString());
		
		verify(snapshotDAO, times(1)).commitSnapshot(anyInt(), anyString(), anyString());
		verify(snapshotDAO, atLeast(1)).getSnapshot(anyInt(), anyBoolean());
		
		reset(snapshotDAO);
	}
	
	@Test
	public void testGetSnapshots() {
		
		services.getSnapshots(anyInt());
		
		verify(snapshotDAO, times(1)).getSnapshots(anyInt());
		
		reset(snapshotDAO);
	}
	
	@Test
	public void testGetSnapshotNotFound() {
		
		when(snapshotDAO.getSnapshot(77, false)).thenReturn(null);
		
		try {
			services.getSnapshot(77);
			fail("Exception expected here");
		} catch (Exception e) {
			
		}
	
		reset(snapshotDAO);
	}
	
	@Test
	public void testGetSnapshot() {
		
		when(snapshotDAO.getSnapshot(177, true)).thenReturn(mock(Snapshot.class));
		
		Snapshot snapshot = services.getSnapshot(177);
		
		assertNotNull(snapshot);
	
		reset(snapshotDAO);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createNewFolderNoParentSpecified() {
		
		Folder folderFromClient = Folder.builder().name("SomeFolder").build();
		
		services.createFolder(folderFromClient);
	}
	
	@Test
	public void testCreateNewFolder() {
		
		Folder folderFromClient = Folder.builder().name("SomeFolder").id(11)
				.parent(Folder.builder().id(0).build()).build();
		
		services.createFolder(folderFromClient);
		
		verify(configDAO, atLeast(1)).createFolder(folderFromClient);
		
		reset(configDAO);
	}
	
	@Test
	public void testGetFolder() {
			
		services.getFolder(1);
		
		verify(configDAO, atLeast(1)).getFolder(1);
		
		reset(configDAO);
	}
	
	@Test
	public void testDeleteConfiguration() {
			
		services.deleteConfiguration(1);
		
		verify(configDAO, atLeast(1)).deleteConfiguration(1);
		
		reset(configDAO);
	}
	
	@Test
	public void testDeleteFolder() {
			
		services.deleteFolder(1);
		
		verify(configDAO, atLeast(1)).deleteFolder(1);
		
		reset(configDAO);
	}
	
	@Test
	public void testMoveNode() {
			
		services.moveNode(1, 2);
		
		verify(configDAO, atLeast(1)).moveNode(1, 2);
		
		reset(configDAO);
	}
	
}

package se.esss.ics.masar.epics.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.config.EpicsServiceTestConfig;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.model.snapshot.SnapshotPv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { EpicsServiceTestConfig.class }) })
public class EpicsServiceTest {

	@Autowired
	private IEpicsService epicsService;

	@Test
	public void testPvGetOk() throws PVReadException {

		SnapshotPv<Integer> snapshotPv = epicsService.getPv("channelName");

		assertEquals(7, snapshotPv.getValue().intValue());

	}

	@Test(expected = PVReadException.class)
	public void testPvGetThrowsException() throws PVReadException {
		epicsService.getPv("badChannelName");

	}
}

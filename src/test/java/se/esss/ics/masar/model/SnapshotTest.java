package se.esss.ics.masar.model;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;

import static org.junit.Assert.*;

public class SnapshotTest {
	
	@Test
	public void testSnapshot() {
		
		Date now = new Date();
		
		Snapshot snapshot = Snapshot.builder()
			.created(now)
			.id(77)
			.build();
		
		assertFalse(snapshot.isApprove());
		assertEquals(77, snapshot.getId());
		assertNull(snapshot.getComment());
		assertNull(snapshot.getUserName());
		
		Snapshot snapshot2 = Snapshot.builder()
		.approve(true)
		.comment("comment")
		.userName("userName")
		.created(now)
		.id(78)
		.build();
		
		assertTrue(snapshot2.isApprove());
		assertEquals(78,  snapshot2.getId());
		assertEquals("comment", snapshot2.getComment());
		assertEquals("userName", snapshot2.getUserName());
		
		assertEquals(snapshot, snapshot);
		assertNotEquals(snapshot, snapshot2);
		assertNotEquals(snapshot, "String");
		
		assertEquals(77, snapshot.hashCode());
		
	}
	
	@Test
	public void testSnapshotPv() {
		
		SnapshotPv<Double> snapshotPv = SnapshotPv.<Double>builder()
				.dtype(4)
				.snapshotId(777)
				.severity(9)
				.status(10)
				.time(1000L)
				.timens(7777)
				.value(new Double(7.7))
				.configPv(ConfigPv.builder().pvName("whatever").build())
				.build();
		
		assertEquals(4, snapshotPv.getDtype());
		assertEquals(777, snapshotPv.getSnapshotId());
		assertEquals(9, snapshotPv.getSeverity());
		assertEquals(10, snapshotPv.getStatus());
		assertEquals(1000L, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
		assertEquals(Double.class, snapshotPv.getValue().getClass());
		assertEquals(7.7, snapshotPv.getValue().doubleValue(), 0);
		assertEquals("whatever", snapshotPv.getConfigPv().getPvName());
		assertFalse(snapshotPv.isFetchStatus());
		
		assertNotEquals(snapshotPv, "String");
		assertEquals(snapshotPv, snapshotPv);
		
		Date now = new Date();
		
		Snapshot snapshot = Snapshot.builder()
		.created(now)
		.id(77)
		.snapshotPvList(Arrays.asList(snapshotPv))
		.build();
		
		assertFalse(snapshot.isApprove());
		assertEquals(77,  snapshot.getId());
		assertNull(snapshot.getComment());
		assertNull(snapshot.getUserName());
		assertEquals(1, snapshot.getSnapshotPvList().size());
		
	}
}

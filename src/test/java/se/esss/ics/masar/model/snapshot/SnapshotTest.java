package se.esss.ics.masar.model.snapshot;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
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
		assertEquals(77,  snapshot.getId());
		assertNull(snapshot.getComment());
		assertNull(snapshot.getUserName());
		
		snapshot = Snapshot.builder()
				.approve(true)
				.comment("comment")
				.userName("userName")
				.created(now)
				.id(77)
				.build();
		
		assertTrue(snapshot.isApprove());
		assertEquals(77,  snapshot.getId());
		assertEquals("comment", snapshot.getComment());
		assertEquals("userName", snapshot.getUserName());
		
	}
	
	@Test
	public void testSnapshotPv() {
		
		SnapshotPv<Double> snapshotPv = SnapshotPv.<Double>builder()
				.dtype(4)
				.id(777)
				.severity(9)
				.status(10)
				.time(1000L)
				.timens(7777)
				.value(new Double(7.7))
				.build();
		
		assertEquals(4, snapshotPv.getDtype());
		assertEquals(777, snapshotPv.getId());
		assertEquals(9, snapshotPv.getSeverity());
		assertEquals(10, snapshotPv.getStatus());
		assertEquals(1000L, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
		assertEquals(Double.class, snapshotPv.getValue().getClass());
		assertEquals(7.7, snapshotPv.getValue().doubleValue(), 0);
		
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

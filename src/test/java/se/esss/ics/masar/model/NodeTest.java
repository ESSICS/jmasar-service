package se.esss.ics.masar.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

	@Test
	public void test1() {
		
		Node node = new Node();
		node.setId(1);
		
		Node node2 = new Node();
		node2.setId(2);
		
		assertNotEquals(node, node2);
		assertEquals(node, node);
		assertNotEquals(node, "String");
		
		assertEquals(1, node.hashCode());
		
	}
}

package se.esss.ics.masar.epics.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;

import org.epics.pvaccess.PVFactory;
import org.epics.pvdata.factory.AbstractPVScalar;
import org.epics.pvdata.factory.BasePVBoolean;
import org.epics.pvdata.factory.BasePVBooleanArray;
import org.epics.pvdata.factory.BasePVByte;
import org.epics.pvdata.factory.BasePVByteArray;
import org.epics.pvdata.factory.BasePVDouble;
import org.epics.pvdata.factory.BasePVDoubleArray;
import org.epics.pvdata.factory.BasePVFloat;
import org.epics.pvdata.factory.BasePVFloatArray;
import org.epics.pvdata.factory.BasePVInt;
import org.epics.pvdata.factory.BasePVIntArray;
import org.epics.pvdata.factory.BasePVLong;
import org.epics.pvdata.factory.BasePVLongArray;
import org.epics.pvdata.factory.BasePVShort;
import org.epics.pvdata.factory.BasePVShortArray;
import org.epics.pvdata.factory.BasePVString;
import org.epics.pvdata.factory.BasePVStringArray;
import org.epics.pvdata.factory.BasePVUByte;
import org.epics.pvdata.factory.BasePVUByteArray;
import org.epics.pvdata.factory.BasePVUInt;
import org.epics.pvdata.factory.BasePVUIntArray;
import org.epics.pvdata.factory.BasePVULong;
import org.epics.pvdata.factory.BasePVULongArray;
import org.epics.pvdata.factory.BasePVUShort;
import org.epics.pvdata.factory.BasePVUShortArray;
import org.epics.pvdata.pv.DeserializableControl;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.SerializableControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.esss.ics.masar.epics.config.EpicsServiceTestConfig;
import se.esss.ics.masar.epics.exception.PVConversionException;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { EpicsServiceTestConfig.class }) })
public class SnapshotPvFactoryTest {

	@Autowired
	private PVStructure alarm;

	@Autowired
	private PVStructure time;
	
	private ConfigPv configPv;
	
	@Before
	public void init() {
		configPv = ConfigPv.builder().id(7).pvName("whatever").build();
	}

	@Test(expected = PVConversionException.class)
	public void testUnsupportedType() {

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { new UnsupportedPVField(), alarm, time });

		SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);
	}

	@Test
	public void testIntValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt value = new BasePVInt(scalar);
		value.put(7);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Integer> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(7, snapshotPv.getValue().intValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUIntValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvUInt);
		BasePVUInt value = new BasePVUInt(scalar);
		value.put(7);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Integer> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(7, snapshotPv.getValue().intValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testByteValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvByte);
		BasePVByte value = new BasePVByte(scalar);
		value.put((byte) 1);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Byte> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(1, snapshotPv.getValue().byteValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUByteValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvUByte);
		BasePVUByte value = new BasePVUByte(scalar);
		value.put((byte) 1);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Byte> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(1, snapshotPv.getValue().byteValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testShortValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvShort);
		BasePVShort value = new BasePVShort(scalar);
		value.put((short) 1);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Short> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(1, snapshotPv.getValue().shortValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUShortValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvUShort);
		BasePVUShort value = new BasePVUShort(scalar);
		value.put((short) 1);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Short> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(1, snapshotPv.getValue().shortValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testLongValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvLong);
		BasePVLong value = new BasePVLong(scalar);
		value.put(10L);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Long> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(10L, snapshotPv.getValue().longValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testULongValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvULong);
		BasePVULong value = new BasePVULong(scalar);
		value.put(10L);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Long> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(10, snapshotPv.getValue().longValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testDoubleValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvDouble);
		BasePVDouble value = new BasePVDouble(scalar);
		value.put(7.7);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Double> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(7.7, snapshotPv.getValue().doubleValue(), 0);
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testFloatValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvFloat);
		BasePVFloat value = new BasePVFloat(scalar);
		value.put(new Float(7.7));

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Float> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(7.7, snapshotPv.getValue().floatValue(), 0.000001);
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testBooleanValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvBoolean);
		BasePVBoolean value = new BasePVBoolean(scalar);
		value.put(true);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<Boolean> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals(true, snapshotPv.getValue().booleanValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testStringValue() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvString);
		BasePVString value = new BasePVString(scalar);
		value.put("string");

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<String> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertEquals("string", snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testDoubleArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvDouble);

		BasePVDoubleArray value = new BasePVDoubleArray(array);
		value.put(0, 2, new double[] { 1.1, 2.2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<double[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new double[] { 1.1, 2.2 }, snapshotPv.getValue(), 0);
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testFloatArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvFloat);

		BasePVFloatArray value = new BasePVFloatArray(array);
		value.put(0, 2, new float[] { (float) 1.1, (float) 2.2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<float[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new float[] { (float) 1.1, (float) 2.2 }, snapshotPv.getValue(), (float) 0.000001);
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testBooleanArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvBoolean);

		BasePVBooleanArray value = new BasePVBooleanArray(array);
		value.put(0, 2, new boolean[] { true, false }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<String[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new String[] { "true", "false" }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testShortArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvShort);

		BasePVShortArray value = new BasePVShortArray(array);
		value.put(0, 2, new short[] { (short) 1, (short) 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<short[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new short[] { (short) 1, (short) 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUShortArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvUShort);

		BasePVUShortArray value = new BasePVUShortArray(array);
		value.put(0, 2, new short[] { (short) 1, (short) 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<short[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new short[] { (short) 1, (short) 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testByteArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvByte);

		BasePVByteArray value = new BasePVByteArray(array);
		value.put(0, 2, new byte[] { (byte) 1, (byte) 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<byte[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new byte[] { (byte) 1, (byte) 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUByteArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvUByte);

		BasePVUByteArray value = new BasePVUByteArray(array);
		value.put(0, 2, new byte[] { (byte) 1, (byte) 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<byte[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new byte[] { (byte) 1, (byte) 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testIntArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvInt);

		BasePVIntArray value = new BasePVIntArray(array);
		value.put(0, 2, new int[] { 1, 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<int[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new int[] { 1, 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testUIntArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvUInt);

		BasePVUIntArray value = new BasePVUIntArray(array);
		value.put(0, 2, new int[] { 1, 2 }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<int[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new int[] { 1, 2 }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testLongArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvLong);

		BasePVLongArray value = new BasePVLongArray(array);
		value.put(0, 2, new long[] { 1L, 2L }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<long[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new long[] { 1L, 2L }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testULongArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvULong);

		BasePVULongArray value = new BasePVULongArray(array);
		value.put(0, 2, new long[] { 1L, 2L }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<long[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new long[] { 1L, 2L }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	@Test
	public void testStringArray() {
		ScalarArray array = PVFactory.getFieldCreate().createScalarArray(ScalarType.pvString);

		BasePVStringArray value = new BasePVStringArray(array);
		value.put(0, 2, new String[] { "a", "b" }, 0);

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" }, new PVField[] { value, getAlarm(), getTime() });

		SnapshotPv<String[]> snapshotPv = SnapshotPvFactory.createSnapshotPv(configPv, pvStructureCombined);

		assertArrayEquals(new String[] { "a", "b" }, snapshotPv.getValue());
		assertEquals(4, snapshotPv.getSeverity());
		assertEquals(5, snapshotPv.getStatus());
		assertEquals(1000, snapshotPv.getTime());
		assertEquals(7777, snapshotPv.getTimens());
	}

	private PVStructure getAlarm() {
		Scalar scalarSeverity = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntSeverity = new BasePVInt(scalarSeverity);
		basePVIntSeverity.put(4);

		Scalar scalarStatus = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntStatus = new BasePVInt(scalarStatus);
		basePVIntStatus.put(5);

		Scalar scalarMessage = PVFactory.getFieldCreate().createScalar(ScalarType.pvString);
		BasePVString basePVStringMessage = new BasePVString(scalarMessage);
		basePVStringMessage.put("SERIOUS_ALARM");

		return PVFactory.getPVDataCreate().createPVStructure(new String[] { "severity", "status", "message" },
				new PVField[] { basePVIntSeverity, basePVIntStatus, basePVStringMessage });
	}

	private PVStructure getAlarmNoSeverity() {

		Scalar scalarStatus = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntStatus = new BasePVInt(scalarStatus);
		basePVIntStatus.put(5);

		Scalar scalarMessage = PVFactory.getFieldCreate().createScalar(ScalarType.pvString);
		BasePVString basePVStringMessage = new BasePVString(scalarMessage);
		basePVStringMessage.put("SERIOUS_ALARM");

		return PVFactory.getPVDataCreate().createPVStructure(new String[] { "status", "message" },
				new PVField[] { basePVIntStatus, basePVStringMessage });
	}

	private PVStructure getAlarmNoStatus() {

		Scalar scalarSeverity = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntSeverity = new BasePVInt(scalarSeverity);
		basePVIntSeverity.put(4);

		Scalar scalarMessage = PVFactory.getFieldCreate().createScalar(ScalarType.pvString);
		BasePVString basePVStringMessage = new BasePVString(scalarMessage);
		basePVStringMessage.put("SERIOUS_ALARM");

		return PVFactory.getPVDataCreate().createPVStructure(new String[] { "severity", "message" },
				new PVField[] { basePVIntSeverity, basePVStringMessage });
	}

	private PVStructure getTime() {
		Scalar scalarSeconsPastEpoch = PVFactory.getFieldCreate().createScalar(ScalarType.pvLong);
		BasePVLong basePVLongSecondsPastEpoch = new BasePVLong(scalarSeconsPastEpoch);
		basePVLongSecondsPastEpoch.put(1000L);

		Scalar scalarNanoSeconds = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntNanoSeconds = new BasePVInt(scalarNanoSeconds);
		basePVIntNanoSeconds.put(7777);

		Scalar scalarUserTag = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntUserTag = new BasePVInt(scalarUserTag);
		basePVIntUserTag.put(10);

		return PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "secondsPastEpoch", "nanoseconds", "userTag" },
				new PVField[] { basePVLongSecondsPastEpoch, basePVIntNanoSeconds, basePVIntUserTag });

	}
	
	private PVStructure getTimeNoSecondsPastEpoch() {
	
		Scalar scalarNanoSeconds = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntNanoSeconds = new BasePVInt(scalarNanoSeconds);
		basePVIntNanoSeconds.put(7777);

		Scalar scalarUserTag = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntUserTag = new BasePVInt(scalarUserTag);
		basePVIntUserTag.put(10);

		return PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "nanoseconds", "userTag" },
				new PVField[] { basePVIntNanoSeconds, basePVIntUserTag });

	}
	
	private PVStructure getTimeNoNanoSeconds() {
		
		Scalar scalarSeconsPastEpoch = PVFactory.getFieldCreate().createScalar(ScalarType.pvLong);
		BasePVLong basePVLongSecondsPastEpoch = new BasePVLong(scalarSeconsPastEpoch);
		basePVLongSecondsPastEpoch.put(1000L);

		Scalar scalarUserTag = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt basePVIntUserTag = new BasePVInt(scalarUserTag);
		basePVIntUserTag.put(10);

		return PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "secondsPastEpoch", "userTag" },
				new PVField[] { basePVLongSecondsPastEpoch, basePVIntUserTag });

	}

	private class UnsupportedPVField extends AbstractPVScalar {

		public UnsupportedPVField() {
			super(PVFactory.getFieldCreate().createScalar(ScalarType.pvUInt));
		}

		@Override
		public void deserialize(ByteBuffer byteBuffer, DeserializableControl deserializableControl) {

		}

		@Override
		public void serialize(ByteBuffer byteBuffer, SerializableControl deserializableControl) {

		}
	}

	@Test
	public void testVerifyPVStructure() {

		PVStructure pvStructureCombined = PVFactory.getPVDataCreate()
				.createPVStructure(new String[] { "alarm", "timeStamp" }, new PVField[] { getAlarm(), getTime() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}

		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvFloat);
		BasePVFloat value = new BasePVFloat(scalar);
		value.put(new Float(7.7));

		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(new String[] { "value", "timeStamp" },
				new PVField[] { value, getTime() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}

		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(new String[] { "value", "alarm" },
				new PVField[] { value, getAlarm() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}

		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { value, getAlarmNoSeverity(), getTime() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}
		
		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { value, getAlarmNoStatus(), getTime() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}
		
		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { value, getAlarm(), getTimeNoSecondsPastEpoch() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}
		
		pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { value, getAlarm(), getTimeNoNanoSeconds() });

		try {
			SnapshotPvFactory.verifyRequiredFields(pvStructureCombined);
			fail(PVConversionException.class.getName() + " expected here");
		} catch (PVConversionException e) {
		}
	}
}

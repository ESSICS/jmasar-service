/** 
 * Copyright (C) ${year} European Spallation Source ERIC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.esss.ics.masar.epics.util;

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
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;

import se.esss.ics.masar.epics.exception.PVConversionException;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

public class SnapshotPvFactory {
	
	private SnapshotPvFactory() {
		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> SnapshotPv<T> createSnapshotPv(ConfigPv configPv, PVStructure pvStructure){
		
		verifyRequiredFields(pvStructure);
		
		PVField valueField = pvStructure.getSubField("value");

		SnapshotPv snapshotPv = getSnapshot(valueField);
		snapshotPv.setConfigPv(configPv);
		
		PVStructure alarmField = (PVStructure)pvStructure.getSubField("alarm");
		snapshotPv.setSeverity(((BasePVInt)alarmField.getSubField("severity")).get());
		snapshotPv.setStatus(((BasePVInt)alarmField.getSubField("status")).get());
		
		PVStructure timeStampField = (PVStructure)pvStructure.getSubField("timeStamp");
		snapshotPv.setTime(((BasePVLong)timeStampField.getSubField("secondsPastEpoch")).get());
		snapshotPv.setTimens(((BasePVInt)timeStampField.getSubField("nanoseconds")).get());

		// PV successfully read and transformed
		snapshotPv.setFetchStatus(true);

		return snapshotPv;
	}
	
	
	protected static void verifyRequiredFields(PVStructure pvStructure){
		
		if(pvStructure.getSubField("value") == null) {
			throw new PVConversionException("Value field missingin PV data");
		}
		
		PVStructure alarmStructure = (PVStructure)pvStructure.getSubField("alarm");
		
		if(alarmStructure == null) {
			throw new PVConversionException("Alarm field missing in PV data");
		}
		
		if(alarmStructure.getSubField("severity") == null || alarmStructure.getSubField("status") == null) {
			throw new PVConversionException("One or more alarm sub-fields missing in PV data");
		}
		
		PVStructure timestampStructure = (PVStructure)pvStructure.getSubField("timeStamp");
		
		if(timestampStructure == null) {
			throw new PVConversionException("Timestamp field missing in PV data");
		}
	
		if(timestampStructure.getSubField("secondsPastEpoch") == null || timestampStructure.getSubField("nanoseconds") == null) {
			throw new PVConversionException("One or more timeStamp sub-fields missing in PV data");
		}
	}

	@SuppressWarnings("rawtypes")
	private static SnapshotPv getSnapshot(PVField pvField){

		if (pvField instanceof BasePVDouble) {
			BasePVDouble fieldValue = (BasePVDouble) pvField;
			SnapshotPv<Double> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVFloat) {
			BasePVFloat fieldValue = (BasePVFloat) pvField;
			SnapshotPv<Float> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVBoolean) {
			BasePVBoolean fieldValue = (BasePVBoolean) pvField;
			SnapshotPv<Boolean> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVString) {
			BasePVString fieldValue = (BasePVString) pvField;
			SnapshotPv<String> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVLong) {
			BasePVLong fieldValue = (BasePVLong) pvField;
			SnapshotPv<Long> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVULong) {
			BasePVULong fieldValue = (BasePVULong) pvField;
			SnapshotPv<Long> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVByte) {
			BasePVByte fieldValue = (BasePVByte) pvField;
			SnapshotPv<Byte> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVUByte) {
			BasePVUByte fieldValue = (BasePVUByte) pvField;
			SnapshotPv<Byte> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVShort) {
			BasePVShort fieldValue = (BasePVShort) pvField;
			SnapshotPv<Short> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVUShort) {
			BasePVUShort fieldValue = (BasePVUShort) pvField;
			SnapshotPv<Short> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVInt) {
			BasePVInt fieldValue = (BasePVInt) pvField;
			SnapshotPv<Integer> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVUInt) {
			BasePVUInt fieldValue = (BasePVUInt) pvField;
			SnapshotPv<Integer> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(fieldValue.get());
			return snapshotPv;
		} else if (pvField instanceof BasePVDoubleArray) {
			BasePVDoubleArray fieldValue = (BasePVDoubleArray) pvField;
			double[] to = new double[fieldValue.getLength()];
			ConvertFactory.getConvert().toDoubleArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<double[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVFloatArray) {
			BasePVFloatArray fieldValue = (BasePVFloatArray) pvField;
			float[] to = new float[fieldValue.getLength()];
			ConvertFactory.getConvert().toFloatArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<float[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVBooleanArray) {
			BasePVBooleanArray fieldValue = (BasePVBooleanArray) pvField;
			String[] to = new String[fieldValue.getLength()];
			ConvertFactory.getConvert().toStringArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<String[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVByteArray) {
			BasePVByteArray fieldValue = (BasePVByteArray) pvField;
			byte[] to = new byte[fieldValue.getLength()];
			ConvertFactory.getConvert().toByteArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<byte[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVUByteArray) {
			BasePVUByteArray fieldValue = (BasePVUByteArray) pvField;
			byte[] to = new byte[fieldValue.getLength()];
			ConvertFactory.getConvert().toByteArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<byte[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		}  else if (pvField instanceof BasePVIntArray) {
			BasePVIntArray fieldValue = (BasePVIntArray) pvField;
			int[] to = new int[fieldValue.getLength()];
			ConvertFactory.getConvert().toIntArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<int[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVUIntArray) {
			BasePVUIntArray fieldValue = (BasePVUIntArray) pvField;
			int[] to = new int[fieldValue.getLength()];
			ConvertFactory.getConvert().toIntArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<int[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVLongArray) {
			BasePVLongArray fieldValue = (BasePVLongArray) pvField;
			long[] to = new long[fieldValue.getLength()];
			ConvertFactory.getConvert().toLongArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<long[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVULongArray) {
			BasePVULongArray fieldValue = (BasePVULongArray) pvField;
			long[] to = new long[fieldValue.getLength()];
			ConvertFactory.getConvert().toLongArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<long[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVShortArray) {
			BasePVShortArray fieldValue = (BasePVShortArray) pvField;
			short[] to = new short[fieldValue.getLength()];
			ConvertFactory.getConvert().toShortArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<short[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVUShortArray) {
			BasePVUShortArray fieldValue = (BasePVUShortArray) pvField;
			short[] to = new short[fieldValue.getLength()];
			ConvertFactory.getConvert().toShortArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<short[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else if (pvField instanceof BasePVStringArray) {
			BasePVStringArray fieldValue = (BasePVStringArray) pvField;
			String[] to = new String[fieldValue.getLength()];
			ConvertFactory.getConvert().toStringArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<String[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} else {
			throw new PVConversionException("Encountered unexpected PV field type: " + pvField.getClass().getName());
		}
	}

	private static int getDtype(PVField pvField) {
		// TODO determine how to set the dtype field from the PVField type
		return 0;
	}
}

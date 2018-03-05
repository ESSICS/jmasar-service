package se.esss.ics.masar.epics.util;

import org.epics.pvdata.factory.BasePVBoolean;
import org.epics.pvdata.factory.BasePVBooleanArray;
import org.epics.pvdata.factory.BasePVDouble;
import org.epics.pvdata.factory.BasePVDoubleArray;
import org.epics.pvdata.factory.BasePVInt;
import org.epics.pvdata.factory.BasePVIntArray;
import org.epics.pvdata.factory.BasePVLong;
import org.epics.pvdata.factory.BasePVString;
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;

import se.esss.ics.masar.model.snapshot.SnapshotPv;

public class SnapshotPvFactory {

	public static SnapshotPv createSnapshotPv(PVStructure pvStructure) {

		SnapshotPv snapshotPv = new SnapshotPv();

		for (PVField pvField : pvStructure.getPVFields()) {

			String fieldName = pvField.getFieldName();

			if ("value".equals(fieldName)) {
				snapshotPv = setPVValueAndType(snapshotPv, pvField);
			} else if ("alarm".equals(fieldName)) {
				snapshotPv = setAlarm(snapshotPv, pvField);
			} else if ("timeStamp".equals(fieldName)) {
				snapshotPv = setTimestamp(snapshotPv, pvField);
			}
		}
		
		// PV successfully read and transformed
		snapshotPv.setFetchStatus(true);
		
		return snapshotPv;
	}

	private static Object getValue(PVField pvField) {

		if (pvField instanceof BasePVDouble) {
			BasePVDouble fieldValue = (BasePVDouble) pvField;
			return fieldValue.get();
		} else if (pvField instanceof BasePVBoolean) {
			BasePVBoolean fieldValue = (BasePVBoolean) pvField;
			return fieldValue.get();
		} else if (pvField instanceof BasePVString) {
			BasePVString fieldValue = (BasePVString) pvField;
			return fieldValue.get();
		} else if (pvField instanceof BasePVLong) {
			BasePVLong fieldValue = (BasePVLong) pvField;
			return fieldValue.get();
		} else if (pvField instanceof BasePVInt) {
			BasePVInt fieldValue = (BasePVInt) pvField;
			return fieldValue.get();
		} else if (pvField instanceof BasePVDoubleArray) {
			BasePVDoubleArray fieldValue = (BasePVDoubleArray) pvField;
			double[] to = new double[fieldValue.getLength()];
			ConvertFactory.getConvert().toDoubleArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			return to;
		} else if (pvField instanceof BasePVIntArray) {
			BasePVIntArray fieldValue = (BasePVIntArray) pvField;
			int[] to = new int[fieldValue.getLength()];
			ConvertFactory.getConvert().toIntArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			return to;
		} else if (pvField instanceof BasePVBooleanArray) {
			BasePVBooleanArray fieldValue = (BasePVBooleanArray) pvField;
			int[] to = new int[fieldValue.getLength()];
			ConvertFactory.getConvert().toIntArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			return to;
		}
		else {
			return null;
		}
	}
	
	private static SnapshotPv setPVValueAndType(SnapshotPv snapshotPv, PVField pvField) {
		
		snapshotPv.setValue(getValue(pvField));
		snapshotPv.setDtype(getDtype(pvField));
		
		return snapshotPv;
	}
	
	private static int getDtype(PVField pvField) {
		// TODO determine how to set the dtype field from the PVField type
		return 0;
	}

	private static SnapshotPv setAlarm(SnapshotPv snapshotPv, PVField pvField) {

		for (PVField field : ((PVStructure) pvField).getPVFields()) {
			String fieldName = field.getFieldName();

			if ("severity".equals(fieldName)) {
				snapshotPv.setSeverity((Integer)getValue(field));
			} else if ("status".equals(fieldName)) {
				snapshotPv.setStatus((Integer)getValue(field));
			}
		}

		return snapshotPv;
	}
	
	private static SnapshotPv setTimestamp(SnapshotPv snapshotPv, PVField pvField) {
	
		for(PVField field : ((PVStructure)pvField).getPVFields()) {
			String fieldName = field.getFieldName();
			
			if("secondsPastEpoch".equals(fieldName)) {
				snapshotPv.setTime((Long)getValue(field));
			}
			else if("nanoseconds".equals(fieldName)) {
				snapshotPv.setTimens((Integer)getValue(field));
			}
		}
		
		return snapshotPv;
	}

}

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

	public static <T> SnapshotPv<T> createSnapshotPv(PVStructure pvStructure) {

		SnapshotPv snapshotPv = null; 

		for (PVField pvField : pvStructure.getPVFields()) {

			String fieldName = pvField.getFieldName();

			if ("value".equals(fieldName)) {
				snapshotPv = getSnapshot(pvField);
				if(snapshotPv == null) {
					return snapshotPv;
				}
				snapshotPv.setDtype(getDtype(pvField));
				break;
			} 
		}
		
		for (PVField pvField : pvStructure.getPVFields()) {

			String fieldName = pvField.getFieldName();
			if ("alarm".equals(fieldName)) {
				snapshotPv = setAlarm(snapshotPv, pvField);
			} else if ("timeStamp".equals(fieldName)) {
				snapshotPv = setTimestamp(snapshotPv, pvField);
			}
		}
		
		// PV successfully read and transformed
		snapshotPv.setFetchStatus(true);
		
		return snapshotPv;
	}
	
	private static SnapshotPv getSnapshot(PVField pvField) {

		if (pvField instanceof BasePVDouble) {
			BasePVDouble fieldValue = (BasePVDouble) pvField;
			SnapshotPv<Double> snapshotPv = new SnapshotPv<>();
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
		} else if (pvField instanceof BasePVInt) {
			BasePVInt fieldValue = (BasePVInt) pvField;
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
		} else if (pvField instanceof BasePVIntArray || pvField instanceof BasePVBooleanArray) {
			BasePVIntArray fieldValue = (BasePVIntArray) pvField;
			int[] to = new int[fieldValue.getLength()];
			ConvertFactory.getConvert().toIntArray(fieldValue, 0, fieldValue.getLength(), to, 0);
			SnapshotPv<int[]> snapshotPv = new SnapshotPv<>();
			snapshotPv.setValue(to);
			return snapshotPv;
		} 
		else {
			return null;
		}
	}
	
	
	private static int getDtype(PVField pvField) {
		// TODO determine how to set the dtype field from the PVField type
		return 0;
	}

	private static SnapshotPv setAlarm(SnapshotPv snapshotPv, PVField pvField) {

		for (PVField field : ((PVStructure) pvField).getPVFields()) {
			String fieldName = field.getFieldName();
			
			if ("severity".equals(fieldName)) {
				BasePVInt basePvInt = (BasePVInt)field;
				snapshotPv.setSeverity(basePvInt.get());
			} else if ("status".equals(fieldName)) {
				BasePVInt basePvInt = (BasePVInt)field;
				snapshotPv.setStatus(basePvInt.get());
			}
		}

		return snapshotPv;
	}
	
	private static SnapshotPv setTimestamp(SnapshotPv snapshotPv, PVField pvField) {
	
		for(PVField field : ((PVStructure)pvField).getPVFields()) {
			String fieldName = field.getFieldName();
			
			if("secondsPastEpoch".equals(fieldName)) {
				BasePVLong basePvLong = (BasePVLong)field;
				snapshotPv.setTime(basePvLong.get());
			}
			else if("nanoseconds".equals(fieldName)) {
				BasePVInt basePvInt = (BasePVInt)field;
				snapshotPv.setTimens(basePvInt.get());
			}
		}
		
		return snapshotPv;
	}

}

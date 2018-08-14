package se.esss.ics.masar.epics.impl;

import org.epics.pvaClient.PvaClient;
import org.epics.pvaClient.PvaClientGetData;
import org.epics.pvdata.pv.PVStructure;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.epics.util.SnapshotPvFactory;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

public class EpicsService implements IEpicsService {

	@Autowired
	private PvaClient pvaClient;

	@Override
	public <T> SnapshotPv<T> getPv(ConfigPv configPv) throws PVReadException {

		PvaClientGetData pvaClientGetData;
		try {
			pvaClientGetData = pvaClient.channel(configPv.getPvName(), "ca", 3.0).get().getData();
			PVStructure myPVStructure = pvaClientGetData.getPVStructure();
			return SnapshotPvFactory.createSnapshotPv(configPv, myPVStructure);
		} catch (Exception e1) {
			LoggerFactory.getLogger(EpicsService.class).error(e1.getMessage());
			return SnapshotPv.<T>builder().fetchStatus(false).configPv(configPv).build();
		}		
	}
}

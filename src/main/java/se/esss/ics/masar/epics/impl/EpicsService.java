package se.esss.ics.masar.epics.impl;

import org.epics.pvaClient.PvaClient;
import org.epics.pvaClient.PvaClientGetData;
import org.epics.pvdata.pv.PVStructure;
import org.springframework.beans.factory.annotation.Autowired;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.epics.util.SnapshotPvFactory;
import se.esss.ics.masar.model.snapshot.SnapshotPv;

public class EpicsService implements IEpicsService {

	@Autowired
	private PvaClient pvaClient;

	@Override
	public <T>  SnapshotPv<T> getPv(String pvName) throws PVReadException{

		if (pvaClient == null)
			pvaClient = PvaClient.get("pva ca");

		try {
			PvaClientGetData pvaClientGetData = pvaClient.channel(pvName, "ca", 2.0).get().getData();
			PVStructure myPVStructure = pvaClientGetData.getPVStructure();
			
			return SnapshotPvFactory.createSnapshotPv(myPVStructure);
		} catch (Exception e) {
			throw new PVReadException("Unable to read PV " + pvName);
		}
	}
}

package se.esss.ics.masar.epics;

import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.model.snapshot.SnapshotPv;

public interface IEpicsService {

	public SnapshotPv getPv(String pvName) throws PVReadException;
}

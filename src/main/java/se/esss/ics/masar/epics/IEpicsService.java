package se.esss.ics.masar.epics;

import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

public interface IEpicsService {

	public <T> SnapshotPv<T> getPv(ConfigPv configPv) throws PVReadException;
}

package se.esss.ics.masar.persistence.h2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.h2.tools.TriggerAdapter;

public class H2Trigger extends TriggerAdapter {

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		newRow.updateTimestamp("last_modified", Timestamp.from(Instant.now()));
	}
}

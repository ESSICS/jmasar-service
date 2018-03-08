package se.esss.ics.masar.model.snapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotPv<T> {

	private int id;
	private int dtype;
	private int severity;
	private int status;
	private long time;
	private int timens;
	private T value;
	private boolean fetchStatus;
}

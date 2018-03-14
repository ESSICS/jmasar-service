package se.esss.ics.masar.epics.config;

import org.epics.pvaClient.PvaClient;
import org.epics.pvaClient.PvaClientChannel;
import org.epics.pvaClient.PvaClientGet;
import org.epics.pvaClient.PvaClientGetData;
import org.epics.pvaccess.PVFactory;
import org.epics.pvdata.factory.BasePVInt;
import org.epics.pvdata.factory.BasePVLong;
import org.epics.pvdata.factory.BasePVString;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarType;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.impl.EpicsService;

import static org.mockito.Mockito.*;

@Configuration
public class EpicsServiceTestConfig {

	@Bean
	public PvaClient pvaClient() {
		PvaClient pvaClient = Mockito.mock(PvaClient.class);
		
		PvaClientChannel pvaClientChannel = mock(PvaClientChannel.class);
		PvaClientGetData pvaClientGetData = mock(PvaClientGetData.class);
		PvaClientGet pvaClientGet = mock(PvaClientGet.class);
		
		
		when(pvaClientGetData.getPVStructure()).thenReturn(getDefaultPVStructure());
		when(pvaClientGet.getData()).thenReturn(pvaClientGetData);
		when(pvaClientChannel.get()).thenReturn(pvaClientGet);
		
		when(pvaClient.channel(anyString(), anyString(), anyDouble())).thenReturn(pvaClientChannel);
		when(pvaClient.channel(anyString(), anyString(), anyDouble())).thenAnswer(new Answer<PvaClientChannel>() {
			
			@Override
			public PvaClientChannel answer(InvocationOnMock invocation) {
				String channelName = invocation.getArgumentAt(0, String.class);
				if("channelName".equals(channelName)) {
					return pvaClientChannel;
				}
				else {
					throw new RuntimeException("Unable to read channel " + channelName);
				}
			}
		});
		return pvaClient;
	}
	
	@Bean
	public IEpicsService epicsService() {
		return new EpicsService();
	}
	
	@Bean("defaultPVStructure")
	public PVStructure getDefaultPVStructure() {
		Scalar scalar = PVFactory.getFieldCreate().createScalar(ScalarType.pvInt);
		BasePVInt value = new BasePVInt(scalar);
		value.put(7);
		
		PVStructure pvStructureCombined = PVFactory.getPVDataCreate().createPVStructure(
				new String[] { "value", "alarm", "timeStamp" },
				new PVField[] { value, getAlarm(), getTime() });
		
		return pvStructureCombined;
	}
	
	@Bean("alarm")
	public PVStructure getAlarm() {
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
	
	@Bean("time")
	public PVStructure getTime() {
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
}

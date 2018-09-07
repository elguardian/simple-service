package org.kie.simple;

import java.util.Map;
import java.util.Optional;

import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.SLAViolatedEvent;

public class SLAViolationHandler extends DefaultProcessEventListener {

	public SLAViolationHandler() {
		System.out.println("SLAViolationHandler event listener loaded");
	}

	@Override
	public void beforeSLAViolated(SLAViolatedEvent event) {
		System.out.println("About to trigger SLA violation event for " + event.getProcessInstance().getProcessName());
	}

	private Optional<SLAInfo> getSLAInfo(SLAViolatedEvent event) {

		
		if (event != null && event.getProcessInstance() != null && event.getNodeInstance() != null) {
			Object obj = ((WorkflowProcessInstance) event.getProcessInstance()).getVariable("slaMap");
			
			Long procInstId = event.getProcessInstance().getId();
			Long nodeInstId = event.getNodeInstance().getId();
			if (obj != null && obj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> slaMap = (Map<String, Object>) obj;
				String key = SLAInfo.generateKey(procInstId, nodeInstId);
				Object innerObj = slaMap.get(key);
				if (innerObj != null && innerObj instanceof SLAInfo) {
					return Optional.of((SLAInfo) innerObj);
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public void afterSLAViolated(SLAViolatedEvent event) {

		System.out.println(event.getProcessInstance().getProcessName() + " has violated an SLA");
		if (event.getNodeInstance() == null) {
			System.out.println("Unable to determine the violating node");
		}

		System.out.println("***** SLA Violated Event ***** NODE: " + event.getNodeInstance().getNodeName());
		WorkItemNodeInstance nii = (WorkItemNodeInstance) event.getNodeInstance();
		Optional<SLAInfo> info = getSLAInfo(event);
		if (!info.isPresent()) {
			System.out.println("didn't get the SLAInfo");
			return;
		}
		

		SLAInfo slaInfo = info.get();
		System.out.println("***** SLA Violated Action ***** NODE: " + event.getNodeInstance().getNodeName() + " ACTION "
				+ slaInfo.getAction());

		
		event.getKieRuntime().signalEvent(slaInfo.getRetrySignalName(), slaInfo);
		nii.cancel();
		System.out.println("***************");

	}

}

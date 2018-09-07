package org.kie.simple;

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

	@Override
	public void afterSLAViolated(SLAViolatedEvent event) {

		System.out.println(event.getProcessInstance().getProcessName() + " has violated an SLA");
		if (event.getNodeInstance() == null) {
			System.out.println("Unable to determine the violating node");
		}

		System.out.println("***** SLA Violated Event ***** NODE: " + event.getNodeInstance().getNodeName());
		WorkItemNodeInstance nii = (WorkItemNodeInstance) event.getNodeInstance();

		
		event.getKieRuntime().signalEvent("sla_violation", nii.getWorkItemId());
		nii.cancel();
		System.out.println("***************");

	}

}

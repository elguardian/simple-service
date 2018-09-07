package org.kie.simple;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.workitem.core.AbstractWorkItemHandler;
import org.jbpm.process.workitem.core.util.Wid;
import org.jbpm.process.workitem.core.util.WidParameter;
import org.jbpm.process.workitem.core.util.WidResult;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

@Wid(widfile = "ServiceHandler.wid", name = "ServiceHandler", displayName = "ServiceHandler", defaultHandler = "mvel: new org.kie.simple.ServiceHandler()", parameters = {
		@WidParameter(name = "SLAMode"), @WidParameter(name = "ViolationAction"),
		@WidParameter(name = "RetrySignalName"), @WidParameter(name = "MaxAutoRetry"),
		@WidParameter(name = "SLAGroupId") }, results = { @WidResult(name = "name") })

public class ServiceHandler extends AbstractWorkItemHandler {

	private Map<Long, Thread> threads = new ConcurrentHashMap<>();

	public ServiceHandler(KieSession ksession) {
		super(ksession);
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("EXECUTING WORKITEM " + workItem.getId());
		workItem.getParameters().keySet().forEach(key -> {
			System.out.println(key + ": " + workItem.getParameter(key));
		});

		WorkflowProcessInstance wpi = (WorkflowProcessInstance) getProcessInstance(workItem);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> slaMap = (Map<String, Object>) wpi.getVariable("slaMap");
		if (slaMap == null) {
			slaMap = new HashMap<>();
			wpi.setVariable("slaMap", slaMap);
		} 
		SLAInfo info = SLAInfoFactory.instance().createFrom(workItem);
		slaMap.put(info.getKey(), info);

		String deploymentId = ((WorkItemImpl) workItem).getDeploymentId();
		ServiceJobWorker worker = new ServiceJobWorker(getSession(), deploymentId, info);
		Thread thread = new Thread(worker);
		threads.put(workItem.getId(), thread);
		thread.start();
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("CANCELLING WORKITEM " + workItem.getId());
		Thread thread = threads.remove(workItem.getId());
		if(thread != null) {
			thread.interrupt();
		} else {
			System.out.println("thread not found for this workitem " + workItem.getId());
		}
	}

}

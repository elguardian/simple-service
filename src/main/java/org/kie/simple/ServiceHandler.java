package org.kie.simple;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.workitem.core.AbstractWorkItemHandler;
import org.jbpm.process.workitem.core.util.Wid;
import org.jbpm.process.workitem.core.util.WidResult;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

@Wid(widfile = "ServiceHandler.wid", 
	name = "ServiceHandler", 
	displayName = "ServiceHandler", 
	defaultHandler = "mvel: new org.kie.simple.ServiceHandler()", 
	parameters = {
    },
	results = { @WidResult(name = "name") }
)
public class ServiceHandler extends AbstractWorkItemHandler {

	private Map<Long, Thread> threads = new ConcurrentHashMap<>();

	public ServiceHandler(KieSession ksession) {
		super(ksession);
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("EXECUTING WORKITEM " + workItem.getId());
		
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(25000);
				Map<String, Object> output = Collections.singletonMap("name", "Gavin");
				String deploymentId = ((WorkItemImpl) workItem).getDeploymentId();
				RuntimeManager rtm = RuntimeManagerRegistry.get().getManager(deploymentId);
				if (rtm != null) {
					RuntimeEngine engine = rtm.getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
					engine.getKieSession().getWorkItemManager().completeWorkItem(workItem.getId(), output);
					rtm.disposeRuntimeEngine(engine);
				} else {
					getSession().getWorkItemManager().completeWorkItem(workItem.getId(), output);
				}
				System.out.println("COMPLETED WORKITEM " + workItem.getId());
			} catch (InterruptedException e) {
				System.out.println("CANCELLED WORKITEM " + workItem.getId());
			}

		});
		threads.put(workItem.getId(), thread);
		thread.start();
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("CANCELLING WORKITEM " + workItem.getId());
		Thread thread = threads.remove(workItem.getId());
		thread.interrupt();
	}
}

package org.kie.simple;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

public class ServiceJobWorker implements Runnable {

	private KieSession ksession;
	private String deploymentId;
	private SLAInfo slaInfo;

	public ServiceJobWorker(KieSession ksession, String deploymentId, SLAInfo slaInfo) {
		this.ksession = ksession;
		this.deploymentId = deploymentId;
		this.slaInfo = slaInfo;
	}
	@Override
	public void run() {			
		System.out.println("process started: "  + slaInfo.getWorkItemId());
		try {
			Thread.sleep(25000);
			
			// do some work
			Map<String, Object> output = new HashMap<>();
			output.put("name", "Gavin");
			
			// complete
			System.out.println("process completed: " + slaInfo.getWorkItemId());
			completeWork(output);
		} catch (InterruptedException e) {
			System.out.println("process cancelled: " + slaInfo.getWorkItemId());
		} 
	}
	
	private void completeWork(Map<String, Object> output) {
		RuntimeManager rtm = RuntimeManagerRegistry.get()
				.getManager(deploymentId == null ? "" : deploymentId);
		if (rtm != null) {
			RuntimeEngine engine = rtm.getRuntimeEngine(ProcessInstanceIdContext.get(slaInfo.getProcessId()));
			engine.getKieSession().getWorkItemManager().completeWorkItem(slaInfo.getWorkItemId(), output);
			rtm.disposeRuntimeEngine(engine);
		} else {
			ksession.getWorkItemManager().completeWorkItem(slaInfo.getWorkItemId(), output);
		}
	}
	
}

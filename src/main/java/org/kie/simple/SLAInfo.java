package org.kie.simple;

import java.io.Serializable;

public class SLAInfo implements Serializable {
	private static final long serialVersionUID = -6816580557005434422L;
	private ViolationAction action;
	private TimerMode timerMode;
	private String slaGroupId;
	private String retrySignalName;
	private Integer maxRetries;
	private Long processId;
	private Long nodeInstanceId;
	private Long workItemId;

	
	public SLAInfo(Long processId, Long nodeInstanceId) {
		super();
		this.processId = processId;
		this.nodeInstanceId = nodeInstanceId;
	}
	
	public SLAInfo(Long processId, 
			Long nodeInstanceId, 
			Long workItemId,
			ViolationAction action, 
			TimerMode timerMode, 
			String slaGroupId, 
			Integer maxRetries,
			String retrySignalName,
			String continueSignalName) {
		super();
		this.processId = processId;
		this.nodeInstanceId = nodeInstanceId;
		this.workItemId = workItemId;
		this.action = action;
		this.timerMode = timerMode;
		this.slaGroupId = slaGroupId;
		this.maxRetries = maxRetries;
		this.retrySignalName = retrySignalName;
	}
	
	public ViolationAction getAction() {
		return action;
	}
	public void setAction(ViolationAction action) {
		this.action = action;
	}
	public TimerMode getTimerMode() {
		return timerMode;
	}
	public void setTimerMode(TimerMode timerMode) {
		this.timerMode = timerMode;
	}
	public String getSlaGroupId() {
		return slaGroupId;
	}
	public void setSlaGroupId(String slaGroupId) {
		this.slaGroupId = slaGroupId;
	}
	public Integer getMaxRetries() {
		return maxRetries;
	}
	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getRetrySignalName() {
		return retrySignalName;
	}

	public void setRetrySignalName(String retrySignalName) {
		this.retrySignalName = retrySignalName;
	}

	public Long getProcessId() {
		return processId;
	}

	public Long getNodeInstanceId() {
		return nodeInstanceId;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public String getKey() {
		return generateKey(processId, nodeInstanceId);
	}
	
	public static String generateKey(Long processId, Long nodeInstanceId) {
		return String.format("%012d", processId)+"::"+String.format("%012d", nodeInstanceId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((maxRetries == null) ? 0 : maxRetries.hashCode());
		result = prime * result + ((nodeInstanceId == null) ? 0 : nodeInstanceId.hashCode());
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
		result = prime * result + ((retrySignalName == null) ? 0 : retrySignalName.hashCode());
		result = prime * result + ((slaGroupId == null) ? 0 : slaGroupId.hashCode());
		result = prime * result + ((timerMode == null) ? 0 : timerMode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SLAInfo other = (SLAInfo) obj;
		if (action != other.action)
			return false;
		if (maxRetries == null) {
			if (other.maxRetries != null)
				return false;
		} else if (!maxRetries.equals(other.maxRetries))
			return false;
		if (nodeInstanceId == null) {
			if (other.nodeInstanceId != null)
				return false;
		} else if (!nodeInstanceId.equals(other.nodeInstanceId))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		if (retrySignalName == null) {
			if (other.retrySignalName != null)
				return false;
		} else if (!retrySignalName.equals(other.retrySignalName))
			return false;
		if (slaGroupId == null) {
			if (other.slaGroupId != null)
				return false;
		} else if (!slaGroupId.equals(other.slaGroupId))
			return false;
		if (timerMode != other.timerMode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SLAInfo [action=" + action + ", timerMode=" + timerMode + ", slaGroupId=" + slaGroupId
				+ ", retrySignalName=" + retrySignalName + ", maxRetries=" + maxRetries + ", processId=" + processId
				+ ", nodeInstanceId=" + nodeInstanceId + "]";
	}


	
}

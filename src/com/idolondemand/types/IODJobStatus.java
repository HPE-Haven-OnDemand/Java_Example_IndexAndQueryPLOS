package com.idolondemand.types;

import java.util.ArrayList;

public class IODJobStatus {

	public ArrayList<IODAction> getActions() {
		return actions;
	}
	public void setActions(ArrayList<IODAction> actions) {
		this.actions = actions;
	}
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	private ArrayList<IODAction> actions;
	private String jobID;
	private String status;
	
}

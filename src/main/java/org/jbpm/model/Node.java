/**
 * 
 */
package org.jbpm.model;

import java.util.ArrayList;

/**
 * @author eschabel
 *
 */
public final class Node {
	
	private String name = "node";
	private String async = "false";
	private String actionElements;
	private ArrayList<String> nodeContentElements;


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAsync(String async) {
		this.async = async;
	}

	public String getAsync() {
		return async;
	}

	public void setActionElements(String actionElements) {
		this.actionElements = actionElements;
	}

	public String getActionElements() {
		return actionElements;
	}

	public void setNodeContentElements(ArrayList<String> nodeContentElements) {
		this.nodeContentElements = nodeContentElements;
	}

	public ArrayList<String> getNodeContentElements() {
		return nodeContentElements;
	}
	
}

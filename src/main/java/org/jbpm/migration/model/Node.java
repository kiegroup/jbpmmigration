/**
 * 
 */
package org.jbpm.migration.model;

import java.util.ArrayList;


/**
 * Class to mapping jPDL Node types to java object.
 *  
 * @author Eric D. Schabell
 *
 */
public final class Node {
	
	private String name;
	private String async = "false";
	
	// action-elements
	private String actionClass;
	private String actionConfigType;
    private String actionName;
    private String actionRefName;
    private Boolean actionAcceptPropagatedEvents = true;   // default value is true.
    private String actionExpression;
    private String actionAsyc;

    // node-content-elements
    private String nodeContentDescription;
// TODO:    private Event nodeContentEvent;
// TODO:    private ExceptionHandler nodeContentExceptionHandler;
// TODO:    private Timer nodeContentTimer;
    private ArrayList<Transition> nodeContentTransitionList;
    private Transition nodeContentTransition;
    
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

	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}

	public String getActionClass() {
		return actionClass;
	}

	public void setActionConfigType(String actionConfigType) {
		this.actionConfigType = actionConfigType;
	}

	public String getActionConfigType() {
		return actionConfigType;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionRefName(String actionRefName) {
		this.actionRefName = actionRefName;
	}

	public String getActionRefName() {
		return actionRefName;
	}

	public void setActionAcceptPropagatedEvents(
			Boolean actionAcceptPropagatedEvents) {
		this.actionAcceptPropagatedEvents = actionAcceptPropagatedEvents;
	}

	public Boolean getActionAcceptPropagatedEvents() {
		return actionAcceptPropagatedEvents;
	}

	public void setActionExpression(String actionExpression) {
		this.actionExpression = actionExpression;
	}

	public String getActionExpression() {
		return actionExpression;
	}

	public void setActionAsyc(String actionAsyc) {
		this.actionAsyc = actionAsyc;
	}

	public String getActionAsyc() {
		return actionAsyc;
	}

	public void setNodeContentDescription(String nodeContentDescription) {
		this.nodeContentDescription = nodeContentDescription;
	}

	public String getNodeContentDescription() {
		return nodeContentDescription;
	}

	public void setTransitionList(ArrayList<Transition> transitionList) {
		this.nodeContentTransitionList = transitionList;
	}

	public ArrayList<Transition> getTransitionList() {
		return nodeContentTransitionList;
	}

	public void setNodeContentTransition(Transition nodeContentTransition) {
		this.nodeContentTransition = nodeContentTransition;
	}

	public Transition getNodeContentTransition() {
		return nodeContentTransition;
	}
}

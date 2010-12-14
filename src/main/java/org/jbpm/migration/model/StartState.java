/**
 * 
 */
package org.jbpm.migration.model;



/**
 * Class to mapping jPDL start-state types to java object.
 *  
 * @author Eric D. Schabell
 *
 */
public final class StartState {
	
	private String name;

	private Transition startStateTransition;
    
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStartStateTransition(Transition startStateTransition) {
		this.startStateTransition = startStateTransition;
	}

	public Transition getStartStateTransition() {
		return startStateTransition;
	}

}

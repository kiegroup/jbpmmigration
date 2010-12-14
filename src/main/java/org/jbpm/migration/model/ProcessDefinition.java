/**
 * 
 */
package org.jbpm.migration.model;


/**
 * Class to mapping jPDL Process Definition types to java object.
 * 
 * @author Eric D. Schabell
 *
 */
public class ProcessDefinition {
	
	// Complex type that can be a description, swimlane, start-state, 
	// node-elements list, action-elemenst list, event, exception-handler 
	// or task.
	private String description;
	private String xmlns;
// TODO:    private Swimlane swimlane;;
// TODO:	private StartState startState;
	private String name;
    
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getXmlns() {
		return xmlns;
	}
    
}

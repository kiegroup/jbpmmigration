/**
 * 
 */
package org.jbpm.model;

/**
 * Class to mapping jPDL Transition types to java object.
 * 
 * @author Eric D. Schabell
 *
 */
public class Transition {

	private String description;
	private String to;
	private String name;
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

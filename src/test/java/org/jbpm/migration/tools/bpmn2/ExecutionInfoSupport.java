/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.bpmn2;

import java.util.Set;

/**
 * Interface for handlers supporting execution information. (how many times was
 * given method executed)
 */
public interface ExecutionInfoSupport {

    public Set<ExecutionInfo> getExecutionInfo();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl;

import java.util.Collection;

import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * Common helper methods for jpdl execution and diagnostics.
 */
public class JpdlHelper {

    /**
     * Finds task instance of given name.
     *
     * @param name
     *            Task name.
     * @param pi
     *            Process instance containing the task.
     * @return task instance
     * @throws IllegalArgumentException
     *             if the task instance hasn't been found.
     */
    public static TaskInstance getTaskInstance(String name, ProcessInstance pi) {
        Collection<TaskInstance> taskInstances = pi.getTaskMgmtInstance().getTaskInstances();
        for (TaskInstance ti : taskInstances) {
            if (ti.getName().equals(name)) {
                return ti;
            }
        }
        throw new IllegalArgumentException(String.format("Task instance with name \"%s\" was not found", name));
    }
}

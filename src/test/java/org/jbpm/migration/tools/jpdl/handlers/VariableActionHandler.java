/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl.handlers;

import java.util.Map;

import org.jbpm.migration.tools.jpdl.listeners.VariableChangeListener;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

/**
 * TODO: add mechanism to specify which and how variables should be changed. =>
 * List of variable names, old and new values.
 */
public class VariableActionHandler implements ActionHandler {

    private static VariableChangeListener variableListener;
    private static Map<String, Object> varChanges;

    @Override
    public void execute(ExecutionContext executionContext) throws Exception {
        ContextInstance ci = executionContext.getContextInstance();

        variableListener.recordOldValues(ci);

        makeChanges(varChanges, executionContext);

        variableListener.recordNewValues(ci);

        executionContext.getProcessInstance().signal(); // complete the node
                                                        // inside the subprocess
    }

    private void makeChanges(Map<String, Object> changes, ExecutionContext executionContext) {
        ContextInstance ci = executionContext.getContextInstance();
        ci.setVariables(changes);
    }

    public static void setVariableListener(VariableChangeListener listener) {
        variableListener = listener;
    }

    public static void setVariableChanges(Map<String, Object> changes) {
        varChanges = changes;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl.handlers;

import org.jbpm.migration.tools.jpdl.listeners.TrackingActionListener;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

/**
 * Default jpdl action handler for testing purposes.
 *
 * Tracks node and event on which is called through TrackingActionListener. When
 * <code>signalization</code> is set, signalizes on the process instance - to
 * continue the process flow.
 *
 * Could be called from BPMN2 process - by JavaNodeHandler; in this case the
 * execution context is <code>null</code>.
 */
public class DefaultActionHandler implements ActionHandler {

    private static TrackingActionListener trackingListener;

    private static boolean signalization = false;

    @Override
    public void execute(ExecutionContext executionContext) throws Exception {
        if (executionContext == null) {
            return; // called from BPMN2 process
        }

        if (trackingListener == null) {
            throw new IllegalStateException("no tracking listener has been provided!");
        }
        trackingListener.addFromContext(executionContext);

        if (signalization) {
            executionContext.getProcessInstance().signal();
        }
    }

    public static void setTrackingListener(TrackingActionListener listener) {
        trackingListener = listener;
    }

    public static void setSignalization(boolean flag) {
        signalization = flag;
    }
}

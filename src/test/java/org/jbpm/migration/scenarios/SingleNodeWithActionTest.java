package org.jbpm.migration.scenarios;

import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessCompleted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessStarted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertTriggeredAndLeft;

import org.jbpm.migration.MigrationTestWrapper;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.jpdl.handlers.DefaultActionHandler;
import org.jbpm.migration.tools.jpdl.listeners.TrackingActionListener;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.Test;

/**
 * Single node with an action inside.
 */
public class SingleNodeWithActionTest extends MigrationTestWrapper {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleNodeWithAction/processdefinition.xml";

    public static final String processId = "SingleNode_Process";

    @Test
    public void testJpdl() {
        ProcessInstance pi = processDef.createProcessInstance();

        TrackingActionListener listener = new TrackingActionListener();
        DefaultActionHandler.setTrackingListener(listener);
        DefaultActionHandler.setSignalization(true);
        pi.signal();

        JpdlAssert.assertProcessStarted(pi);
        listener.wasCalledOnNode("node");
        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        prepareProcess(definition);
        ksession = kbase.newKieSession();

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);
        assertTriggeredAndLeft(listener, "node");
        assertProcessCompleted(listener, processId);
    }
}

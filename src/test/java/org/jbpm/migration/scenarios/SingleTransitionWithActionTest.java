package org.jbpm.migration.scenarios;

import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessCompleted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessStarted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertTriggeredAndLeft;

import org.jbpm.migration.JbpmMigrationRuntimeTest;
import org.jbpm.migration.tools.bpmn2.JavaNodeHandler;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.jpdl.handlers.DefaultActionHandler;
import org.jbpm.migration.tools.jpdl.listeners.TrackingActionListener;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests migration of a node with a transition defining an action. The
 * transition should be migrated into a sequenceFlow -> scriptTask ->
 * sequenceFlow, and the action executed.
 */
public class SingleTransitionWithActionTest extends JbpmMigrationRuntimeTest {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleTransitionWithAction/processdefinition.xml";

    public static final String processId = "SingleTransitionWithAction_Process";

    @BeforeClass
    public static void getTestReady() {
        prepareProcess(definition);
    }

    @Test
    public void testJpdl() {
        ProcessInstance pi = processDef.createProcessInstance();

        TrackingActionListener listener = new TrackingActionListener();
        DefaultActionHandler.setTrackingListener(listener);
        DefaultActionHandler.setSignalization(false);
        pi.signal();

        JpdlAssert.assertProcessStarted(pi);
        listener.wasCalledOnNode("node");
        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        ksession = kbase.newKieSession();

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        JavaNodeHandler javaNodeHandler = new JavaNodeHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("JavaNode", javaNodeHandler);

        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);

        assertTriggeredAndLeft(listener, "node");

        assertProcessCompleted(listener, processId);
    }
}

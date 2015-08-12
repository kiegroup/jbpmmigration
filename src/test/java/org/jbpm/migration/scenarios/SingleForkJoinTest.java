package org.jbpm.migration.scenarios;

import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessCompleted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessStarted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertTriggeredAndLeft;

import org.jbpm.migration.MigrationTestWrapper;
import org.jbpm.migration.tools.bpmn2.JavaNodeHandler;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.jpdl.handlers.DefaultActionHandler;
import org.jbpm.migration.tools.jpdl.listeners.TrackingActionListener;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Single scenario with fork and corresponding join.
 */
public class SingleForkJoinTest extends MigrationTestWrapper {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleForkJoin/processdefinition.xml";

    public static final String processId = "singleForkJoin_Process";

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
        JpdlAssert.assertCalledOnNode(listener, "node1");
        JpdlAssert.assertCalledOnNode(listener, "node2");

        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        ksession = kbase.newKieSession();

        ksession.getWorkItemManager().registerWorkItemHandler("JavaNode", new JavaNodeHandler());

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);
        assertTriggeredAndLeft(listener, "start-state");
        assertTriggeredAndLeft(listener, "fork1");
        assertTriggeredAndLeft(listener, "node1");
        assertTriggeredAndLeft(listener, "node2");
        assertTriggeredAndLeft(listener, "join1");
        assertTriggeredAndLeft(listener, "end-state");
        assertProcessCompleted(listener, processId);
    }
}

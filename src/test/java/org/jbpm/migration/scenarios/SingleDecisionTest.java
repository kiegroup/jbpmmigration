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
import org.junit.Ignore;
import org.junit.Test;

/**
 * Scenario with single decision node.
 *
 */
public class SingleDecisionTest extends MigrationTestWrapper {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleDecision/processdefinition.xml";

    public static final String processId = "singleDecision_Process";

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
        JpdlAssert.assertCalledOnNode(listener, "end-state1");

        JpdlAssert.assertProcessCompleted(pi);
    }

    @Ignore("decision cannot be migrated to executable process definition - no way how to migrate split conditions.")
    @Test
    public
        void testBpmn() {
        ksession = kbase.newKieSession();

        ksession.getWorkItemManager().registerWorkItemHandler("JavaNode", new JavaNodeHandler());

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);
        assertTriggeredAndLeft(listener, "start-state");
        assertTriggeredAndLeft(listener, "end-state1");
        assertProcessCompleted(listener, processId);
    }
}

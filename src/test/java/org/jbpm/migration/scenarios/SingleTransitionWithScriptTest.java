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
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests migration of a node with a transition defining a script. The transition
 * should be migrated into a sequenceFlow -> scriptTask -> sequenceFlow.
 * <p/>
 * The script contents cannot be transformed automatically, but it should be
 * added as a comment into corresponding scriptTask in the result BPMN.
 */
public class SingleTransitionWithScriptTest extends MigrationTestWrapper {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleTransitionWithScript/processdefinition.xml";

    public static final String processId = "SingleTransitionWithScript_Process";

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
        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);
        assertTriggeredAndLeft(listener, "node");
        assertProcessCompleted(listener, processId);
    }
}

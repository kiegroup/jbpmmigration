package org.jbpm.migration.scenarios;

import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessCompleted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessStarted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertTriggeredAndLeft;

import org.jbpm.migration.JbpmMigrationRuntimeTest;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Testing single state. State is waiting node - external signal is needed to
 * continue.
 *
 *
 */
public class SingleStateTest extends JbpmMigrationRuntimeTest {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleState/processdefinition.xml";

    public static final String processId = "singleState_Process";

    @BeforeClass
    public static void getTestReady() {
        prepareProcess(definition);
    }

    @Test
    public void testJpdl() {
        ProcessInstance pi = processDef.createProcessInstance();
        pi.signal();
        JpdlAssert.assertProcessStarted(pi);
        pi.signal();
        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        ksession = kbase.newKieSession();

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        ksession.startProcess(processId);
        assertProcessStarted(listener, processId);

        assertTriggeredAndLeft(listener, "start-state");

        ksession.signalEvent("signal", null);

        assertTriggeredAndLeft(listener, "state");
        assertTriggeredAndLeft(listener, "end-state");

        assertProcessCompleted(listener, processId);
    }
}

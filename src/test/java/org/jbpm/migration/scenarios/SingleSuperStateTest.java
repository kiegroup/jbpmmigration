package org.jbpm.migration.scenarios;

import org.jbpm.migration.MigrationTestWrapper;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.jpdl.handlers.DefaultActionHandler;
import org.jbpm.migration.tools.jpdl.listeners.TrackingActionListener;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.migration.tools.listeners.TrackingListenerAssert;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests node group inside super state.
 *
 * Verifies https://issues.jboss.org/browse/JBPM-3693
 *
 */
public class SingleSuperStateTest extends MigrationTestWrapper {
    public static final String definition =
            "org/jbpm/migration/scenarios/singleSuperState/processdefinition.xml";

    public static final String processId = "singleSuperState_Process";

    @BeforeClass
    public static void getTestReady() {
        prepareProcess(definition);
    }

    @Test
    public void testJpdl() {
        ProcessInstance pi = processDef.createProcessInstance();

        TrackingActionListener listener = new TrackingActionListener();
        DefaultActionHandler.setTrackingListener(listener);

        pi.signal();

        JpdlAssert.assertProcessStarted(pi);

        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        ksession = kbase.newKieSession();

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        ksession.startProcess(processId);

        TrackingListenerAssert.assertProcessStarted(listener, processId);
        TrackingListenerAssert.assertProcessCompleted(listener, processId);
    }
}

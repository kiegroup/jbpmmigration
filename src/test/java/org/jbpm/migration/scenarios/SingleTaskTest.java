package org.jbpm.migration.scenarios;

import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertLeft;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessCompleted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertProcessStarted;
import static org.jbpm.migration.tools.listeners.TrackingListenerAssert.assertTriggered;

import org.assertj.core.api.Assertions;
import org.jbpm.migration.JbpmMigrationRuntimeTest;
import org.jbpm.migration.tools.jpdl.JpdlAssert;
import org.jbpm.migration.tools.jpdl.JpdlHelper;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.test.JbpmJUnitTestCase.TestWorkItemHandler;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.runtime.process.WorkItem;

/**
 * Single task test.
 *
 * task node with task inside => task. Should be userTask.
 *
 */
public class SingleTaskTest extends JbpmMigrationRuntimeTest {

    public static final String definition =
            "org/jbpm/migration/scenarios/singleTask/processdefinition.xml";

    public static final String processId = "singleTask_Process";

    @BeforeClass
    public static void getTestReady() {
        prepareProcess(definition);
    }

    @Test
    public void testJpdl() throws Exception {
        ProcessInstance pi = processDef.createProcessInstance();
        pi.signal();
        JpdlAssert.assertProcessStarted(pi);

        TaskInstance ti = JpdlHelper.getTaskInstance("Test task", pi);
        Assertions.assertThat(ti.getActorId()).isEqualTo("EXPERT");
        ti.end();

        JpdlAssert.assertTaskEnded(ti);
        JpdlAssert.assertProcessCompleted(pi);
    }

    @Test
    public void testBpmn() {
        ksession = kbase.newKieSession();

        TrackingProcessEventListener listener = new TrackingProcessEventListener();
        ksession.addEventListener(listener);

        TestWorkItemHandler handler = new TestWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);

        ksession.startProcess(processId);

        assertProcessStarted(listener, processId);

        assertTriggered(listener, "human-task");
        WorkItem wi = handler.getWorkItem();
        ksession.getWorkItemManager().completeWorkItem(wi.getId(), null);

        assertLeft(listener, "human-task");
        assertProcessCompleted(listener, processId);
    }

}

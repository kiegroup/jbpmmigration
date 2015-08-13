package org.jbpm.migration.scenarios;

import org.assertj.core.api.Assertions;
import org.jbpm.migration.JbpmMigrationRuntimeTest;
import org.junit.Test;

/**
 * Insurance - InitializeAndValidateProcess scenario.
 *
 */
@org.junit.Ignore("look at JIRA https://issues.jboss.org/browse/JBPM-4313")
public class InitializeAndValidateTest extends JbpmMigrationRuntimeTest {
    public static final String definition =
            "org/jbpm/migration/scenarios/insuranceInitializeAndValidateProcess/processdefinition.xml";

    public static final String processId = "Insurance_InitializeValidatingProcess_Process";

    @Test
    public void testBpmn() {
        prepareProcess(definition);
        Assertions.assertThat(kbase).isNotNull();
    }
}
/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.migration.xsl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbpm.migration.JbpmMigration;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Base class for tests for the jPDL process definition transformer with JAXP.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public abstract class AbstractJpdl3Test {
    // XSLT sheet.
    private static final String XSLT_SHEET = "src/main/resources/jpdl3-bpmn2.xsl";

    // Results file of transformation.
    private static final String RESULTS_FILE = "target/processdefinition.bpmn.xml";

    /** Allow for subclasses to override the JPDL validation, as some (ESB) processes are not valid. */
    protected boolean validateJpdl = true;

    @BeforeClass
    public static void oneTimeSetUp() {
        // Set up Log4J.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // Make sure the style sheet is available.
        final File xsltSheet = new File(XSLT_SHEET);
        assertThat("Stylesheet missing.", xsltSheet.exists(), is(true));
    }

    /**
     * Transform the input file to an output file.
     */
    @Test
    public void transformjPDL3() throws Exception {
        // Make sure the input file is available.
        final File jpdl = new File(getJpdlFile());
        assertThat("Indicated input file missing.", jpdl.exists(), is(true));

        if (validateJpdl) {
            // Validate the input process definition.
            assertThat("Not a valid jPDL definition.", JbpmMigration.validateJpdl(FileUtils.readFileToString(jpdl)), is(true));
        }

        // Remove any previously existing output first.
        File bpmn = new File(RESULTS_FILE);
        if (bpmn.exists()) {
            assertThat("Unable to clean output.", bpmn.delete(), is(true));
        }

        // Transform the input file; creates the output file.
        JbpmMigration.main(new String[] { jpdl.getPath(), XSLT_SHEET, RESULTS_FILE });

        // Make sure the output file is available.
        bpmn = new File(RESULTS_FILE);
        assertThat("Expected output file missing.", bpmn.exists(), is(true));

        // Validate the output process definition.
        assertThat("Not a valid BPMN definition.", JbpmMigration.validateBpmn(FileUtils.readFileToString(bpmn)), is(true));
    }

    protected abstract String getJpdlFile();
}

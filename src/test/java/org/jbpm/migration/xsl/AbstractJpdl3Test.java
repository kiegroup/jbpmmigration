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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.MessageFormat;

import javax.xml.transform.TransformerException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Base class for tests for the jPDL process definition transformer with JAXP.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public abstract class AbstractJpdl3Test {
    // XSLT sheet.
    private static final String XSLT_SHEET = "src/main/resources/jpdl3-bpmn2.xsl";

    // Results file of transformation using a Format.
    private static final String RESULTS_FILE_FORMAT = "target/migrated_processes/{0}/processdefinition.bpmn2";
    
    // Directory for test to place ouput created using a Format.
    private static final String RESULTS_DIR_FORMAT = "target/migrated_processes/{0}";
    
    @BeforeClass
    public static void oneTimeSetUp() {
        // Set up Log4J.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // Make sure the style sheet is available.
        File xsltSheet = new File(XSLT_SHEET);
        assertThat("Stylesheet missing.", xsltSheet.exists(), is(true));
    }

    /**
     * Validate the jPDL input file.
     */
    @Test
    public void validJpldDefinition() {
        // Make sure the input file is available.
        File jpdl = new File(getJpdlFile());
        assertThat("Indicated input file missing.", jpdl.exists(), is(true));

        // Validate the input process definition.
        Document document = JpdlValidator.validateDefinition(jpdl);
        assertThat("Not a valid jPDL definition.", document, is(notNullValue()));
    }

    /**
     * Transform the input file to an output file.
     */
    @Test
    public void transformjPDL() {
        // Remove any previously existing output first.
        File outputFile = new File(getResultsFile());
        if (outputFile.exists()) {
            assertThat("Unable to clean output.", outputFile.delete(), is(true));
        }
        
        // Ensure directory exists.
        File jpdl = new File(getJpdlFile());
        String results = MessageFormat.format(RESULTS_DIR_FORMAT, jpdl.getParentFile().getName());
        if (!new File(results).mkdirs()) {
            fail("Problem creating output directory: " + results);
        }

        // Transform the input file; creates the output file.
        try {
            JbpmMigration.transform(getJpdlFile(), XSLT_SHEET, getResultsFile());
        } catch (TransformerException e) {
            e.printStackTrace();
            fail("Problem encountered during the transformation: " + e.getMessage());
        }

        // Check that an output file is created.
        outputFile = new File(getResultsFile());
        assertThat("Expected output file missing.", outputFile.exists(), is(true));
    }

    /**
     * Validate the BPMN2 output file.
     */
    @Test
    public void validBpmnDefinition() {
        File bpmn = new File(getResultsFile());
        Document bpmnDoc = XmlUtils.parseFile(bpmn);
        assertThat(bpmnDoc, is(notNullValue()));
        assertThat(BpmnValidator.validateDefinition(bpmnDoc), is(true));
    }

    /**
     * Provide a results file path.
     * 
     * @return String.
     */
    protected String getResultsFile() {
        File jpdl = new File(getJpdlFile());
        String resultsFile = MessageFormat.format(RESULTS_FILE_FORMAT, jpdl.getParentFile().getName());
        return resultsFile;
    }

    protected abstract String getJpdlFile();
}

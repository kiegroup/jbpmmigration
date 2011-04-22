/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.migration.xsl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;

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

    // Results file of transformation.
    private static final String RESULTS_FILE = "target/processdefinition.bpmn.xml";

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
        File outputFile = new File(RESULTS_FILE);
        if (outputFile.exists()) {
            assertThat("Unable to clean output.", outputFile.delete(), is(true));
        }

        // Transform the input file; creates the output file.
        try {
            SimpleJaxpTransformer.transform(getJpdlFile(), XSLT_SHEET, RESULTS_FILE);
        } catch (TransformerException e) {
            e.printStackTrace();
            fail("Problem encountered during the transformation: " + e.getMessage());
        }

        // Check that an output file is created.
        outputFile = new File(RESULTS_FILE);
        assertThat("Expected output file missing.", outputFile.exists(), is(true));
    }

    /**
     * Validate the BPMN2 output file.
     */
    @Test
    public void validBpmnDefinition() {
        File bpmn = new File(RESULTS_FILE);
        Document bpmnDoc = XmlUtils.parseFile(bpmn);
        assertThat(bpmnDoc, is(notNullValue()));
        assertThat(BpmnValidator.validateDefinition(bpmnDoc), is(true));
    }

    protected abstract String getJpdlFile();
}

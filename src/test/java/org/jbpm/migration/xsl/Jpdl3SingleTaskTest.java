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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests for the jPDL process definition transformer with JAXP.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class Jpdl3SingleTaskTest {

    // input jpdl file.
    private static final String INPUT_JPDL = "src/test/resources/jpdl3/singleTask/processdefinition.xml";

    // xsd sheet.
    private static final String XSD_SHEET = "src/main/resources/jpdl3-bpmn2.xsl";

    // results file of transformation.
    private static final String RESULTS_FILE = "target/processdefinition.bpmn.xml";

    @BeforeClass
    public static void oneTimeSetUp() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
    }

    /**
     * Validate the jPDL input file.
     * 
     * @throws Exception
     */
    @Test
    public void validJpldDefinition() throws Exception {
        File jpdl = new File(INPUT_JPDL);
        try {
            Document document = JpdlValidator.validateDefinition(jpdl);
            assertThat(document, is(notNullValue()));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not a valid jPDL definition.");
        }
    }

    /**
     * Transform the input file.
     * 
     * @throws Exception
     */
    @Test
    public void transformjPDL() throws Exception {
        SimpleJaxpTransformer.main(new String[] { INPUT_JPDL, XSD_SHEET, RESULTS_FILE });
        assertThat(RESULTS_FILE, is(notNullValue()));
    }

    /**
     * Validate the BPMN output file.
     * 
     * @throws Exception
     */
    @Test
    public void validBpmnDefinition() throws Exception {
        File bpmn = new File(RESULTS_FILE);
        Document bpmnDoc = XmlUtils.parseFile(bpmn);
        assertThat(bpmnDoc, is(notNullValue()));
        assertThat(BpmnValidator.validateDefinition(bpmnDoc), is(true));
    }
}

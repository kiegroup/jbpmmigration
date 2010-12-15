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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.jbpm.migration.xsl.BpmnValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests for the jDPL process definition transformer.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class ProcessTransformerTest {
    @BeforeClass
    public static void oneTimeSetUp() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
    }

    @Test
    public void validDefinition() throws Exception {
        File jpdlFile = new File("src/test/resources/jpdl3/singleNode/processdefinition.xml");
        assertThat(jpdlFile.exists(), is(equalTo(true)));
        Document jpdlDoc = XmlUtils.parseFile(jpdlFile);
        assertThat(jpdlDoc, is(notNullValue()));
        Logger.getRootLogger().info("jPDL:\n" + XmlUtils.format(jpdlDoc));

        Document bpmnDoc = ProcessTransformer.transform(jpdlDoc);
        assertThat(bpmnDoc, is(notNullValue()));
        Logger.getRootLogger().info("BPMN:\n" + XmlUtils.format(bpmnDoc));
        assertThat(BpmnValidator.validateDefinition(bpmnDoc), is(equalTo(true)));
    }
}

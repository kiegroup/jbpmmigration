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
import static org.junit.Assert.assertThat;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the jDPL process definition migrator.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class MigratorTest {
    private static final String JPDL_FILE = "src/test/resources/jpdl3/singleNode/processdefinition.xml";
    private static final String BPMN_FILE = "target/processdefinition.bpmn.xml";

    @BeforeClass
    public static void oneTimeSetUp() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
    }

    @Test
    public void validDefinition() throws Exception {
        // Check the input.
        File jpdlFile = new File(JPDL_FILE);
        assertThat("Indicated input file missing.", jpdlFile.exists(), is(equalTo(true)));

        // Remove any previously existing output first.
        File bpmnFile = new File(BPMN_FILE);
        if (bpmnFile.exists()) {
            assertThat("Unable to clean output.", bpmnFile.delete(), is(true));
        }

        // Migrate the input.
        Migrator.migrate(JPDL_FILE, BPMN_FILE);

        // Check that an output file is created.
        bpmnFile = new File(BPMN_FILE);
        assertThat("Expected output file missing.", bpmnFile.exists(), is(true));
    }
}

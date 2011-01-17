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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the jPDL process definition transformer with JAXP.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class SimpleJaxpTransformerTest {
    @BeforeClass
    public static void oneTimeSetUp() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ALL);
    }

    @Test
    public void validDefinition() throws Exception {
        SimpleJaxpTransformer.main(new String[] { 
        		"src/test/resources/jpdl3/singleNode/processdefinition.xml",
                "src/main/resources/jpdl3-bpmn2.xsl" });
    }
}

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

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * This class migrate a jPDL process definition to an equivalent BPMN 2.0 process definition, using XSLT translation of the JDPL
 * XML to the BPMN 2.0 XML.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class Migrator {
    private static final Logger LOGGER = Logger.getLogger(Migrator.class);

    /**
     * @param args
     *            The input for this method:<br>
     *            <ul>
     *            <li>args[0]: The (full) path of the jPDL process definition that's to be migrated.</li>
     *            </ul>
     *            Any further arguments are ignored.
     */
    public static void main(String[] args) {
        // Check the input.
        if (args.length < 1 || StringUtils.isBlank(args[0])) {
            throw new IllegalArgumentException("Insufficient arguments for the migrator (must be 1).");
        }
        File jpdl = new File(args[0]);
        if (!jpdl.isFile()) {
            throw new IllegalArgumentException("The input arguments must be a file.");
        }

        // Validate the jPDL and GPD files.
        Document input = JpdlValidator.validateDefinition(jpdl);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(XmlUtils.format(input));
        }

        // Transform the process.
        Document output = ProcessTransformer.transform(input);
        if (output == null) {
            LOGGER.fatal("Unable to transform process definition; aborting. See previous log entries for details.");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(XmlUtils.format(input));
        }

        // Validate the BPMN file.
        if (!BpmnValidator.validateDefinition(output)) {
            LOGGER.warn("Transformed process definition is not valid according to the BPMN 2.0 specification."
                    + " See previous log entries for details.");
        }
    }
}

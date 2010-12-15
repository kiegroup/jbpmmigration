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
import org.jbpm.migration.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * This class migrate a jPDL process definition to an equivalent BPMN 2.0 process definition, using XSLT translation of the JDPL/GPD
 * XML to the BPMN 2.0 XML.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public class Migrator {
    /**
     * @param args
     *            The input for this method:<br>
     *            <ul>
     *            <li>args[0]: The (full) path of the jPDL process definition that's to be migrated.</li>
     *            <li>args[1]: The (full) path of the corresponding GPD positioning file.</li>
     *            </ul>
     *            Any further arguments are ignored.
     */
    public static void main(String[] args) {
        // Check the input.
        if (args.length != 2 || StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[1])) {
            throw new IllegalArgumentException("Insufficient arguments for the migrator (must be 2).");
        }
        File jpdl = new File(args[0]);
        File gpd = new File(args[1]);
        if (!jpdl.isFile() || !gpd.isFile()) {
            throw new IllegalArgumentException("Both of the input arguments must be files.");
        }

        // Validate the jPDL and GPD files.
        Document input = JpdlValidator.validateDefinition(jpdl, gpd);
        System.out.println(XmlUtils.format(input));

        // Transform the process.
        Document output = ProcessTransformer.transform(input);
        System.out.println(XmlUtils.format(output));

        // TODO: Validate the BPMN file.
    }
}

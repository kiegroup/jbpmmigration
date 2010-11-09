/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, JBoss Inc., and individual contributors as indicated
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
package org.jbpm.migration;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * This class validates a resulting jDPL definition against the applicable XML schema.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class BpmnValidator {
    /* XML Schema file for BPMN version 2.0 on the classpath. */
    private static final String BPMN_2_0_SCHEMA = "Schemas/BPMN/BPMN20.xsd";

    /** Logging facility. */
    private static final Logger LOGGER = Logger.getLogger(BpmnValidator.class);

    /** Private constructor to enforce non-instantiability. */
    private BpmnValidator() {
    }

    /**
     * Validate a given BPMN process definition against the applicable schema.
     * 
     * @param bpmn
     *            The process definition.
     * @param gpd
     *            The graphical positioning for the process definition.
     * @return The concatenated document containing both process definition and positioning, or null if (one of) the validations was
     *         not successful.
     */
    public static boolean validateDefinition(Document bpmn) {
        // Validate against the jPDL schema.
        File schema = null;
        try {
            schema = new File(BpmnValidator.class.getClassLoader().getResource(BPMN_2_0_SCHEMA).toURI());
        } catch (URISyntaxException usEx) {
            LOGGER.error("Cannot locate BPMN schema.", usEx);
            return false;
        }
        return XmlUtils.validate(bpmn, schema);
    }
}

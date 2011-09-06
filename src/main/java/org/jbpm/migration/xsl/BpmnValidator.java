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
    private static final String BPMN_2_0_SCHEMA = "BPMN20.xsd";

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

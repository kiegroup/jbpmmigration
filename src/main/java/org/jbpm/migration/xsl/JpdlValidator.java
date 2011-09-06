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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class validates an original jDPL definition against the applicable XML schema.
 * <p>
 * TODO: Make jPDL version flexible (now only 3.2 is supported).<br>
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class JpdlValidator {
    /* XML Schema file for jPDL version 3.2 on the classpath. */
    private static final String JPDL_3_2_SCHEMA = "jpdl-3.2.xsd";

    /** Logging facility. */
    private static final Logger LOGGER = Logger.getLogger(JpdlValidator.class);

    /** Private constructor to enforce non-instantiability. */
    private JpdlValidator() {
    }

    /**
     * Parse and validate a given jDPL process definition against the applicable schema.
     * 
     * @param jpdl
     *            The process definition.
     * @return The <code>Document</code> containing the process definition, or null if (one of) the validations was
     *         not successful.
     */
    public static Document validateDefinition(File jpdl) {
        // Parse the jDPL definition into a DOM tree.
        Document document = XmlUtils.parseFile(jpdl);
        if (document == null) {
            return null;
        }

        // Get the jPDL version from the process definition.
        Node xmlnsNode = document.getFirstChild().getAttributes().getNamedItem("xmlns");
        if (xmlnsNode != null && StringUtils.isNotBlank(xmlnsNode.getNodeValue())) {
            String version = xmlnsNode.getNodeValue().substring(xmlnsNode.getNodeValue().length() - 3);
            LOGGER.info("jPDL version == " + version);
        }

        // Validate against the jPDL schema.
        File schema = null;
        try {
            schema = new File(JpdlValidator.class.getClassLoader().getResource(JPDL_3_2_SCHEMA).toURI());
        } catch (URISyntaxException usEx) {
            LOGGER.error("Cannot locate jPDL schema.", usEx);
            return null;
        }
        return XmlUtils.validate(document, schema) ? document : null;
    }
}

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

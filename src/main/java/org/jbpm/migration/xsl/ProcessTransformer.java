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
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.jbpm.migration.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * Transforms a given jPDL/GPD definition to an equivalent BPMN 2.0 definition.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class ProcessTransformer {
    /* XSL Transformation file for jPDL version 3.2 on the classpath. */
    private static final String MIGRATION_STYLE_SHEET = "jpdl3-bpmn2.xsl";

    /** Logging facility. */
    private static final Logger LOGGER = Logger.getLogger(ProcessTransformer.class);

    /** Private contractor to prevent instantiation. */
    private ProcessTransformer() {
    }

    public static Document transform(Document jpdl) {
        File styleSheet = null;
        try {
            styleSheet = new File(JpdlValidator.class.getClassLoader().getResource(MIGRATION_STYLE_SHEET).toURI());
        } catch (URISyntaxException usEx) {
            LOGGER.error("Cannot locate jPDL to BPMN style sheet.", usEx);
            return null;
        }
        return XmlUtils.transform(jpdl, styleSheet);
    }
}

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
package org.jbpm.migration.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.jbpm.bpmn_2_0.ObjectFactory;
import org.jbpm.bpmn_2_0.TDefinitions;
import org.jbpm.jpdl_3_2.ProcessDefinition;

/**
 * This class gives shortcuts for (un)marshalling between XML and Java, abstracting away the JAXB-specific handling.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class MarshallingUtil {
    /** Logging facility. */
    private static final Logger LOGGER = Logger.getLogger(MarshallingUtil.class);

    /** Private constructor to enforce non-instantiability. */
    private MarshallingUtil() {
    }

    /**
     * Unmarshal a given <code>File</code> into a JPDL Java object hierarchy.
     * 
     * @param jpdl
     *            The given <code>File</code> containing JDPL XML.
     * @return The corresponding JPDL <code>ProcessDefinition</code>, or <code>null</code> if unmarshalling was not successful.
     */
    public static ProcessDefinition unmarshal(File jpdl) {
        ProcessDefinition pd = null;
        try {
            Unmarshaller u = JAXBContext.newInstance("org.jbpm.jpdl_3_2").createUnmarshaller();
            pd = (ProcessDefinition) u.unmarshal(new FileInputStream(jpdl));
        } catch (Exception ex) {
            LOGGER.fatal("Unable to unmarshal the given InputStream.", ex);
        }
        return pd;
    }

    /**
     * Marshal a given BPMN 2.0 <code>TDefinitions</code> hierarchy into a given <code>OutputStream</code>.
     * 
     * @param def
     *            The given BPMN object hierarchy.
     * @param os
     *            The given <code>OutputStream</code> for containing the output XML. Nothing gets streamed if marshalling was not
     *            successful.
     */
    public static void marshal(TDefinitions def, OutputStream os) {
        try {
            Marshaller m = JAXBContext.newInstance("org.jbpm.bpmn_2_0").createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(new ObjectFactory().createDefinitions(def), os);
        } catch (Exception ex) {
            LOGGER.fatal("Unable to marshal the given TDefinitions object.", ex);
        }
    }
}

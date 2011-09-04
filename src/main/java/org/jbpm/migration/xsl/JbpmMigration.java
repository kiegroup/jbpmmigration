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

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class JbpmMigration {
    
    // Default XSLT sheet.
    private static final String DEFAULT_XSLT_SHEET = "src/main/resources/jpdl3-bpmn2.xsl";
    
    
    /** Private constructor to prevent instantiation. */
    private JbpmMigration() {
    }

    /**
     * Accept two or three command line arguments: 
     *      - the name of an XML file (required) 
     *      - the name of an XSLT stylesheet (optional, default is jpdl 3.2)
     *      - the name of the file the result of the transformation is to be written to.
     */
    public static void main(String[] args) throws TransformerException {
        if (args.length == 2) {
            // using default jpdl 3.2 as XSLT stylesheet.
            transform(args[0], args[1]);
        } else if (args.length == 3 ) {
            // using arg[1] as XSLT stylesheet.
            transform(args[0], args[1], args[2]);
        } else {
            System.err.println("Usage:");
            System.err.println("  java " + JbpmMigration.class.getName() + " jpdlProcessDefinitionFileName xsltFileName outputFileName");
            System.err.println(" or you can use the default jpdl 3.2 transformation:");
            System.err.println("  java " + JbpmMigration.class.getName() + " jpdlProcessDefinitionFileName outputFileName");
            System.exit(1);
        }
        
        
    }

    /**
     * 
     * @param xmlFile
     *            The name of an XML input file.
     * @param outputFile
     *            The name of the file the result of the transformation is to be written to.
     * @throws TransformerException
     *             If the creation of the {@link Transformer} or the transformation run into problems.
     */
    static void transform(String xmlFile, String outputFile) throws TransformerException {
        // Create a transformer with the given stylesheet.
        Source xsltSource = new StreamSource(new File(DEFAULT_XSLT_SHEET));
        Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);

        // Transform the given input file and put the result in the given output file.
        Source xmlSource = new StreamSource(new File(xmlFile));
        Result result = new StreamResult(new File(outputFile));
        transformer.transform(xmlSource, result);
    }

    /**
     * 
     * @param xmlFile
     *            The name of an XML input file.
     * @param xsltFile
     *            The name of an XSLT stylesheet.
     * @param outputFile
     *            The name of the file the result of the transformation is to be written to.
     * @throws TransformerException
     *             If the creation of the {@link Transformer} or the transformation run into problems.
     */
    static void transform(String xmlFile, String xsltFile, String outputFile) throws TransformerException {
        // Create a transformer with the given stylesheet.
        Source xsltSource = new StreamSource(new File(xsltFile));
        Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);

        // Transform the given input file and put the result in the given output file.
        Source xmlSource = new StreamSource(new File(xmlFile));
        Result result = new StreamResult(new File(outputFile));
        transformer.transform(xmlSource, result);
    }
}

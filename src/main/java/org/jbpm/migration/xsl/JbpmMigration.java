/**
 * Copyright 2010 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.jbpm.migration.xsl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class JbpmMigration {
    // Default XSLT sheet.
    private static final String DEFAULT_XSLT_SHEET = "jpdl3-bpmn2.xsl";

    /** Private constructor to prevent instantiation. */
    private JbpmMigration() {
    }

    /**
     * Accept two or three command line arguments: - the name of an XML file (required) - the name of an XSLT stylesheet (optional, default is jpdl 3.2) - the
     * name of the file the result of the transformation is to be written to.
     */
    public static void main(final String[] args) throws TransformerException {
        if (args.length == 2) {
            // using default jpdl 3.2 as XSLT stylesheet.
            transform(args[0], args[1]);
        } else if (args.length == 3) {
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
    static void transform(final String xmlFile, final String outputFile) throws TransformerException {
        // Create a transformer with the default stylesheet.
        final Transformer transformer = createTransformerForDefaultSheet();

        // Transform the given input file and put the result in the given output file.
        final Source xmlSource = new StreamSource(new File(xmlFile));
        final Result result = new StreamResult(new File(outputFile));
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
    static void transform(final String xmlFile, final String xsltFile, final String outputFile) throws TransformerException {
        // Create a transformer with the given stylesheet.
        final Source xsltSource = new StreamSource(new File(xsltFile));
        final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);

        // Transform the given input file and put the result in the given output file.
        final Source xmlSource = new StreamSource(new File(xmlFile));
        final Result result = new StreamResult(new File(outputFile));
        transformer.transform(xmlSource, result);
    }

    /**
     * API call to transform a JPDL definition (currently version 3.2) to a BPMN2 definition.
     * 
     * @param jpdl
     *            The input definition (as an XML String).
     * @return The output definition (as an XML String as well).
     */
    public static String transform(final String jpdl) {
        final StringWriter outputWriter = new StringWriter();

        try {
            // Create a transformer with the default stylesheet.
            final Transformer transformer = createTransformerForDefaultSheet();

            // Transform the given input file and put the result in the output stream.
            final Source xmlSource = new StreamSource(new StringReader(jpdl));
            final Result result = new StreamResult(outputWriter);
            transformer.transform(xmlSource, result);
        } catch (final Exception ex) {
            ex.printStackTrace(new PrintWriter(outputWriter));
        }

        return outputWriter.toString();
    }

    private static Transformer createTransformerForDefaultSheet() throws TransformerConfigurationException {
        // Create a resolver for the imported sheets available the root of the jar.
        final URIResolver resolver = new URIResolver() {
            @Override
            public Source resolve(final String href, final String base) throws TransformerException {
                return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(href));
            }
        };

        // Create the transformer for the root sheet (also in the root of the jar).
        final Source xsltSource = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_XSLT_SHEET));
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(resolver);
        final Transformer transformer = transformerFactory.newTransformer(xsltSource);
        transformer.setURIResolver(resolver);

        return transformer;
    }
}

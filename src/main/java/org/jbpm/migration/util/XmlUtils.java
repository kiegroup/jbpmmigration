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
package org.jbpm.migration.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xalan.trace.PrintTraceListener;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Convenience class for working with XML documents.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class XmlUtils {
    private static final Logger LOGGER = Logger.getLogger(XmlUtils.class);

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    static {
        FACTORY.setNamespaceAware(true);
    }

    /** Private constructor to prevent instantiation. */
    private XmlUtils() {
    }

    /**
     * Create an empty XML structure.
     * 
     * @return An empty DOM tree.
     */
    public static Document createEmptyDocument() {
        Document output = null;
        try {
            final DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.newDocument();
        } catch (final Exception ex) {
            LOGGER.error("Problem creating empty XML document.", ex);
        }

        return output;
    }

    /**
     * Parse a <code>File</code> containing an XML structure into a DOM tree.
     * 
     * @param input
     *            The input XML file.
     * @return The corresponding DOM tree, or <code>null</code> if the input could not be parsed successfully.
     */
    public static Document parseFile(final File input) {
        Document output = null;
        try {
            final DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.parse(input);
        } catch (final Exception ex) {
            String msg = "Problem parsing the input XML file";
            if (ex instanceof SAXParseException) {
                msg += " at line #" + ((SAXParseException) ex).getLineNumber();
            }
            LOGGER.error(msg, ex);
        }

        return output;
    }

    /**
     * Parse a <code>String</code> containing an XML structure into a DOM tree.
     * 
     * @param input
     *            The input XML <code>String</code>.
     * @return The corresponding DOM tree, or <code>null</code> if the input could not be parsed successfully.
     */
    public static Document parseString(final String input) {
        Document output = null;
        try {
            final DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.parse(new ByteArrayInputStream(input.getBytes()));
        } catch (final Exception ex) {
            String msg = "Problem parsing the input XML string";
            if (ex instanceof SAXParseException) {
                msg += " at line #" + ((SAXParseException) ex).getLineNumber();
            }
            LOGGER.error(msg, ex);
        }

        return output;
    }

    /**
     * Write an XML document to a <code>File</code>.
     * 
     * @param input
     *            The input XML document.
     * @param file
     *            The intended <code>File</code>.
     */
    public static void writeFile(final Document input, final File file) {
        try {
            new FileWriter(file).write(format(input));
        } catch (final Exception ex) {
            LOGGER.error("Problem writing XML to file.", ex);
        }
    }

    /**
     * Validate an XML document against an XML Schema definition.
     * 
     * @param input
     *            The input XML document.
     * @param schemaFile
     *            The XML Schema against which the document must be validated.
     * @return Whether the validation was successful.
     */
    public static boolean validate(final Document input, final File schemaFile) {
        boolean isValid = true;
        try {
            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final Schema schema = factory.newSchema(new StreamSource(schemaFile));
            final Validator val = schema.newValidator();
            final ParserErrorHandler eh = new ParserErrorHandler();
            val.setErrorHandler(eh);
            val.validate(new DOMSource(input));
            isValid = !eh.didExceptionOccur();
        } catch (final Exception ex) {
            LOGGER.error("Problem validating the given process definition.", ex);
            isValid = false;
        }

        return isValid;
    }

    /**
     * Transform an XML document according to an XSL style sheet.
     * 
     * @param input
     *            The input XML document.
     * @param styleSheet
     *            The XSL style sheet according to which the document must be transformed.
     * @return The transformed document, or <code>null</code> if a problem occurred while transforming.
     */
    public static Document transform(final Document input, final File styleSheet) {
        Document output = null;
        try {
            output = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(styleSheet));
            instrumentTransformer(transformer);

            transformer.transform(new DOMSource(input), new DOMResult(output));
        } catch (final Exception ex) {
            LOGGER.error("Problem transforming XML file.", ex);
        }

        return output;
    }

    /**
     * Format an XML document (fragment) to a pretty-printable <code>String</code>.
     * 
     * @param input
     *            The uppermost node of the input XML document (fragment).
     * @return The formatted <code>String</code>.
     */
    public static String format(final Node input) {
        return format(new DOMSource(input));
    }

    /**
     * Format an XML <code>String</code> to a pretty-printable <code>String</code>.
     * 
     * @param input
     *            The input XML <code>String</code>.
     * @return The formatted <code>String</code>.
     */
    public static String format(final String input) {
        return format(new StreamSource(input));
    }

    private static String format(final Source input) {
        StreamResult result = null;
        try {
            // Use an identity transformation to write the source to the result.
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            instrumentTransformer(transformer);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            result = new StreamResult(new StringWriter());

            transformer.transform(input, result);
        } catch (final Exception ex) {
            LOGGER.error("Problem formatting DOM representation.", ex);
            return null;
        }

        return result.getWriter().toString();
    }

    /**
     * Adds a little verbosity to a constructed <code>Transformer</code>.
     * 
     * @param transformer
     *            The <code>Transformer</code> which may produce some output.
     * @throws Exception
     *             If setting/adding a listener gives a problem.
     */
    private static void instrumentTransformer(Transformer transformer) throws Exception {
        transformer.setErrorListener(new TransformerErrorListener());

        if (transformer instanceof TransformerHandler) {
            transformer = ((TransformerHandler) transformer).getTransformer();
        }
        if (transformer instanceof TransformerImpl) {
            final TraceManager tm = ((TransformerImpl) transformer).getTraceManager();
            final PrintTraceListener ptl = new PrintTraceListener(new PrintWriter(System.out));
            // Print information as each node is 'executed' in the stylesheet.
            ptl.m_traceElements = true;
            // Print information after each result-tree generation event.
            ptl.m_traceGeneration = true;
            // Print information after each selection event.
            ptl.m_traceSelection = true;
            // Print information whenever a template is invoked.
            ptl.m_traceTemplates = true;
            // Print information whenever an extension call is made.
            ptl.m_traceExtension = true;
            tm.addTraceListener(ptl);
        }
    }

    /** Private class for making the parsing and validation processes a little more verbose. */
    private static class ParserErrorHandler implements ErrorHandler {
        private boolean exceptionOccurred;

        @Override
        public void warning(final SAXParseException saxParseEx) {
            log(Level.WARN, saxParseEx);
        }

        @Override
        public void error(final SAXParseException saxParseEx) {
            log(Level.ERROR, saxParseEx);
        }

        @Override
        public void fatalError(final SAXParseException saxParseEx) {
            log(Level.FATAL, saxParseEx);
        }

        boolean didExceptionOccur() {
            return exceptionOccurred;
        }

        private void log(final Level level, final SAXParseException saxParseEx) {
            exceptionOccurred = true;
            LOGGER.log(level, "Problem parsing XML: " + saxParseEx.getMessage());
        }
    }

    /** Private class for making the transformation processes a little more verbose. */
    private static class TransformerErrorListener implements ErrorListener {
        @Override
        public void warning(final TransformerException transformerEx) {
            log(Level.WARN, transformerEx);
        }

        @Override
        public void error(final TransformerException transformerEx) {
            log(Level.ERROR, transformerEx);
        }

        @Override
        public void fatalError(final TransformerException transformerEx) {
            log(Level.FATAL, transformerEx);
        }

        private void log(final Level level, final TransformerException transformerEx) {
            LOGGER.log(level, "Problem transforming XML: " + transformerEx);
        }
    }
}

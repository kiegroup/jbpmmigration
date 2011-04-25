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
package org.jbpm.migration.util;

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
            DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.newDocument();
        } catch (Exception ex) {
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
    public static Document parseFile(File input) {
        Document output = null;
        try {
            DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.parse(input);
        } catch (Exception ex) {
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
    public static Document parseString(String input) {
        Document output = null;
        try {
            DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new ParserErrorHandler());
            output = db.parse(input);
        } catch (Exception ex) {
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
    public static void writeFile(Document input, File file) {
        try {
            new FileWriter(file).write(format(input));
        } catch (Exception ex) {
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
    public static boolean validate(Document input, File schemaFile) {
        boolean isValid = true;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(schemaFile));
            Validator val = schema.newValidator();
            ParserErrorHandler eh = new ParserErrorHandler();
            val.setErrorHandler(eh);
            val.validate(new DOMSource(input));
            isValid = !eh.didExceptionOccur();
        } catch (Exception ex) {
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
    public static Document transform(Document input, File styleSheet) {
        Document output = null;
        try {
            output = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(styleSheet));
            instrumentTransformer(transformer);

            transformer.transform(new DOMSource(input), new DOMResult(output));
        } catch (Exception ex) {
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
    public static String format(Node input) {
        return format(new DOMSource(input));
    }

    /**
     * Format an XML <code>String</code> to a pretty-printable <code>String</code>.
     * 
     * @param input
     *            The input XML <code>String</code>.
     * @return The formatted <code>String</code>.
     */
    public static String format(String input) {
        return format(new StreamSource(input));
    }

    private static String format(Source input) {
        StreamResult result = null;
        try {
            // Use an identity transformation to write the source to the result.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            instrumentTransformer(transformer);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            result = new StreamResult(new StringWriter());

            transformer.transform(input, result);
        } catch (Exception ex) {
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
            TraceManager tm = ((TransformerImpl) transformer).getTraceManager();
            PrintTraceListener ptl = new PrintTraceListener(new PrintWriter(System.out));
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
        public void warning(SAXParseException saxParseEx) {
            log(Level.WARN, saxParseEx);
        }

        @Override
        public void error(SAXParseException saxParseEx) {
            log(Level.ERROR, saxParseEx);
        }

        @Override
        public void fatalError(SAXParseException saxParseEx) {
            log(Level.FATAL, saxParseEx);
        }

        boolean didExceptionOccur() {
            return exceptionOccurred;
        }

        private void log(Level level, SAXParseException saxParseEx) {
            exceptionOccurred = true;
            LOGGER.log(level, "Problem parsing XML: " + saxParseEx.getMessage());
        }
    }

    /** Private class for making the transformation processes a little more verbose. */
    private static class TransformerErrorListener implements ErrorListener {
        @Override
        public void warning(TransformerException transformerEx) {
            log(Level.WARN, transformerEx);
        }

        @Override
        public void error(TransformerException transformerEx) {
            log(Level.ERROR, transformerEx);
        }

        @Override
        public void fatalError(TransformerException transformerEx) {
            log(Level.FATAL, transformerEx);
        }

        private void log(Level level, TransformerException transformerEx) {
            LOGGER.log(level, "Problem transforming XML: " + transformerEx);
        }
    }
}

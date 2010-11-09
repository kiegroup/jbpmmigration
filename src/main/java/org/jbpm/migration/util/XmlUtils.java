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
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
            output = FACTORY.newDocumentBuilder().newDocument();
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
            output = FACTORY.newDocumentBuilder().parse(input);
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
            output = FACTORY.newDocumentBuilder().parse(input);
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
            // TODO: Experiment with ErrorHandler on Validator for more concise error messages.
            schema.newValidator().validate(new DOMSource(input));
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
        StreamResult result = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            result = new StreamResult(new StringWriter());
            transformer.transform(new DOMSource(input), result);
        } catch (Exception ex) {
            LOGGER.error("Problem formatting DOM representation.", ex);
            return null;
        }

        return result.getWriter().toString();
    }

    /**
     * Format an XML <code>String</code> to a pretty-printable <code>String</code>.
     * 
     * @param input
     *            The input XML <code>String</code>.
     * @return The formatted <code>String</code>.
     */
    public static String format(String input) {
        StreamResult result = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            result = new StreamResult(new StringWriter());
            transformer.transform(new StreamSource(input), result);
        } catch (Exception ex) {
            LOGGER.error("Problem formatting DOM representation.", ex);
            return null;
        }

        return result.getWriter().toString();
    }
}

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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Eric D. Schabell
 *
 */
public class SimpleJaxpTransformer {
		 
    /**
     * Accept two command line arguments: the name of an XML file, and
     * the name of an XSLT stylesheet. The result of the transformation
     * is written to stdout.
     */
    public static void main(String[] args)
            throws javax.xml.transform.TransformerException {
        if (args.length != 3) {
            System.err.println("Usage:");
            System.err.println("  java " + SimpleJaxpTransformer.class.getName(  )
                    + " xmlFileName xsltFileName outputFileName");
            System.exit(1);
        }
 
        File xmlFile    = new File(args[0]);
        File xsltFile   = new File(args[1]);
        File outputFile = new File(args[2]);
        
        Source xmlSource  = new StreamSource(xmlFile);
        Source xsltSource = new StreamSource(xsltFile);
        Result result     = new StreamResult(outputFile);
 
        // create an instance of TransformerFactory
        TransformerFactory transFact = TransformerFactory.newInstance( );
 
        Transformer trans = transFact.newTransformer(xsltSource);
 
        trans.transform(xmlSource, result);
    }

}

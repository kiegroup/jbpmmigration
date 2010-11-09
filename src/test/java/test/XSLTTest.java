package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Scanner;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class to sort out the transformations of our use cases.
 * 
 * @author Marco Rietveld
 *
 */
public class XSLTTest {

    public static final String useCasesDir = "use_cases";
   
    public static final String singleForkJoinJpdl = "single_fork_join_jpdl";
    public static final String singleDecisionJpdl = "single_decision_jpdl";
    public static final String singleNodeJpdl = "single_node_jpdl";
    public static final String singleStateJpdl = "single_state_jpdl";
    public static final String singleSubprocessJpdl = "single_subprocess_jpdl";
    public static final String singleTaskJpdl = "single_task_jpdl";
   
    public static final String procDefXML = "processdefinition.xml";
    public static final String bpmnDefXML = "bpmnProcess.xml";

    public static final String migrateXSLT = "migrate.xslt";
    
    private TransformerFactory transformerFactory;
    private Transformer transformer;
   
    @Before
    public void before() throws Exception {
        InputStream xsltInputStream = getClass().getClassLoader().getResourceAsStream(migrateXSLT);
        
        StreamSource source = new StreamSource(xsltInputStream);
        assertTrue("XSLT source could not be instantiated.", source != null && source.getInputStream() != null );
       
        transformerFactory = TransformerFactory.newInstance();
        Templates templates = null;
        try {
            templates = transformerFactory.newTemplates(source);
        }
        catch(  TransformerConfigurationException tce ) {
            fail( "Could not instantiate transformer templates: " + tce.getMessageAndLocation());
        }

        assertTrue("Transformer template could not be instantiated.", templates != null);
        try {
            transformer = templates.newTransformer();
        }
        catch( TransformerConfigurationException tce ) {
            tce.printStackTrace();
            fail( "Could not instantiate transformer: " + tce.getMessageAndLocation());
        }
    }
    
    @Test
    public void happyTest() {
        String process = "Single Task";
        String dir = singleTaskJpdl;
        
        // transform jPDL task.
        File resultFile = transformJpdlXml(transformer, dir, process);

        System.out.println("=================================");
        System.out.println("= The transformation result is: =");
        System.out.println("=================================");
        displayFileContents(resultFile);
    }

    /**
     * Used to dump a file transformation results to the log.
     * 
     * @param resultFile
     */
	private static void displayFileContents(File resultFile) {
		StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream(resultFile));
			while (scanner.hasNextLine()){
				text.append(scanner.nextLine() + NL);
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
        finally{
          scanner.close();
        }

        System.out.println(prettyFormat(text.toString()));
	}
    
	/**
	 * Transformation of the input process definition to output format (bpmn).
	 * @param transformer
	 * @param dir
	 * @param process
	 * @return
	 */
    public static File transformJpdlXml(Transformer transformer, String dir, String process) {
        URL startURL = XSLTTest.class.getClassLoader().getResource(dir + "/" + procDefXML);
        assertTrue( process + " JPDL URL could not be retrieved.", startURL != null );
        File startFile = new File(startURL.getFile());
        
        // debug line for looking at start file.
        System.out.println("======================");
        System.out.println("= The input file is: =");
        System.out.println("======================");
        displayFileContents(startFile);
        assertTrue( process + " JPDL File could not be instantiated.", startFile != null && startFile.exists());

        dir = startFile.getParentFile().getAbsolutePath();
        String fileLoc = dir + "/" + bpmnDefXML;
        File resultFile = new File(fileLoc);
        try { 
            resultFile.createNewFile();
        }
        catch( IOException ioe ) {
            fail( "Unable to create new file: " + ioe.getMessage() );
        }
        assertTrue( process + " BPMN File could not be retrieved.", resultFile != null );
        
        try {
            transformer.transform(new StreamSource(startFile), new StreamResult(resultFile));
        }
        catch(TransformerException te ) {
            fail( "Could not transform " + process + " from JPDL to BPMN: " + te.getMessageAndLocation());
        }
        
        return resultFile;
    }
    
    
    /**
     * Format XML into given indentation depth.
     * 
     * @param input
     * @param indent
     * @return
     */
    private static String prettyFormat(String input, int indent) {
        try {
        	Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Format with default indenting.
     * 
     * @param input
     * @return
     */
    private static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
}

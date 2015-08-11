package org.jbpm.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.assertj.core.api.Assertions;
import org.jbpm.migration.tools.MigrationHelper;

import org.jbpm.graph.def.ProcessDefinition;
import org.junit.After;
import org.kie.api.io.ResourceType;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Migration test wrapper.
 *
 * Contains methods for test preparation.
 */
public class MigrationTestWrapper {

    protected static KieBase kbase;

    private static KnowledgeBuilder kbuilder;

    protected static KieSession ksession;

    protected static ProcessDefinition processDef;

    private static MigrationTestWrapper instance = new MigrationTestWrapper();

    private static File basedir;
    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(MigrationTestWrapper.class);

    protected static List<File> migrate(String... processDefinitions) {
        final List<File> migratedFiles = new ArrayList<File>(processDefinitions.length);
        for (String processDefinition : processDefinitions) {
            migratedFiles.add(migrate(processDefinition));
        }
        return migratedFiles;
    }

    protected static File migrate(String processDefinition) {
        File jpdlFile = null;
        try {
            jpdlFile = new File(MigrationTestWrapper.class.getResource("/" + processDefinition).toURI());
        } catch (URISyntaxException ex) {
            logger.error(null, ex);
        }
        return migrate(jpdlFile);
    }

    protected static File migrate(File processDefinition) {
        return migrate(processDefinition, processDefinition.getParentFile().getName());
    }

    protected static File migrate(File processDefinition, String bpmnFileName) {
        File bpmnFile = instance.createTempFile(bpmnFileName, "bpmn2");
        MigrationHelper.migration(processDefinition, bpmnFile);
        return bpmnFile;
    }

    protected static void buildNewKbase(File... bpmnFiles) {
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (File bpmnFile : bpmnFiles) {
            kbuilder.add(ResourceFactory.newFileResource(bpmnFile), ResourceType.BPMN2);
        }

        if (kbuilder.hasErrors()) {
            throw new RuntimeException(kbuilder.getErrors().toString());
        }

        kbase = kbuilder.newKnowledgeBase();
    }

    protected static void createProcessDefinition(String processDefinition) {
        processDef = ProcessDefinition.parseXmlResource(processDefinition);
    }

    /**
     * Creates jpdl process definition, migrates to BPMN2 and builds
     * KnowledgeBase.
     *
     * @param allProcesses
     *            JPDL process definition files, the first one is used for
     *            createProcessDefinition().
     */
    protected static void prepareProcess(String... allProcesses) {
        final List<File> bpmnFiles = migrate(allProcesses);
        createProcessDefinition(allProcesses[0]);
        buildNewKbase(bpmnFiles.toArray(new File[] {}));
    }

    /**
     * Creates jpdl process definition, migrates to BPMN2 and builds
     * KnowledgeBase.
     *
     * @param processDefinition
     *            JPDL process definition file.
     */
    protected static void prepareProcess(String processDefinition) {
        File bpmnFile = migrate(processDefinition);
        createProcessDefinition(processDefinition);
        buildNewKbase(bpmnFile);
    }

    /**
     * Adds new classpath bpmn2 definition to an existing knowledge base.
     *
     * @path path to bpmn2 definition file on classpath.
     */
    protected static void addBpmnProcessFromClassPath(String... paths) {
        for (String path : paths) {
            kbuilder.add(ResourceFactory.newClassPathResource(path, MigrationTestWrapper.class), ResourceType.BPMN2);
        }

        if (kbuilder.hasErrors()) {
            throw new RuntimeException(kbuilder.getErrors().toString());
        }

        kbase = kbuilder.newKnowledgeBase();
    }

    /**
     * Creates new knowledge base from classpath bpmn2 definition.
     *
     * @path path to bpmn2 definition file on classpath.
     */
    protected static void createBpmnProcessFromClassPath(String... paths) {
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        addBpmnProcessFromClassPath(paths);
    }

    protected final File createTempFile(String name) {
        return createTempFile(name, "");
    }

    protected static synchronized File getBasedir() {
        if (MigrationTestWrapper.basedir == null) {
            Assertions.assertThat(System.getProperty("basedir")).as("System property for basedir not set!").isNotNull();
            MigrationTestWrapper.basedir = new File(System.getProperty("basedir"));
            Assertions.assertThat(MigrationTestWrapper.basedir.exists()).as("Basedir does not exist! Check value of 'basedir' system property.").isTrue();
        }
        return MigrationTestWrapper.basedir;
    }

    /**
     * Returns location of directory for temporary files
     *
     * @return directory to save temporary files to
     */
    private static synchronized File getTempDir() {
        File tempDir = new File(getBasedir(), getProperty("temp.dir", "tmp"));
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempDir;
    }

    protected static String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    protected static synchronized Properties getProperties() {
        if (properties == null) {
            // lazy initialization of properties
            File basedir = MigrationTestWrapper.getBasedir();
            File propFile = new File(basedir, "build.properties");
            if (!propFile.exists()) {
                propFile = new File(basedir.getParentFile(), "build.properties");
            }
            Assertions.assertThat(propFile.exists()).as("Couldn't find build.properties!").isTrue();
            properties = new Properties();
            try {
                properties.load(new FileInputStream(propFile));
            } catch (FileNotFoundException e) {
                Assertions.fail("Properties file not found. At this point, this is impossible.");
            } catch (IOException e) {
                Assertions.fail("Properties file cannot be read!");
            }
        }
        return (Properties) properties.clone();
    }

    protected final File createTempFile(String name, String extension) {
        File dir = new File(getTempDir(), getClass().getSimpleName());
        if (!dir.exists()) {
            dir.mkdir();
        }

        int i = 0;
        File temp;
        while ((temp = new File(dir, String.format("%s_%03d.%s", name, i++, extension))).exists()) {
        }

        return temp;
    }

    @After
    public void dispose() {
        if (ksession != null) {
            ksession.dispose();
        }
    }

}

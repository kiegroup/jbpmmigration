package org.jbpm.migration.tools.jpdl;

import org.dom4j.Element;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.node.SubProcessResolver;

/**
 * Resolves subprocess definition based upon the subprocess's name
 */
public class NameBasedSubProcessResolver implements SubProcessResolver {

    private static final long serialVersionUID = 801168586616886569L;

    @Override
    public ProcessDefinition findSubProcess(Element element) {
        String name = element.attributeValue("name").replaceAll("\\.", "/");
        return ProcessDefinition.parseXmlResource(name + "definition.xml");
    }
}

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
package org.jbpm.migration.jaxb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jbpm.bpmn_2_0.ObjectFactory;
import org.jbpm.bpmn_2_0.TDefinitions;
import org.jbpm.bpmn_2_0.TEndEvent;
import org.jbpm.bpmn_2_0.TProcess;
import org.jbpm.bpmn_2_0.TScriptTask;
import org.jbpm.bpmn_2_0.TSequenceFlow;
import org.jbpm.bpmn_2_0.TStartEvent;
import org.jbpm.jpdl_3_2.EndState;
import org.jbpm.jpdl_3_2.Node;
import org.jbpm.jpdl_3_2.ProcessDefinition;
import org.jbpm.jpdl_3_2.StartState;
import org.jbpm.jpdl_3_2.Transition;

/**
 * Transforms a given jPDL/GPD definition to an equivalent BPMN 2.0 definition.
 * 
 * @author Eric D. Schabell
 * @author Maurice de Chateau
 */
public final class ProcessTransformer {
    /** Logging facility. */
    private static final Logger LOGGER = Logger.getLogger(ProcessTransformer.class);

    private static int transitionCounter = 1;

    /** Private contractor to prevent instantiation. */
    private ProcessTransformer() {
    }

    public static TDefinitions transform(ProcessDefinition pd) {
        Map<Object, Object> migrationMap = new HashMap<Object, Object>();

        TDefinitions def = new TDefinitions();
        def.setTargetNamespace("http://www.jboss.org/jbpm/migrationtool/");

        TProcess proc = new TProcess();
        proc.setId(pd.getName());
        migrationMap.put(pd, proc);
        def.getRootElement().add(new ObjectFactory().createProcess(proc));

        resolveNodeElements(proc, pd.getDescriptionOrSwimlaneOrStartState(), migrationMap);

        return def;
    }

    private static void resolveNodeElements(TProcess proc, List<Object> elements, Map<Object, Object> migrationMap) {
        for (Object obj : elements) {
            if (obj instanceof StartState) {
                StartState ss = (StartState) obj;
                TStartEvent se = new TStartEvent();
                se.setId(ss.getName());
                se.setName(ss.getName());
                migrationMap.put(ss, se);
                proc.getFlowElement().add(new ObjectFactory().createStartEvent(se));

                for (Object child : ss.getDescriptionOrTaskOrTransition()) {
                    if (child instanceof Transition) {
                        // TODO: Figure out a way to get the 'to' node.
                        resolveTransition(proc, (Transition) child, se, null);
                    } else {
                        LOGGER.error("Unsupported child element type encountered: " + child.getClass().getName());
                    }
                }
            } else if (obj instanceof Node) {
                Node node = (Node) obj;
                TScriptTask st = new TScriptTask();
                st.setId(node.getName());
                st.setName(node.getName());
                proc.getFlowElement().add(new ObjectFactory().createScriptTask(st));

                for (Object child : node.getDescriptionOrEventOrExceptionHandler()) {
                    if (child instanceof Transition) {
                        // TODO: Figure out a way to get the 'to' node.
                        resolveTransition(proc, (Transition) child, st, null);
                    } else {
                        LOGGER.error("Unsupported child element type encountered: " + child.getClass().getName());
                    }
                }
            } else if (obj instanceof EndState) {
                EndState es = (EndState) obj;
                TEndEvent ee = new TEndEvent();
                ee.setId(es.getName());
                ee.setName(es.getName());
                proc.getFlowElement().add(new ObjectFactory().createEndEvent(ee));
            } else {
                LOGGER.error("Unsupported node element type encountered: " + obj.getClass().getName());
            }
        }
    }

    private static void resolveTransition(TProcess proc, Transition t, Object from, Object to) {
        TSequenceFlow sf = new TSequenceFlow();
        if (StringUtils.isBlank(t.getName())) {
            sf.setId("flow" + transitionCounter);
            sf.setName("flow" + transitionCounter++);
        } else {
            sf.setId(t.getName());
            sf.setName(t.getName());
        }
        sf.setSourceRef(from);
        sf.setTargetRef(to);
        proc.getFlowElement().add(new ObjectFactory().createSequenceFlow(sf));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl.handlers;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

/**
 * Simple decision handler.
 */
public class DefaultDecisionHandler implements DecisionHandler {

    private static String transitionName;

    @Override
    public String decide(ExecutionContext executionContext) throws Exception {
        return transitionName;
    }

    public static void setLeavingTransition(String transition) {
        transitionName = transition;
    }
}

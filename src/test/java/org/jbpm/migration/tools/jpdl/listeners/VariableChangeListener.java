/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl.listeners;

import org.jbpm.context.exe.ContextInstance;

/**
 * Just for marking...
 */
public interface VariableChangeListener {

    /**
     * Records the state of variables in given context instance.
     *
     * @param cxt
     *            context instance containing variables.
     *
     *            Must be called before making changes to variables in order to
     *            record the original state of variables.
     */
    public void recordOldValues(ContextInstance cxt);

    /**
     * Records the state of variables in given context instance.
     *
     * @param cxt
     *            context instance containing variables.
     *
     *            Must be called after making changes to variables in order to
     *            record the new state of variables.
     */
    public void recordNewValues(ContextInstance cxt);
}

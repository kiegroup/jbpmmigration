/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl;

/**
 * Keeps change made to process variable.
 */
public class VariableChange {
    private String name;
    private Object oldValue;
    private Object newValue;

    public VariableChange() {
    }

    public VariableChange(String name, Object original) {
        this.name = name;
        this.oldValue = original;
    }

    public VariableChange(String name, Object original, Object changed) {
        this.name = name;
        this.oldValue = original;
        this.newValue = changed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariableChange other = (VariableChange) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.oldValue != other.oldValue && (this.oldValue == null || !this.oldValue.equals(other.oldValue))) {
            return false;
        }
        if (this.newValue != other.newValue && (this.newValue == null || !this.newValue.equals(other.newValue))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.oldValue != null ? this.oldValue.hashCode() : 0);
        hash = 29 * hash + (this.newValue != null ? this.newValue.hashCode() : 0);
        return hash;
    }

}

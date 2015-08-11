package org.jbpm.migration.tools.bpmn2;

/**
 * Information about class.method execution.
 */
public class ExecutionInfo {
    private String className;
    private String methodName;
    private int count;

    public ExecutionInfo(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.count = 1;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    /**
     * resets the execution count to 0.
     *
     * @return actual instance
     */
    public ExecutionInfo reset() {
        count = 0;
        return this;
    }

    /**
     * increases the execution count by 1.
     *
     * @return actual instance
     */
    public ExecutionInfo inc() {
        count++;
        return this;
    }

    public int ExecutionCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExecutionInfo other = (ExecutionInfo) obj;
        if ((this.className == null) ? (other.className != null) : !this.className.equals(other.className)) {
            return false;
        }
        if ((this.methodName == null) ? (other.methodName != null) : !this.methodName.equals(other.methodName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 89 * hash + (this.methodName != null ? this.methodName.hashCode() : 0);
        return hash;
    }

}

package org.jbpm.migration.tools.bpmn2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Simplified JavaInvocationWorkItemHandler for testing purposes.
 *
 */
public class JavaNodeHandler implements WorkItemHandler, ExecutionInfoSupport {

    private Set<ExecutionInfo> execInfo = new HashSet<ExecutionInfo>();

    private List<Exception> exceptions = new ArrayList<Exception>();

    /**
     * Executes class.method specified on task inputs. Saves information about
     * execution.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        String className = (String) workItem.getParameter("class");
        String methodName = (String) workItem.getParameter("method");

        addExecution(className, methodName);
        try {
            Class<?> c = Class.forName(className);
            Method method = null;
            Object object = null;

            for (Method m : c.getMethods()) {
                if (m.getName().equals(methodName) && (m.getParameterTypes().length == 1)) {
                    method = m;
                    break;
                }
            }
            if (method == null) {
                throw new NoSuchMethodException(className + "." + methodName + "(..)");
            }

            if (!Modifier.isStatic(method.getModifiers())) {
                object = c.newInstance();
            }
            method.invoke(object, new Object[1]);

            manager.completeWorkItem(workItem.getId(), null);
        } catch (Exception e) {
            manager.abortWorkItem(workItem.getId());
            exceptions.add(e);
        }
    }

    @Override
    public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
        // Do nothing
    }

    private void addExecution(String className, String methodName) {
        ExecutionInfo temp = new ExecutionInfo(className, methodName);
        for (ExecutionInfo info : execInfo) {
            if (info.equals(temp)) {
                info.inc();
                break;
            }
        }
        execInfo.add(temp);
    }

    @Override
    public Set<ExecutionInfo> getExecutionInfo() {
        return execInfo;
    }

    public List<Exception> getExceptions() {
        return this.exceptions;
    }
}

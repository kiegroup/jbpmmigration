package org.jbpm.migration.tools.bpmn2;

import org.assertj.core.api.Assertions;

/**
 * Assertions for the work item handler execution.
 */
public class ExecutionAssert {

    /**
     * Asserts whether the given class and method was executed at least once.
     *
     * @param execInfo
     *            instance of ExecutionInfoSupport - handler tracking the
     *            execution.
     * @param className
     *            name of the called class.
     * @param methodName
     *            name of the called method.
     *
     */
    public static void assertExecuted(ExecutionInfoSupport execInfo, String className, String methodName) {
        ExecutionInfo info = getInfo(execInfo, className, methodName);
        Assertions.assertThat(info.ExecutionCount()).isGreaterThan(0);
    }

    /**
     * Asserts whether the given class and method was executed at least <i>n</i>
     * times.
     *
     * @param execInfo
     *            instance of ExecutionInfoSupport - handler tracking the
     *            execution.
     * @param className
     *            name of the called class.
     * @param methodName
     *            name of the called method.
     * @param n
     *            how many times should have been the class.method called.
     *
     */
    public static void assertExecutedAtLeast(ExecutionInfoSupport execInfo, String className, String methodName, 
        int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("n must be zero or positive integer, but was: %s", n));
        }
        ExecutionInfo info = getInfo(execInfo, className, methodName);
        Assertions.assertThat(info.ExecutionCount()).isGreaterThanOrEqualTo(n);
    }

    /**
     * Asserts whether the given class and method was executed exactly <i>n</i>
     * times.
     *
     * @param execInfo
     *            instance of ExecutionInfoSupport - handler tracking the
     *            execution.
     * @param className
     *            name of the called class.
     * @param methodName
     *            name of the called method.
     * @param n
     *            how many times should have been the class.method called.
     *
     */
    public static void assertExecutedExactly(ExecutionInfoSupport execInfo, String className, String methodName, 
        int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("n must be zero or positive integer, but was: %s", n));
        }
        ExecutionInfo info = getInfo(execInfo, className, methodName);
        Assertions.assertThat(info.ExecutionCount()).isEqualTo(n);
    }

    /**
     * Asserts whether the given class and method was executed at most <i>n</i>
     * times.
     *
     * @param execInfo
     *            instance of ExecutionInfoSupport - handler tracking the
     *            execution.
     * @param className
     *            name of the called class.
     * @param methodName
     *            name of the called method.
     * @param n
     *            how many times should have been the class.method called.
     *
     */
    public static void assertExecutedAtMost(ExecutionInfoSupport execInfo, String className, String methodName, int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("n must be zero or positive integer, but was: %s", n));
        }
        ExecutionInfo info = getInfo(execInfo, className, methodName);
        Assertions.assertThat(info.ExecutionCount()).isLessThanOrEqualTo(n);
    }

    private static ExecutionInfo getInfo(ExecutionInfoSupport execInfo, String className, String methodName) {
        ExecutionInfo temp = new ExecutionInfo(className, methodName);
        for (ExecutionInfo info : execInfo.getExecutionInfo()) {
            if (info.equals(temp)) {
                return info;
            }
        }
        return temp.reset();
    }
}

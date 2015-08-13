package org.jbpm.migration.tools.listeners;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.jbpm.migration.tools.listeners.TrackingProcessEventListener;

/**
 * Asserts usable to find out if/how many times were nodes in process visited.
 * Works with TrackingProcessEventListener. If correct order of visited nodes is
 * known use <link>IterableListenerAssert</link>.
 */
public class TrackingListenerAssert {

    public static void assertTriggeredAndLeft(TrackingProcessEventListener listener, String nodeName) {
        assertTriggered(listener, nodeName);
        assertLeft(listener, nodeName);
    }

    public static void assertTriggered(TrackingProcessEventListener listener, String nodeName) {
        Assertions.assertThat(listener.wasNodeTriggered(nodeName)).isTrue();
    }

    public static void assertLeft(TrackingProcessEventListener listener, String nodeName) {
        Assertions.assertThat(listener.wasNodeLeft(nodeName)).isTrue();
    }

    public static void assertProcessStarted(TrackingProcessEventListener listener, String processId) {
        Assertions.assertThat(listener.wasProcessStarted(processId)).isTrue();
    }

    public static void assertProcessCompleted(TrackingProcessEventListener listener, String processId) {
        Assertions.assertThat(listener.wasProcessCompleted(processId)).isTrue();
    }

    public static void assertChangedVariable(TrackingProcessEventListener listener, String variableId) {
        Assertions.assertThat(listener.wasVariableChanged(variableId)).isTrue();
    }

    /**
     * Asserts that the node with the given name was triggered <i>count</i>
     * times
     *
     * @param listener
     *            process event listener
     * @param nodeName
     *            name of the node which is tested
     * @param count
     *            how many times is expected the node had been triggered
     */
    public static void assertTriggered(TrackingProcessEventListener listener, String nodeName, int count) {
        Assertions.assertThat(containsNode(listener.getNodesTriggered(), nodeName)).isEqualTo(count);
    }

    /**
     * Asserts that the node with the given name was left <i>count</i> times
     * 
     * @param listener
     *            process event listener
     * @param nodeName
     *            name of the node which is tested
     * @param count
     *            how many times is expected the node had been left
     */
    public static void assertLeft(TrackingProcessEventListener listener, String nodeName, int count) {
        Assertions.assertThat(containsNode(listener.getNodesLeft(), nodeName)).isEqualTo(count);
    }

    /**
     * @return number of node's occurrences in given list
     */
    private static int containsNode(List<String> nodes, String nodeName) {
        int count = 0;
        for (String node : nodes) {
            if (node.equals(nodeName))
                count++;
        }
        return count;
    }
}

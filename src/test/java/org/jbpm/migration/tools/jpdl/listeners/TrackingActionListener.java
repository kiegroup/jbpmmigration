/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.migration.tools.jpdl.listeners;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.graph.def.Event;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;

/**
 * Tracking Listener. saves events and node connected with action
 */
public class TrackingActionListener {
    private List<Event> acceptedEvents = new ArrayList<Event>();
    private List<Node> firedNodes = new ArrayList<Node>();

    public void addNode(Node n) {
        if (n != null) {
            firedNodes.add(n);
        }
    }

    public void addEvent(Event e) {
        if (e != null) {
            acceptedEvents.add(e);
        }
    }

    /**
     * Adds information about both node and event from actual execution context.
     *
     * @param ctx
     *            execution context (should be passed from the action handler)
     */
    public void addFromContext(ExecutionContext ctx) {
        addEvent(ctx.getEvent());
        addNode(ctx.getNode());
    }

    /**
     * Tells whether was action called on the event.
     *
     * @param eventType
     *            type of the event
     */
    public boolean wasEventAccepted(String eventType) {
        for (Event e : getEvents()) {
            if (e.getEventType().equals(eventType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether was action called on the event.
     *
     * @param event
     *            event instance
     */
    public boolean wasEventAccepted(Event event) {
        return getEvents().contains(event);
    }

    /**
     * Tells whether was action called on the given node.
     *
     * @param nodeName
     *            name of the node
     */
    public boolean wasCalledOnNode(String nodeName) {
        for (Node n : getNodes()) {
            if (n.getName().equals(nodeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether was action called on the given node.
     *
     * @param node
     *            node instance
     */
    public boolean wasCalledOnNode(Node node) {
        return getNodes().contains(node);
    }

    /**
     * @return all recorded events
     */
    public List<Event> getEvents() {
        return this.acceptedEvents;
    }

    /**
     * @return all recorded nodes
     */
    public List<Node> getNodes() {
        return this.firedNodes;
    }

    /**
     * clears both nodes and event recordings
     */
    public void clear() {
        getEvents().clear();
        getNodes().clear();
    }
}

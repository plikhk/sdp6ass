package org.example;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DirectoryNode implements Element {
    private final String name;
    private final Map<String, Element> children = new TreeMap<>();
    private DirectoryNode parent;

    public DirectoryNode(String name) {
        if (name == null || name.contains("/")) {
            throw new IllegalArgumentException("Invalid directory name: " + name);
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        long total = 0;
        for (Element child : children.values()) {
            total += child.getSize();
        }
        return total;
    }

    @Override
    public DirectoryNode getParent() {
        return parent;
    }

    @Override
    public void setParent(DirectoryNode parent) {
        this.parent = parent;
    }

    public void addChild(Element child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }
        String childName = child.getName();
        if (children.containsKey(childName)) {
            throw new IllegalArgumentException("Duplicate sibling name: " + childName);
        }
        if (child.getParent() != null) {
            throw new IllegalStateException("Element already belongs to a parent.");
        }
        if (isAncestor(child)) {
            throw new IllegalArgumentException("Cycle detected: cannot add ancestor as child.");
        }

        child.setParent(this);
        children.put(childName, child);
    }

    public Map<String, Element> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    private boolean isAncestor(Element target) {
        DirectoryNode current = this;
        while (current != null) {
            if (current == target) return true;
            current = current.getParent();
        }
        return false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Element child : children.values()) {
            child.accept(visitor);
        }
    }
}
package org.example;

public interface Element {
    String getName();
    void accept(Visitor visitor);
    long getSize();
    DirectoryNode getParent();
    void setParent(DirectoryNode parent);
}
package com.datahelper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class Node {
    private int id;
    private int pId;
    private Node parent;
    private List<Node> childs = new ArrayList<Node>();
    private String tagName;
    private boolean isExpandable;
    private int nodeLevel;

    public List<Node> getChild() {
        return childs;
    }

    public void addChild(Node child) {

        if (!this.childs.contains(child)) {
            this.childs.add(child);
        }
    }

    public int getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(int nodeLevel) {

        if (nodeLevel >= 0){
            this.nodeLevel = nodeLevel;
        }
        else{
            this.nodeLevel = -1;
        }
    }

    public Node() {

        this.id = -1;
        this.pId = -1;
        this.nodeLevel = -1;
    }

    public Node(int id, int pId, String tagName) {
        this.id = id;
        this.pId = pId;
        this.tagName = tagName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        }else{
            this.id = -1;
        }
    }

    public boolean isExpandable() {
        return isExpandable;
    }
    public boolean isLeaf() {
        return (this.childs.size() == 0);
    }
    public void setExpandable(boolean expandable) {
        isExpandable = expandable;

        Node parentNode = this.getParent();
        if (expandable && parentNode != null){
            parentNode.setExpandable(expandable);
        }
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        if (pId >= 0){
            this.pId = pId;
        }else{
            this.pId = -1;
        }
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isRootNode(){
        return this.parent == null;
    }
}

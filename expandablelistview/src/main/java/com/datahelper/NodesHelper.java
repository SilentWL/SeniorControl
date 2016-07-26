package com.datahelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class NodesHelper{
    private static NodesHelper mNodesHelper = null;
    private NodesHelper(){

    }
    public static NodesHelper newInstance(){
        if (mNodesHelper == null){
            synchronized (NodesHelper.class){
                if (mNodesHelper == null){
                    mNodesHelper = new NodesHelper();
                }
            }
        }
        return  mNodesHelper;
    }

    public <T> List<Node> convertDataToNodes(List<T> datas) throws IllegalAccessException {
        ArrayList<Node> nodes = new ArrayList<Node>();

        for(T data : datas){
            Class<?> clzz = data.getClass();
            Field[] fields = clzz.getDeclaredFields();
            int nodeId = -1;
            int nodePid = -1;
            String nodeTagName = null;
            boolean newNode = false;

            for(Field field:fields){
                if (field.isAnnotationPresent(AnnotationNodeId.class)){
                    field.setAccessible(true);
                    nodeId = field.getInt(data);
                    newNode = true;
                }else if (field.isAnnotationPresent(AnnotationNodePid.class)){
                    field.setAccessible(true);
                    nodePid = field.getInt(data);
                    newNode = true;
                }else if (field.isAnnotationPresent(AnnotationNodeTagString.class)){
                    field.setAccessible(true);
                    nodeTagName = (String)field.get(data);
                    newNode = true;
                }
            }
            if (newNode){
                nodes.add(new Node(nodeId, nodePid, nodeTagName));
            }
        }
        buildNodes(nodes);
        return nodes;
    }
    public void buildNodes(List<Node> nodes){
        int nodeCount = nodes.size();
        for (int i = 0; i < nodeCount; i++){
            Node node = nodes.get(i);

            for (int j = i+1; j < nodeCount; j++){
                if (node.getpId() != -1 && node.getpId() == nodes.get(j).getId()){
                    node.setParent(nodes.get(j));
                    nodes.get(j).addChild(node);
                }else if (nodes.get(j).getpId() != -1 && node.getId() == nodes.get(j).getpId()){
                    node.addChild(nodes.get(j));
                    nodes.get(j).setParent(node);
                }
            }
        }
        List<Node> rootNodes = listRootNodes(nodes);

        generateLevels(rootNodes, 0);
    }
    private void generateLevels(List<Node> rootNodes, int level){
        for(Node rootNode : rootNodes){
            rootNode.setNodeLevel(level);
            generateLevels(rootNode.getChild(), level+1);
        }

    }
    public List<Node> listRootNodes(List<Node> nodes){
        List<Node> rootNodes = new ArrayList<Node>();

        for(Node node:nodes){
            if (node.isRootNode()){
                rootNodes.add(node);
            }
        }

        return  (rootNodes.size() != 0)?rootNodes:null;
    }

    private void addVisiableNodes(List<Node> srcNodes, List<Node> dstNodes){
        for (Node node:srcNodes){
            dstNodes.add(node);
            if (node.isExpandable() && node.getChild().size() > 0) {
                addVisiableNodes(node.getChild(), dstNodes);
            }

        }
    }
    public List<Node> listVisiableNodesFromRootNodes(List<Node> rootNodes){
        List<Node> expandableNodes = new ArrayList<Node>();

        addVisiableNodes(rootNodes, expandableNodes);

        return (expandableNodes.size() != 0)?expandableNodes:null;
    }

    private Node getNodeById(int nodeId, List<Node> nodes){
        Node desNode = null;
        for(Node node : nodes){
            if (node.getId() == nodeId){
                desNode = node;
                break;
            }else if (!node.isLeaf()){
                if ((desNode = getNodeById(nodeId, node.getChild())) != null){
                    break;
                }
            }
        }
        return desNode;

    }
    private int getNodeMaxId(List<Node> nodes){
        int maxId = -1;
        for(Node node:nodes){
            if (node.getId() >= maxId){
                maxId = node.getId();
            }
            if (!node.isLeaf()){
                int tempMaxId = -1;
                if ((tempMaxId = getNodeMaxId(node.getChild())) >= maxId){
                    maxId = tempMaxId;
                }
            }
        }
        return maxId;
    }
    public int obtainAvailableId(List<Node> allNodes){
        int maxId = -1;
        int desId = -1;

        maxId = getNodeMaxId(allNodes);
        if (allNodes.size() == maxId + 1){
            maxId = -1;
        }

        for(int i = 0; i <= maxId; i++){
            if (getNodeById(i, allNodes) == null){
                desId = i;
                break;
            }
        }

        return ((desId == -1) ? maxId + 1 : desId);
    }
}

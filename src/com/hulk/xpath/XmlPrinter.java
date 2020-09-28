package com.hulk.xpath;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml信息打印器
 * @author hulk
 *
 */
public class XmlPrinter {
	
	/**
	 * 打印节点信息
	 * @param tag
	 * @param nodeList
	 * @param printContent
	 */
	public static void print(String tag, NodeList nodeList, boolean printContent) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node == null) {
                continue;
            }
            print(tag, node, printContent);
        }
    }
	
	/**
	 * 打印节点信息
	 * @param tag
	 * @param nodeInfoList
	 * @param printContent
	 */
	public static void print(String tag, List<NodeInfo> nodeInfoList) {
        for (int i = 0; i < nodeInfoList.size(); i++) {
        	NodeInfo node = nodeInfoList.get(i);
            if (node == null) {
                continue;
            }
            print(tag, node.toString());
        }
    }
	
	/**
	 * 打印节点信息
	 * @param tag
	 * @param printContent
	 * @param nodeList
	 */
    public static void print(String tag, boolean printContent, Node... nodeList) {
        if (nodeList == null) {
            return;
        }
        for (int i = 0; i < nodeList.length; i++) {
            Node node = nodeList[i];
            if (node == null) {
                continue;
            }
            print(tag, node, printContent);
        }
    }
    
    /**
     * 打印节点信息
     * @param tag 打印TAG
     * @param nodeList 节点列表
     * @param printContent 是否打印节点里中的内容: 是所有子节点的内容值,没有子节点的名称
     */
    public static void print(String tag, List<Node> nodeList, boolean printContent) {
        if (nodeList == null) {
            return;
        }
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            if (node == null) {
                continue;
            }
            print(tag, node, printContent);
        }
    }
    
    /**
     * 打印节点信息
     * @param tag 打印TAG
     * @param nodes 节点集合
     * @param printContent 是否打印节点里中的内容: 是所有子节点的内容值,没有子节点的名称
     */
    public static void print(String tag, Set<Node> nodes, boolean printContent) {
        if (nodes == null) {
            return;
        }
        Iterator<Node> it = nodes.iterator();
        while(it.hasNext()) {
        	Node node = it.next();
        	if (node == null) {
                continue;
            }
            print(tag, node, printContent);
        }
    }
    
    /**
     * 打印节点信息
     * @param tag 打印TAG
     * @param node 节点
     * @param printContent 是否打印节点里中的内容: 是所有子节点的内容值,没有子节点的名称
     */
    public static void print(String tag, Node node, boolean printContent) {
        StringBuffer buff = new StringBuffer("Name: " + node.getNodeName());
        //buff.append(" /Type: " + node.getNodeType());
        if(printContent && node.getNodeValue() != null) {
        	buff.append(" /Value: " + node.getNodeValue());
        }
        if(printContent && node.getTextContent() != null) {
        	buff.append(" /Content: " + node.getTextContent());
        }
        print(tag, buff.toString());
    }

    /**
     * 打印到屏幕
     * @param tag TAG
     * @param msg 打印信息
     */
    public static void print(String tag, String msg) {
        System.out.println(tag + ": " + msg);
    }
    
    /**
     * 打印日志
     * @param tag
     * @param msg
     */
    public static void log(String tag, String msg) {
        print(tag, msg);
    }
}

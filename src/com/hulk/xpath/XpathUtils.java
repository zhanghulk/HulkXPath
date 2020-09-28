package com.hulk.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathUtils {

	private static final String TAG = "XpathUtils";
	
	private static boolean DEBUG = false;

	/**
	 * 转换节点的名称和值
	 * @param nodeList
	 * @return
	 */
	public static NodeInfo parseNode(Node node) {
        if (node == null) {
            return null;
        }
        int type = node.getNodeType();
        String name = node.getNodeName();
        if (!isElementNode(type)) {
        	//不是元素级别的节点忽略掉
        	log("parseNode: Ignored not element node=" + name + ",type=" + type);
        	return null;
        }
        if (!node.hasChildNodes()) {
        	log("parseNode: Ignored has no Child node=" + name);
        	return null;
        }
        log("parseNode: Start parse node name: " + name);
        NodeInfo info = new NodeInfo(name);
        info.type = type;
        NamedNodeMap attrsMap = node.getAttributes();
        if(attrsMap != null && attrsMap.getLength() > 0) {
        	info.attrs = parseAttributes(attrsMap);
        	log("parseNode: parsed attrs=" + info.attrs);
        }
        NodeList childNodes = node.getChildNodes();
    	int childLength = childNodes.getLength();
    	if(childLength == 1) {
    		//注意: #text 标签也是node的孩子,
            //如 <title>Java Examples</title> 中的"Java Examples"
    		String content = node.getTextContent();
    		info.content = content;
    		log("parseNode: parsed content=" + content);
    	} else {
    		if(childLength > 1) {
    			//多个孩子的节点,执行node.getTextContent();返回子节点中所有的#text
    			//使用递归再次解析, eg:
    			/*
    			<item language="Java">
    				<title>Java Examples</title>
    				<link>http://www.javacodegeeks.com/</link>
    			</item>
    			*/
        		log("parseNode: recursion to parse child nodes for node name: " + name);
        		List<NodeInfo> childs = parseNodeList(childNodes);
        		info.childs = childs;
        	}
    	}
        return info;
    }
	
	/**
	 * 解析节点属性
	 * @param attrNodeMap
	 * @return
	 */
	public static Map<String, String> parseAttributes(NamedNodeMap attrNodeMap) {
		if(attrNodeMap == null) {
			log("parseAttributes: Ignored attrNodeMap is null");
			return null;
		}
		Map<String, String> attrs = new HashMap<String, String>();
		int length = attrNodeMap.getLength();
		for(int i = 0; i < length; i++) {
			Node node = attrNodeMap.item(i);
			String name = node.getNodeName();
			String value = node.getNodeValue();
			attrs.put(name, value);
		}
		return attrs;
	}
	
	/**
	 *  解析节点属性
	 * @param node
	 * @return
	 */
	public static Map<String, String> parseNodeAttributes(Node node) {
		if(node == null) {
			return null;
		}
		NamedNodeMap attrsMap = node.getAttributes();
		return parseAttributes(attrsMap);
	}
	
	/**
	 * 转换节点列表的名称和值
	 * @param nodeList
	 * @return
	 */
	public static List<NodeInfo> parseNodeList(NodeList nodeList) {
		List<NodeInfo> list = new ArrayList<NodeInfo>(); 
		int length = nodeList.getLength();
		log("parseNodeList: length=" + length);
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            if (node == null) {
                continue;
            }
            NodeInfo info = parseNode(node);
            if(info != null) {
            	list.add(info);
            }
        }
        return list;
    }
	
	/**
	 * 转换节点列表的名称和值
	 * @param nodeList
	 * @return
	 */
	public static List<NodeInfo> parseNodeList(List<Node> nodeList) {
		List<NodeInfo> list = new ArrayList<NodeInfo>(); 
		int length = nodeList.size();
		log("parseNodeList: length=" + length);
        for (int i = 0; i < length; i++) {
            Node node = nodeList.get(i);
            if (node == null) {
                continue;
            }
            NodeInfo info = parseNode(node);
            if(info != null) {
            	list.add(info);
            }
        }
        return list;
    }
	
	/**
	 * 是否为元素节点:<title>Java Examples</title>
	 * @param node
	 * @return
	 */
	public static boolean isElementNode(Node node) {
		if(node== null) {
			return false;
		}
		int nodeType = node.getNodeType();
		return isElementNode(nodeType);
	}
	
	/**
	 * 是否为元素节点:<title>Java Examples</title>
	 * @param nodeType
	 * @return
	 */
	public static boolean isElementNode(int nodeType) {
		return nodeType == Node.ELEMENT_NODE;
	}
	
	/**
	 * 是否为属性节点
	 * @param nodeType
	 * @return
	 */
	public static boolean isAttributeNode(int nodeType) {
		return nodeType == Node.ATTRIBUTE_NODE;
	}
	
	/**
	 * 是否为文本内容节点
	 * @param nodeType
	 * @return
	 */
	public static boolean isTextNode(int nodeType) {
		return nodeType == Node.TEXT_NODE;
	}
	
	private static void log(String msg) {
		if(DEBUG) {
			XmlPrinter.log(TAG, msg);
		}
    }
	
	public static void setDebug(boolean debug) {
		DEBUG = debug;
	}
}

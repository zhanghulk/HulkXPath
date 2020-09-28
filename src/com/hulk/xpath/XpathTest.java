package com.hulk.xpath;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Xpath测试程序:
 * @author hulk
 *
 */
public class XpathTest {

	private final static String TAG = "XpathTest";
	private final static boolean DEBUG = true;
	static XpathParser sXpathParser;
	
	public static void main(String[] args) throws Exception {
		XmlPrinter.print(TAG, "Test XpathParser");
		//执行xml解析
		log("执行xml解析");
		sXpathParser = new XpathParser();
		//sXpathParser.parseXml(XML.RSS_XML.trim());
		String  filePath = "rss_demo.xml";
		sXpathParser.parseFile(filePath);
		Node node;
		
		//获取根节点
		log("获取根节点");
		node = sXpathParser.getRootNode();
		XmlPrinter.print("Root", node, false);
		//通过名称获取根节点
		log("通过名称获取根节点");
		node = sXpathParser.getRootNode("rss");
		XmlPrinter.print("Root rss", node, false);
		
		//解析节点列表的节点和器子节点的全部信息
		log("解析节点列表的节点和器子节点的全部信息");
		Map<String, String> attrMap = XpathUtils.parseNodeAttributes(node);
		XmlPrinter.log("Node attr map", attrMap.toString());
		
		NodeList nodeList = null;
		//打印根节点下的所有元素节点
		log("打印根节点下的所有元素节点");
		Set<Node> set = sXpathParser.getRootChildNodes();
		XmlPrinter.print("RootChildNodes", set, false);
		
		///rss/channel/*下所有节点, 
		log("获取rss/channel/*下所有节点");
		nodeList = sXpathParser.getExpressionNodeList("/rss/channel/*");
		XmlPrinter.print("//rss/channel/* ExpressionNodeList", nodeList, false);
		
		//解析节点列表的节点和器子节点的全部信息
		log("解析节点列表的节点和器子节点的全部信息");
		List<NodeInfo> nodeInfoList = XpathUtils.parseNodeList(nodeList);
		XmlPrinter.print("//rss/channel/* nodeInfoList", nodeInfoList);
		
		
		//通过计算表达式获取元素方式
		log("通过计算表达式获取元素方式");
		//获取第一本书信息
		log("获取第一本书信息");
		String expression = "//bookstore/book[1]";
		nodeList = sXpathParser.getExpressionNodeList(expression);
		XmlPrinter.print(expression, nodeList, true);
				
		//获取第一本书标题
		log("获取第一本书标题");
		expression = "//bookstore/book[1]/title";
		nodeList = sXpathParser.getExpressionNodeList(expression);
		XmlPrinter.print(expression, nodeList, true);
		
		//价格条件筛选
		log("价格条件筛选");
		expression = "//bookstore/book[price>35.00]/title";
		nodeList = sXpathParser.getExpressionNodeList(expression);
		XmlPrinter.print(expression, nodeList, true);
	}
	
	private static void log(String msg) {
		if(DEBUG) {
			XmlPrinter.log(TAG, msg);
		}
    }
}

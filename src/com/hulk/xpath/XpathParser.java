package com.hulk.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Xpth java 解析器
 * <p>ref: https://my.oschina.net/cloudcoder/bprint/223359
 * <p> eg rss_demo.xml:
 *
 * @Time: 2019-04-28 17:56
 * @author: zhanghao
 */
public class XpathParser {

	public static final String TAG = "XpathParser";
	public static boolean DEBUG = false;
	private Document doc;
    private XPath xpath;

    /**
     * Xpath构造器
     */
    public XpathParser() {
    	//do something
    }
    
    /**
     * Xpath构造器
     * @param xmlText
     * @throws Exception
     */
    public void XpathParser(String xmlText) throws Exception {
    	parseXml(xmlText);
    }
    
    /**
     * Xpath构造器
     * @param input 输入流:
     * <p> eg:  new ByteArrayInputStream(xmlText.getBytes()) or new FileInputStream(new File(xmlFilePath))
     * @throws Exception
     */
    public void XpathParser(InputStream input) throws Exception {
    	doParse(input);
    }
    
    /**
     * 初始化Document、XPath对象
     * @param xmlText XMl文本
     * @throws Exception
     */
    public void parseXml(String xmlText) throws Exception {
        doParse(new ByteArrayInputStream(xmlText.getBytes()));
    }

    /**
     * 初始化Document、XPath对象
     * @param xmlFilePath "demo.xml"
     * @throws Exception
     */
    public void parseFile(String xmlFilePath) throws Exception {
        doParse(new FileInputStream(new File(xmlFilePath)));
    }

    /**
     * 执行解析操作
     * 初始化Document、XPath对象
     * @param is 输入流
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws Exception
     */
    private void doParse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        // 创建Document对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(is);

        // 创建XPath对象
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
    }

    /**
     * 获取根元素
     * @return
     * @throws XPathExpressionException
     */
    public Node getRootNode() throws XPathExpressionException {
        //根节点的名称, eg /* or /rss (rss为实际文本根节点名称)
        Node node = (Node) xpath.evaluate("/*", doc, XPathConstants.NODE);
        return node;
    }

    /**
     * 获取根元素, " /*" or "/rss" (rss为实际文本根节点名称)
     * xml文件只能有一个根元素,如果有多个解析会出现如下异常:
     * [Fatal Error] :40:2: The markup in the document following the root element must be well-formed.
     * @return
     * @throws XPathExpressionException
     */
    public Node getRootNode(String rootNodeName) throws XPathExpressionException {
        Node node = (Node) xpath.evaluate("/" + rootNodeName, doc, XPathConstants.NODE);
        return node;
    }

    /**
     * 根据自定义表达式，获取符合条件的所有元素
     * <p>通用节点查询接口函数
     * @param expression  
     * <p>eg (1) "/rss/channel/*": 获取/rss/channel/下面的所有子元素;
     * <p>   (2) "//bookstore/book[price>35.00]/title" 表示获取 获取所有大于指定价格的书箱 (表达式前面必须为//)
     * @return
     * @throws XPathExpressionException
     */
    public NodeList getExpressionNodeList(String expression) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        return nodeList;
    }

    /**
     * 获取指定名称的部分元素
       eg 只获取元素名称为title的元素
     * @param name 节点名称 eg title
     * @throws XPathExpressionException
     */
    public NodeList getPartNodeList(String name) throws XPathExpressionException {
        String expression = "//*[name() = '" + name + "']";
        NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        return nodeList;
    }

    /**
     * 获取包含子节点的元素
     * @return
     * @throws XPathExpressionException
     */
    public NodeList haveChildNodes() throws XPathExpressionException {
        NodeList nodeList = (NodeList) xpath.evaluate("//*[*]", doc, XPathConstants.NODESET);
        return nodeList;
    }

    /**
     * 获取指定层级的元素
     * @param levels eg, 3 (获取第三层的全部元素)
     * @return
     * @throws XPathExpressionException
     */
    public NodeList getLevelElements(int levels) throws XPathExpressionException, IllegalArgumentException {
        if (levels <= 0) {
            throw new IllegalArgumentException("Invalid levels: " + levels);
        }
        //eg: "/*/*/*/*"
        StringBuffer expression = new StringBuffer();
        for (int i = 0; i < levels; i++) {
            expression.append("/*");
        }
        NodeList nodeList = (NodeList) xpath.evaluate(expression.toString(), doc, XPathConstants.NODESET);
        return nodeList;
    }
    
    /**
     * 获取根节点下所有的元素节点
     * @return
     */
    public Set<Node> getRootChildNodes() {
        if (doc == null) {
            return null;
        }
        Set<Node> set = new HashSet<>();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			int nodeType = node.getNodeType();
			if (XpathUtils.isElementNode(nodeType)) {
				set.add(node);
			} else {
				//log("getRootChildNodes: Ignored nodeType=" + nodeType + ", Name=" + node.getNodeName());
			}
		}
        return set;
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

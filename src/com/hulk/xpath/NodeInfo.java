package com.hulk.xpath;

import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;

/**
 * 简化版的节点信息
 * 
 * @author hulk
 *
 */
public class NodeInfo {
	/**
	 * 节点名称
	 */
	public String name;
	/**
	 * 节点类型
	 */
	public int type;
	/**
	 * 节点内容, 如 如 <title>Java Examples</title> 中的"Java Examples"
	 */
	public String content;
	/**
	 * 节点值,通常为空, 通常为属性值, 如:
	 * <item language="Java">
			<title>Java Examples</title>
			<link>http://www.javacodegeeks.com/</link>
		</item>
	 */
	public String value;

	/**
	 * 属性字典
	 */
	public Map<String, String> attrs;
	
	/**
	 * 节点孩子列表,为null表示没有
	 */
	public List<NodeInfo> childs;
	
	public NodeInfo() {
	}

	public NodeInfo(String name) {
		this.name = name;
	}
	
	public NodeInfo(String name, String content) {
		this.name = name;
		this.content = content;
	}

	@Override
	public String toString() {
		return "NodeInfo{name=" + name + ",type=" + type + ",content=" + content + ",value=" + value + ", attrs=" + attrs + ", childs=" + childs + "}";
	}
}

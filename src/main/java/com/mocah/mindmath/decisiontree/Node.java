/**
 *
 */
package com.mocah.mindmath.decisiontree;

import java.util.List;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Node {
	private String nodeid;
	private NodeType nodetype;
	private ValueType valuetype;
	private List<Child> childs;

	public String getId() {
		return this.nodeid;
	}

	public List<Child> getChildren() {
		return this.childs;
	}

	public boolean hasChild() {
		return (this.childs != null && childs.size() > 0);
	}

	public NodeType getType() {
		return this.nodetype;
	}

	public ValueType getValueType() {
		return this.valuetype;
	}

	@Override
	public String toString() {
		return this.nodeid;
	}
}

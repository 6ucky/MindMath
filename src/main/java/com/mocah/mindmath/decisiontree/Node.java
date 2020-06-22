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
	private String feedback_id;

	/**
	 * @return
	 */
	public String getId() {
		return this.nodeid;
	}

	/**
	 * @return
	 */
	public List<Child> getChildren() {
		return this.childs;
	}

	/**
	 * @param childId the id of the child
	 * @return a {@code Child} object if the node has the child, {@code null}
	 *         otherwise
	 */
	public Child getChild(String childId) {
		Child child = null;

		for (Child c : childs) {
			if (c.getId().equals(childId)) {
				child = c;
			}
		}

		return child;
	}

	/**
	 * @param node the node object representing the child (id will be compared)
	 * @return a {@code Child} object if the node has the child, {@code null}
	 *         otherwise
	 */
	public Child getChild(Node node) {
		if (node != null)
			return getChild(node.getId());

		return null;
	}

	/**
	 * @return
	 */
	public boolean hasChilds() {
		return (this.childs != null && childs.size() > 0);
	}

	/**
	 * @return
	 */
	public NodeType getType() {
		return this.nodetype;
	}

	/**
	 * @return
	 */
	public ValueType getValueType() {
		return this.valuetype;
	}

	/**
	 * @return the id of the feedback, {@code null} if call on a non feedback node
	 *         or id not found
	 */
	public String getFeedbackId() {
		if (this.nodetype == NodeType.FEEDBACK)
			return feedback_id;

		return null;
	}

	@Override
	public String toString() {
		return this.nodeid;
	}
}

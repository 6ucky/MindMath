/**
 *
 */
package com.mocah.mindmath.decisiontree;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.google.gson.JsonPrimitive;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Tree {
	private String root;
	private List<Node> nodes;

	/**
	 * @return
	 */
	public String getRootId() {
		return this.root;
	}

	/**
	 * @return
	 */
	public Node getRoot() {
		return getNodeById(this.root);
	}

	/**
	 * @param id
	 * @return
	 */
	public Node getNodeById(String id) {
		for (Node n : nodes) {
			if (n.getId().equals(id))
				return n;
		}

		return null;
	}

	/**
	 * Based on code https://www.baeldung.com/java-print-binary-tree-diagram
	 *
	 * @author Yavuz Tas
	 *
	 * @param root
	 * @return
	 */
	private String traversePreOrder(Node root) {
		if (root == null)
			return "";

		Set<Node> visitedNodes = new HashSet<>();
		visitedNodes.add(root);

		StringBuilder sb = new StringBuilder();
		sb.append(root.getId());
		sb.append(" <" + root.getType() + " / " + root.getValueType() + ">");

		ListIterator<Child> it = root.getChildren().listIterator();
		while (it.hasNext()) {
			Child myChild = it.next();

			String childpointer = (it.hasNext()) ? "├──" : "└──";

			traverseNodes(visitedNodes, sb, "", childpointer, myChild, it.hasNext());
		}

		StringBuilder unlinkedBuilder = new StringBuilder();
		boolean hasUnlinked = false;
		for (Node node : this.nodes) {
			if (!visitedNodes.contains(node)) {
				unlinkedBuilder.append("\n");
				unlinkedBuilder.append(node.getId());
				hasUnlinked = true;
			}
		}

		if (hasUnlinked) {
			sb.append("\n\nUNLINKED NODES :");
			sb.append(unlinkedBuilder);
		}

		return sb.toString();
	}

	/**
	 * Based on code https://www.baeldung.com/java-print-binary-tree-diagram
	 *
	 * @author Yavuz Tas
	 *
	 * @param sb
	 * @param padding
	 * @param pointer
	 * @param child
	 * @param hasOtherSibling
	 */
	private void traverseNodes(Set<Node> visitedNodes, StringBuilder sb, String padding, String pointer, Child child,
			boolean hasOtherSibling) {
		if (child != null) {
			Node node = getNodeById(child.getId());

			sb.append("\n");
			sb.append(padding);
			sb.append(pointer);

			Edge edge = child.getEdge();
			JsonPrimitive value = edge.getValue();
			sb.append("(if " + value + ") ");

			sb.append(child.getId());
			if (node != null) {
				visitedNodes.add(node);
				sb.append(" <" + node.getType() + " / " + node.getValueType() + ">");
			} else {
				sb.append(" <NODE MISSING>");
			}

			StringBuilder paddingBuilder = new StringBuilder(padding);
			if (hasOtherSibling) {
				paddingBuilder.append("│   ");
			} else {
				paddingBuilder.append("    ");
			}

			String paddingForAll = paddingBuilder.toString();

			if (node != null && node.hasChild()) {
				ListIterator<Child> it = node.getChildren().listIterator();
				while (it.hasNext()) {
					Child myChild = it.next();

					String childpointer = (it.hasNext()) ? "├──" : "└──";

					traverseNodes(visitedNodes, sb, paddingForAll, childpointer, myChild, it.hasNext());
				}
			}
		}
	}

	@Override
	public String toString() {
		return traversePreOrder(getNodeById(this.root));
	}
}

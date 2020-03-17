/**
 *
 */
package com.mocah.mindmath.decisiontree.search;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Queue;

import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.Tree;

/**
 * @author tsimonfine
 *
 */
public abstract class TreeSearch {
	protected final Tree tree;
	protected final LinkedHashSet<Node> visited;
	protected final Deque<Node> opened;

	/**
	 * @param tree
	 */
	public TreeSearch(Tree tree) {
		this.tree = tree;
		this.visited = new LinkedHashSet<>();
		this.opened = new ArrayDeque<>();
	}

	/**
	 * Add neighbors to the opened nodes
	 *
	 * @param node, the node to open
	 * @return
	 */
	protected abstract Queue<Node> open(Node node);

	/**
	 * Visit the next node
	 *
	 * @return the node visited
	 */
	protected abstract Node crawl();

	/**
	 * @param node
	 */
	public void visitNode(Node node) {
		this.visited.add(node);
	}

	/**
	 * @param node
	 * @return
	 */
	public boolean wasVisited(Node node) {
		return this.visited.contains(node);
	}

	/**
	 * @return
	 */
	public Tree getTree() {
		return this.tree;
	}

	/**
	 * @return
	 */
	public LinkedHashSet<Node> getVisitedNodes() {
		return this.visited;
	}
}

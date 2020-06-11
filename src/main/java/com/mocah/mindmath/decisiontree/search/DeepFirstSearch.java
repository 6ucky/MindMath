/**
 *
 */
package com.mocah.mindmath.decisiontree.search;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ListIterator;

import com.mocah.mindmath.decisiontree.Child;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.Tree;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DeepFirstSearch extends TreeSearch {

	public DeepFirstSearch(Tree tree) {
		super(tree);
	}

	@Override
	public Deque<Node> open(Node node) {
		Deque<Node> open = new ArrayDeque<>();

		if (node.hasChilds()) {
			ListIterator<Child> it = node.getChildren().listIterator();
			while (it.hasNext()) {
				Child child = it.next();
				Node n = this.tree.getNodeById(child.getId());

				if (n != null) {
					open.offerLast(n);
					this.opened.offerFirst(n);
				}
			}
		}

		return open;
	}

	@Override
	public Node crawl() {
		Node node = opened.pollFirst();
		this.visitNode(node);

		return node;
	}

	public boolean hasOpened() {
		return !this.opened.isEmpty();
	}

}

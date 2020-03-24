/**
 *
 */
package com.mocah.mindmath.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A Branch of a Mindmath Decision Tree is the set of all successive nodes from
 * the root to the first decision node with also all the feedback node leaves
 *
 * @author Thibaut SIMON-FINE
 */
public class Branch {
	private List<Node> stateNodes;
	private Node decisionNode;
	private List<Node> feedbackNodes;

	public Branch() {
		this.stateNodes = new ArrayList<>();
		this.feedbackNodes = new ArrayList<>();
	}

	/**
	 * Instantiate a new Branch from another instance
	 *
	 * @param b
	 */
	public Branch(Branch b) {
		this();
		if (b != null) {
			this.stateNodes.addAll(b.getStateNodes());
			this.decisionNode = b.getDecisionNode();
			this.feedbackNodes.addAll(b.getFeedbackNodes());
		}
	}

	/**
	 * Get the parameters of the state.
	 *
	 * @return list of nodes which represents the parameters of a state in the
	 *         environnement
	 */
	public List<Node> getStateNodes() {
		return stateNodes;
	}

	/**
	 * Add a parameter to the state
	 *
	 * @param n a node of type <code>NodeType.STATE</code>
	 */
	public void addStateNode(Node n) {
		this.stateNodes.add(n);
	}

	/**
	 * Add multiple parameters to the state
	 *
	 * @param c the collection of nodes of type <code>NodeType.STATE</code>
	 */
	public void addAllStateNodes(Collection<? extends Node> c) {
		this.stateNodes.addAll(c);
	}

	/**
	 * @return the decision node of the branch.
	 */
	public Node getDecisionNode() {
		return decisionNode;
	}

	/**
	 * Set a decision node <code>NodeType.DECISION</code>
	 *
	 * @param decisionNode
	 */
	public void setDecisionNode(Node decisionNode) {
		this.decisionNode = decisionNode;
	}

	/**
	 * @return a list of all the node for different feedbacks
	 */
	public List<Node> getFeedbackNodes() {
		return feedbackNodes;
	}

	/**
	 * @param n a node of type <code>NodeType.FEEDBACK</code>
	 */
	public void addFeedbackNode(Node n) {
		this.feedbackNodes.add(n);
	}

	/**
	 * @param c the collection of nodes of type <code>NodeType.FEEDBACK</code>
	 */
	public void addAllFeedbackNodes(Collection<? extends Node> c) {
		this.feedbackNodes.addAll(c);
	}

	@Override
	public String toString() {
		return this.stateNodes + " / " + this.decisionNode + " / " + this.feedbackNodes;
	}
}

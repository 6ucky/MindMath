package com.mocah.mindmath.repository;

import java.util.Deque;

import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.search.DeepFirstSearch;

public interface Matrixrepository{
	
	public static DeepFirstSearch getVisitedNode(DeepFirstSearch dfs, Node root) {
		dfs.visitNode(root);
		Deque<Node> opened = dfs.open(root);
		if (!opened.isEmpty())
		{
			Node child = opened.pollFirst();
			getVisitedNode(dfs, child);
		}
		return dfs;
	}

}

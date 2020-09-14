package com.mocah.mindmath.learning.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;

public class ExpertLearning extends AbstractLearning {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2431512030248522237L;
	
	private HashMap<IState, Integer> table;
	private final HashMap<IState, ArrayList<IValue>> qValues;

	public ExpertLearning(IPolicy policy) {
		this(policy, new HashMap<IState, ArrayList<IValue>>());
	}
	
	public ExpertLearning(IPolicy policy, HashMap<IState, ArrayList<IValue>> qValues) 
	{
		super(policy);
		this.qValues = qValues;
		this.table = new HashMap<IState, Integer>();
	}

	@Override
	public IAction step(IState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void learn(IState oldState, IAction action, double reward, IState newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IAction> getPossibleActions(IState state) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * sort the qValues by values
	 */
	public void sortByValues() {
		for(IState state: qValues.keySet())
		{
			Collections.sort(qValues.get(state), ValueComparator);
			// initialize table
			table.put(state, qValues.get(state).size() - 1);
		}
	}
	
	public static Comparator<IValue> ValueComparator = new Comparator<IValue>() {

	    public int compare(IValue v1, IValue v2) {
	    	
	    	double value1 = v1.getValue();
	    	double value2 = v2.getValue();

	       if(value1 > value2)
	    	   return 1;
	       else
	    	   return -1;
	       }
	    };

	public HashMap<IState, ArrayList<IValue>> getqValues() {
		return qValues;
	}

	public HashMap<IState, Integer> getTable() {
		return table;
	}

	public void setTable(HashMap<IState, Integer> table) {
		this.table = table;
	}

}

package com.mocah.mindmath.learning.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;

import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;

public class ExpertLearning extends AbstractLearning {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2431512030248522237L;
	
	//four default error types for feedback content
	private static final String[] error_list = { "1" };
	private HashMap<IState, List<Object>> error_table;
	private HashMap<IState, LinkedList<IValue>> qValues;

	public ExpertLearning(IPolicy policy) {
		this(policy, new HashMap<IState, ArrayList<IValue>>());
	}
	
	public ExpertLearning(IPolicy policy, Map<IState, ArrayList<IValue>> qValues) 
	{
		super(policy);
		this.error_table = new HashMap<IState, List<Object>>();
		HashMap<IState, LinkedList<IValue>> new_qValues = new HashMap<IState, LinkedList<IValue>>();
		//sort the qValues by values
		for(IState state: qValues.keySet())
		{
			//convert from ArrayList to LinkedList
			LinkedList<IValue> new_list = new LinkedList<IValue>();
			for(IValue value : qValues.get(state))
			{
				new_list.add(value);
			}
			
			Collections.sort(new_list, ValueComparator);
			new_qValues.put(state, new_list);
			// initialize table
			this.error_table.put(state, Arrays.asList(error_list));
		}
		this.qValues = new_qValues;
	}

	/**
	 * get not visited error type, need to be after step()
	 * @param state current state
	 * @param error asked error type
	 * @return the result of error type
	 * @throws Exception Error type not found in expert learning
	 */
	public String getErrorType(IState state, String error) throws Exception
	{
		List<Object> errors = error_table.get(state); 
		if(errors.size() == 0)
			throw new Exception("Error type not found in expert learning.");
		
		String result;
		if(errors.contains(error))
		{
			result = error;
		}
		else
		{
			result = (String) errors.get(0);
		}
		//TODO check to remove all errors table when returning the same feedback content
		errors.remove(result);
		error_table.put(state, errors);
		return result;
	}
	
	@Override
	public IAction step(IState state) {
		LinkedList<IValue> values = qValues.get(state);
		List<Object> errors = error_table.get(state); 
		//if all error type are visited, put the first value to the last and return second value
		if(errors.size() == 0)
		{
			IValue first_value = values.pollFirst();
			values.addLast(first_value);
			this.error_table.put(state, Arrays.asList(error_list));
		}
		return values.getFirst().myAction();
	}

	@Override
	public void learn(IState oldState, IAction action, double reward, IState newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IAction> getPossibleActions(IState state) {
		List<IValue> stateValues = this.qValues.get(state);

		List<IAction> actions = new ArrayList<>(stateValues.size());
		for (IValue value : stateValues) {
			actions.add(value.myAction());
		}

		return actions;
	}
	
	public static Comparator<IValue> ValueComparator = new Comparator<IValue>() {

	    public int compare(IValue v1, IValue v2) {
	    	
	    	double value1 = v1.getValue();
	    	double value2 = v2.getValue();

	       if(value1 < value2)
	    	   return 1;
	       else
	    	   return -1;
	       }
	    };

	public HashMap<IState, LinkedList<IValue>> getqValues() {
		return qValues;
	}

	public HashMap<IState, List<Object>> getErrorTable() {
		return error_table;
	}
}

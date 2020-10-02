package com.mocah.mindmath.learning.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.assertj.core.util.Arrays;

import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.actions.MindMathAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.server.entity.feedbackContent.ContentErrorType;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;

public class ExpertLearning extends AbstractLearning {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2431512030248522237L;
	
	//four different error types for feedback content
	private static final String[] error_list_differ = {"1", "2", "3", "4"};
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
			this.error_table.put(state, Arrays.asList(error_list_differ));
		}
		this.qValues = new_qValues;
	}
	
	/**
	 * check if the different error type has the same content url
	 * @param list
	 * @return
	 */
	private boolean hasDiffErrorType(LinkedList<IValue> list)
	{
		IValue value = list.getFirst();
		IAction action = value.myAction();
		String feedbackid = action.getId();
		String leaf = ((MindMathAction) action).getLeaf();
		FeedbackContent feedbackcontent = LearningProcess.getRepo().getFeedbackContent(feedbackid, leaf);
		if(feedbackcontent == null)
			return true;
		List<ContentErrorType> errorcontents = feedbackcontent.getContents();
		if(errorcontents.get(0).getContent_url().equals(errorcontents.get(1).getContent_url()))
			return false;
		return true;
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
		LinkedList<IValue> values = qValues.get(state);
		if(values.size() == 0)
			return error;
		List<Object> errors = error_table.get(state); 
		if(errors.size() == 0)
			throw new Exception("Error type not found in expert learning.");
		
		if(errors.contains(error))
		{
			errors.remove(error);
		}
		//if error doesn't exist, poll qValues and return error_list_differ without error
		else
		{
			values.pollFirst();
			errors = Arrays.asList(error_list_differ); 
			errors.remove(error);
		}
		System.out.println("[Error Types]" + errors.toString());
		error_table.put(state, errors);
		return error;
	}
	
	@Override
	public IAction step(IState state) {
		LinkedList<IValue> values = qValues.get(state);
		if(values.size() == 0)
			return null;
		IAction action = values.getFirst().myAction();
		
		//if there are same images, poll the qValues
		if(!hasDiffErrorType(qValues.get(state)))
			values.pollFirst();
		
		//always return the first qValue
		return action;
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
	       // add random in equation weight
	       if(value1 == value2)
	       {
	    	   Random random = new Random();
	    	   if(random.nextDouble() < 0.5)
	    		   return 1;
	       }
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

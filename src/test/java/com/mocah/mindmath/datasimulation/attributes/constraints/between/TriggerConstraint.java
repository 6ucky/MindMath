package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.AnswerEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TriggerEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class TriggerConstraint {
	public static final Map<TriggerEnum, Set<ErrorCodeEnum>> map = ImmutableMap.of(TriggerEnum.VALIDATE,
			EnumSet.allOf(ErrorCodeEnum.class), TriggerEnum.HELP, EnumSet.of(ErrorCodeEnum.NULL));

	public static final Map<TriggerEnum, Set<AnswerEnum>> map2 = ImmutableMap.of(TriggerEnum.VALIDATE,
			EnumSet.complementOf(EnumSet.of(AnswerEnum.NULL)), TriggerEnum.HELP, EnumSet.of(AnswerEnum.NULL));
}

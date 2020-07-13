package com.mocah.mindmath.datasimulation.attributes.constraints;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.AnswerEnum;
import com.mocah.mindmath.datasimulation.attributes.ErrorCodeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class AnswerConstraint {
	public static final Map<AnswerEnum, Set<ErrorCodeEnum>> map = ImmutableMap.of(AnswerEnum.NULL,
			EnumSet.of(ErrorCodeEnum.NULL), AnswerEnum.TRUE, EnumSet.of(ErrorCodeEnum.NULL));
}

package com.mocah.mindmath.datasimulation.attributes.constraints;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.ErrorCodeEnum;
import com.mocah.mindmath.datasimulation.attributes.FamilyTaskEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class FamilyTaskConstraint {
	public static final Map<FamilyTaskEnum, Set<ErrorCodeEnum>> map = ImmutableMap.of(FamilyTaskEnum.FT3_1,
			EnumSet.of(ErrorCodeEnum.ERR1), FamilyTaskEnum.FT8_3_1, EnumSet.of(ErrorCodeEnum.ERR2));
}

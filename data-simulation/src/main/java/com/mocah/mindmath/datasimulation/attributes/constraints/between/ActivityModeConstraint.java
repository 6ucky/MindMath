package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ActivityModeConstraint {
	public static final Map<ActivityModeEnum, Set<ErrorCodeEnum>> map = ImmutableMap.of(ActivityModeEnum.A0,
			EnumSet.of(ErrorCodeEnum.ERR2), ActivityModeEnum.A1, EnumSet.of(ErrorCodeEnum.ERR3), ActivityModeEnum.A2,
			EnumSet.allOf(ErrorCodeEnum.class));
}

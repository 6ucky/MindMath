package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DomainConstraint {
	public static final Map<DomainEnum, Set<TaskFamilyEnum>> map = ImmutableMap.of(DomainEnum.ALGEBRA,
			EnumSet.of(TaskFamilyEnum.FT3_1), DomainEnum.GEOMETRY, EnumSet.of(TaskFamilyEnum.FT8_3_1));
}

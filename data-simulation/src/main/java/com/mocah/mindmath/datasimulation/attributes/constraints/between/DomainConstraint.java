package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.FamilyTaskEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DomainConstraint {
	public static final Map<DomainEnum, Set<FamilyTaskEnum>> map = ImmutableMap.of(DomainEnum.ALGEBRA,
			EnumSet.of(FamilyTaskEnum.FT3_1), DomainEnum.GEOMETRY, EnumSet.of(FamilyTaskEnum.FT8_3_1));
}

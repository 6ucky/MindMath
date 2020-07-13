package com.mocah.mindmath.datasimulation.attributes.constraints;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mocah.mindmath.datasimulation.attributes.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.FamilyTaskEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DomainConstraint {
	public static final Map<DomainEnum, Set<FamilyTaskEnum>> map = ImmutableMap.of(DomainEnum.ALGEBRA,
			ImmutableSet.of(FamilyTaskEnum.FT3_1), DomainEnum.GEOMETRY, ImmutableSet.of(FamilyTaskEnum.FT8_3_1));
}

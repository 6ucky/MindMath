package com.mocah.mindmath.datasimulation.attributes.constraints;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.FamilyTaskEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DomainConstraint {
	public static final Map<DomainEnum, Set<FamilyTaskEnum>> map = ImmutableMap.of(DomainEnum.ALGEBRA,
			EnumSet.of(FamilyTaskEnum.FT3_1), DomainEnum.GEOMETRY, EnumSet.of(FamilyTaskEnum.FT8_3_1));
}

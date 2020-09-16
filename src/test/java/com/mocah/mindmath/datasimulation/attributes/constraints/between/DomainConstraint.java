package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.GeneratorEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DomainConstraint {
	public static final Map<DomainEnum, Set<GeneratorEnum>> map = ImmutableMap.of(DomainEnum.ALGEBRA,
			EnumSet.of(GeneratorEnum.RES_EQ_PERMIER_DEGRE), DomainEnum.GEOMETRY, EnumSet.noneOf(GeneratorEnum.class));
}

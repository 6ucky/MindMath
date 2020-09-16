package com.mocah.mindmath.datasimulation.attributes.constraints.between;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.GeneratorEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class GeneratorConstraint {
	public static final Map<GeneratorEnum, Set<TaskFamilyEnum>> map = ImmutableMap.of(
			GeneratorEnum.RES_EQ_PERMIER_DEGRE,
			EnumSet.of(TaskFamilyEnum.FT3_1, TaskFamilyEnum.FT3_2_1, TaskFamilyEnum.FT3_2_2, TaskFamilyEnum.FT3_2_3,
					TaskFamilyEnum.FT6_1_1, TaskFamilyEnum.FT6_1_2, TaskFamilyEnum.FT6_1_3, TaskFamilyEnum.FT6_2_1,
					TaskFamilyEnum.FT6_2_2, TaskFamilyEnum.FT6_2, TaskFamilyEnum.FT7_1, TaskFamilyEnum.FT7_2,
					TaskFamilyEnum.FT7_3, TaskFamilyEnum.FT8_1, TaskFamilyEnum.FT8_2, TaskFamilyEnum.FT8_3_1,
					TaskFamilyEnum.FT8_3_2, TaskFamilyEnum.FT8_3_3, TaskFamilyEnum.FT9_1, TaskFamilyEnum.FT10_1));

	public static final Map<GeneratorEnum, Set<ErrorCodeEnum>> map2 = ImmutableMap.of(
			GeneratorEnum.RES_EQ_PERMIER_DEGRE,
			EnumSet.of(ErrorCodeEnum.NULL, ErrorCodeEnum.CONFUSION_RATIONNEL, ErrorCodeEnum.NON_GESTION_OPPOSE,
					ErrorCodeEnum.INV_NUM_DEN, ErrorCodeEnum.CONCAT, ErrorCodeEnum.OPPOSE));

}

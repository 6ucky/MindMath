/**
 *
 */
package com.mocah.mindmath.datasimulation.dataexports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

import com.google.common.base.Strings;
import com.mocah.mindmath.datasimulation.AppConfig;
import com.mocah.mindmath.datasimulation.FeedbackData;
import com.mocah.mindmath.datasimulation.json.SimulatedData;
import com.mocah.mindmath.datasimulation.json.SimulatedDataContainer;
import com.mocah.mindmath.datasimulation.json.SimulatedDataContainerIterator;
import com.mocah.mindmath.datasimulation.json.SimulatedDataLearner;
import com.mocah.mindmath.datasimulation.profiles.IProfile;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class GraphExporter extends AbstractExporter {

	public GraphExporter(SimulatedDataContainer toExport) {
		super(toExport);
	}

	@Override
	public void export() {
		Iterator<SimulatedDataLearner> it = new SimulatedDataContainerIterator(toExport);

		// Global graph
		int globalI = 0;
		double globalCumReward = 0.0;
		List<Integer> xGlobalIteration = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Double> xGlobalIterationShift = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Double> yGlobalReward = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Double> yGlobalCumReward = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Integer> yGlobalActivityMode = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Integer> yGlobalFeedbackWeight = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Double> yGlobalSuccessProb = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);
		List<Double> yGlobalIncreaseProb = new ArrayList<>(toExport.getDatasets().size() * AppConfig.MAX_ITERATION);

		// State evolution graph
		Map<String, List<Integer>> xStateIterations = new HashMap<>();
		Map<String, Map<String, List<Double>>> yStateValues = new HashMap<>();

		// Per profile graph
		Map<IProfile, Integer> profileI = new HashMap<>();
		Map<IProfile, Double> profileCumReward = new HashMap<>();
		Map<IProfile, List<Integer>> xProfileIteration = new HashMap<>();
		Map<IProfile, List<Double>> xProfileIterationShift = new HashMap<>();
		Map<IProfile, List<Double>> yProfileReward = new HashMap<>();
		Map<IProfile, List<Double>> yProfileCumReward = new HashMap<>();
		Map<IProfile, List<Integer>> yProfileActivityMode = new HashMap<>();
		Map<IProfile, List<Integer>> yProfileFeedbackWeight = new HashMap<>();
		Map<IProfile, List<Double>> yProfileSuccessProb = new HashMap<>();
		Map<IProfile, List<Double>> yProfileIncreaseProb = new HashMap<>();
		while (it.hasNext()) {
			SimulatedDataLearner learner = it.next();
			IProfile lProfile = learner.getProfile();

			// Per Learner graph
			List<Integer> xLearnerIteration = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Double> xLearnerIterationShift = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Double> yLearnerReward = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Double> yLearnerCumReward = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Integer> yLearnerActivityMode = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Integer> yLearnerFeedbackWeight = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Double> yLearnerSuccessProb = new ArrayList<>(AppConfig.MAX_ITERATION);
			List<Double> yLearnerIncreaseProb = new ArrayList<>(AppConfig.MAX_ITERATION);

			// Init profile if first profile encounter
			profileI.putIfAbsent(lProfile, 0);
			profileCumReward.putIfAbsent(lProfile, 0.0);
			xProfileIteration.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			xProfileIterationShift.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileReward.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileCumReward.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileActivityMode.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileFeedbackWeight.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileSuccessProb.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));
			yProfileIncreaseProb.putIfAbsent(lProfile, new ArrayList<>(AppConfig.MAX_ITERATION));

			int learnerI = 0;
			double learnerCumReward = 0.0;
			for (SimulatedData learnerData : learner.getDataset()) {
				// Populate X axis (iteration and shifted iterations)
				xLearnerIteration.add(learnerI);
				xLearnerIterationShift.add(learnerI + 0.5);
				xProfileIteration.get(lProfile).add(profileI.get(lProfile));
				xProfileIterationShift.get(lProfile).add(profileI.get(lProfile) + 0.5);
				xGlobalIteration.add(globalI);
				xGlobalIterationShift.add(globalI + 0.5);

				// Populate learner reward
				yLearnerReward.add(0.0);
				yProfileReward.get(lProfile).add(0.0);
				yGlobalReward.add(0.0);
				FeedbackData f = learnerData.getFeedback();
				if (f != null) {
					Double r = f.getReward();
					if (r == null) {
						r = 0.0;
					}
					yLearnerReward.set(learnerI, r.doubleValue());
					yProfileReward.get(lProfile).set(profileI.get(lProfile), r.doubleValue());
					yGlobalReward.set(globalI, r.doubleValue());

					// Populate state's graph data
					String state = f.getModifiedState();
					if (!Strings.isNullOrEmpty(state)) {
						Map<String, Double> qvalues = f.getQvalues();

						if (!xStateIterations.containsKey(state)) {
							// Init state if first state encounter
							xStateIterations.put(state, new ArrayList<>());
							yStateValues.put(state, new HashMap<>());
						}

						if (qvalues.size() > 0) {
							xStateIterations.get(state).add(globalI);

							for (String action : qvalues.keySet()) {
								if (!yStateValues.get(state).containsKey(action)) {
									// Init action if first action encounter
									yStateValues.get(state).put(action, new ArrayList<>());
								}

								yStateValues.get(state).get(action).add(qvalues.get(action));
							}
						}
					}
				}

				// Populate learner cumulative reward
				yLearnerCumReward.add(learnerCumReward + yLearnerReward.get(learnerI));
				yProfileCumReward.get(lProfile)
						.add(profileCumReward.get(lProfile) + yProfileReward.get(lProfile).get(profileI.get(lProfile)));
				yGlobalCumReward.add(globalCumReward + yGlobalReward.get(globalI));

				// Update cumulative reward for next iteration
				learnerCumReward = yLearnerCumReward.get(learnerI);
				profileCumReward.put(lProfile, yProfileCumReward.get(lProfile).get(profileI.get(lProfile)));
				globalCumReward = yGlobalCumReward.get(globalI);

				// Populate activity mode
				Integer am = learnerData.getGenerated().getActivityMode().getValue();
				yLearnerActivityMode.add(am);
				yProfileActivityMode.get(lProfile).add(am);
				yGlobalActivityMode.add(am);

				// Populate feedback weight
				FeedbackData fdb = learnerData.getFeedback();
				Integer fdbw = -1;
				if (fdb != null) {
					fdbw = AppConfig.getWeightInfo(learnerData.getFeedback().getIdFeedback());
				}
				yLearnerFeedbackWeight.add(fdbw);
				yProfileFeedbackWeight.get(lProfile).add(fdbw);
				yGlobalFeedbackWeight.add(fdbw);

				// Populate exercise success probability
				double sp = learnerData.getExerciseSuccessProb();
				yLearnerSuccessProb.add(sp);
				yProfileSuccessProb.get(lProfile).add(sp);
				yGlobalSuccessProb.add(sp);

				// Populate activity mode increasing probability
				double ip = learnerData.getActivityModeIncreaseSuccessProb();
				yLearnerIncreaseProb.add(ip);
				yProfileIncreaseProb.get(lProfile).add(sp);
				yGlobalIncreaseProb.add(ip);

				learnerI += 1;
				globalI += 1;
				profileI.put(lProfile, profileI.get(lProfile) + 1);
			}

			String learnerChartsTitle = lProfile.getClass().getSimpleName() + ": " + learner.getLearnerId();
			@SuppressWarnings("rawtypes")
			List<Chart> learnerCharts = generateChartsFor(learnerChartsTitle, xLearnerIteration, xLearnerIterationShift,
					yLearnerActivityMode, yLearnerIncreaseProb, yLearnerFeedbackWeight, yLearnerSuccessProb,
					yLearnerReward, yLearnerCumReward);

			saveCharts(new File(exportPath + learner.getLearnerId() + "_Graph"), learnerCharts, 3, 1);
		}

		String globalChartsTitle = "All learners: all profiles";
		@SuppressWarnings("rawtypes")
		List<Chart> globalCharts = generateChartsFor(globalChartsTitle, xGlobalIteration, xGlobalIterationShift,
				yGlobalActivityMode, yGlobalIncreaseProb, yGlobalFeedbackWeight, yGlobalSuccessProb, yGlobalReward,
				yGlobalCumReward);

		saveCharts(new File(exportPath + "Global_Graph"), globalCharts, 3, 1);

		for (IProfile profile : profileI.keySet()) {

			String profileChartsTitle = "All learners: " + profile.getClass().getSimpleName();
			@SuppressWarnings("rawtypes")
			List<Chart> profileCharts = generateChartsFor(profileChartsTitle, xProfileIteration.get(profile),
					xProfileIterationShift.get(profile), yProfileActivityMode.get(profile),
					yProfileIncreaseProb.get(profile), yProfileFeedbackWeight.get(profile),
					yProfileSuccessProb.get(profile), yProfileReward.get(profile), yProfileCumReward.get(profile));

			saveCharts(new File(exportPath + profile.getClass().getSimpleName() + "_Graph"), profileCharts, 3, 1);
		}

		String stateGraphExportPath = exportPath + "states" + File.separator;
		for (String state : xStateIterations.keySet()) {
			// Create Chart
			CategoryChart stateChart = new CategoryChartBuilder().width(800).height(600).title(state)
					.xAxisTitle("Iteration").yAxisTitle("Qvalue").build();

			// Customize Chart
			stateChart.getStyler().setAvailableSpaceFill(.9);
			stateChart.getStyler().setOverlapped(false);

			// Series
			Map<String, List<Double>> qvalues = yStateValues.get(state);
			for (String action : qvalues.keySet()) {
				stateChart.addSeries(action, xStateIterations.get(state), qvalues.get(action));
			}

			saveChart(new File(stateGraphExportPath + state.hashCode() + "_Graph"), stateChart);
		}
	}

	private XYChart buildChart(String title, String xAxisTitle, String yAxisTitle) {
		XYChart chart = new XYChartBuilder().width(800).height(600).title(title).xAxisTitle(xAxisTitle)
				.yAxisTitle(yAxisTitle).build();

		return chart;
	}

	@SuppressWarnings("rawtypes")
	private List<Chart> generateChartsFor(String chatstitle, List<? extends Number> xIterations,
			List<? extends Number> xIterationsShift, List<? extends Number> yActivityMode,
			List<? extends Number> yIncreaseProb, List<? extends Number> yFeedbackWeight,
			List<? extends Number> ySuccessProb, List<? extends Number> yReward, List<? extends Number> yCumReward) {

		XYChart learnerDataChart = buildChart(chatstitle + " - Learner level over time", "Iterations", "");
		XYSeries ldcs1 = learnerDataChart.addSeries("Learner level", xIterations, yActivityMode);
		ldcs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Step);
		ldcs1.setYAxisGroup(0);
		learnerDataChart.setYAxisGroupTitle(0, "Learner level");
		((AxesChartStyler) learnerDataChart.getStyler()).setYAxisMin(0, 0.0);
		((AxesChartStyler) learnerDataChart.getStyler()).setYAxisMax(0, 2.0);
		XYSeries ldcs2 = learnerDataChart.addSeries("Level increase probability", xIterations, yIncreaseProb);
		ldcs2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
		ldcs2.setYAxisGroup(1);
		learnerDataChart.setYAxisGroupTitle(1, "Probability");
		// Be sure that second axis will be drawn on the right
		learnerDataChart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
		((AxesChartStyler) learnerDataChart.getStyler()).setYAxisMin(1, 0.0);
		((AxesChartStyler) learnerDataChart.getStyler()).setYAxisMax(1, 1.0);

		XYChart learnerExerciseChart = buildChart("Success and feedback weight over time", "Iterations", "");
		XYSeries lecs1 = learnerExerciseChart.addSeries("Feedback weight", xIterationsShift, yFeedbackWeight);
		lecs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		lecs1.setYAxisGroup(0);
		learnerExerciseChart.setYAxisGroupTitle(0, "Feedback weight");
		((AxesChartStyler) learnerExerciseChart.getStyler()).setYAxisMin(0, 0.0);
		((AxesChartStyler) learnerExerciseChart.getStyler()).setYAxisMax(0, 4.0);
		XYSeries lecs2 = learnerExerciseChart.addSeries("Success probability", xIterations, ySuccessProb);
		lecs2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
		lecs2.setYAxisGroup(1);
		learnerExerciseChart.setYAxisGroupTitle(1, "Probability");
		// Be sure that second axis will be drawn on the right
		learnerExerciseChart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
		((AxesChartStyler) learnerExerciseChart.getStyler()).setYAxisMin(1, 0.0);
		((AxesChartStyler) learnerExerciseChart.getStyler()).setYAxisMax(1, 1.0);

		XYChart learnerResChart = buildChart("Reward and cumulative reward over time", "Iterations", "Rewards");
		XYSeries lrcs1 = learnerResChart.addSeries("Reward", xIterationsShift, yReward);
		XYSeries lrcs2 = learnerResChart.addSeries("Cumulative Reward", xIterationsShift, yCumReward);

		List<Chart> learnerCharts = new ArrayList<>();
		learnerCharts.add(learnerDataChart);
		learnerCharts.add(learnerExerciseChart);
		learnerCharts.add(learnerResChart);

		return learnerCharts;
	}

	private void saveChart(File f, Chart<? extends Styler, ? extends Series> chart) {
		try {
			createPath(f);
			BitmapEncoder.saveBitmapWithDPI(chart, f.getCanonicalPath(), BitmapFormat.PNG, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveCharts(File f, @SuppressWarnings("rawtypes") List<Chart> charts, int rows, int cols) {
		try {
			createPath(f);
			BitmapEncoder.saveBitmap(charts, rows, cols, f.getCanonicalPath(), BitmapFormat.PNG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

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
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

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

		// Per profile graph
		Map<IProfile, Integer> profileI = new HashMap<>();
		Map<IProfile, Double> profileCumReward = new HashMap<>();
		Map<IProfile, List<Integer>> xProfileIteration = new HashMap<>();
		Map<IProfile, List<Double>> xProfileIterationShift = new HashMap<>();
		Map<IProfile, List<Double>> yProfileReward = new HashMap<>();
		Map<IProfile, List<Double>> yProfileCumReward = new HashMap<>();
		Map<IProfile, List<Integer>> yProfileActivityMode = new HashMap<>();
		Map<IProfile, List<Integer>> yProfileFeedbackWeight = new HashMap<>();
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
					if (r != null) {
						yLearnerReward.set(learnerI, r.doubleValue());
						yProfileReward.get(lProfile).set(profileI.get(lProfile), r.doubleValue());
						yGlobalReward.set(globalI, r.doubleValue());
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
				Integer fdbw = AppConfig.getWeightInfo(learnerData.getFeedback().getIdFeedback());
				yLearnerFeedbackWeight.add(fdbw);
				yProfileFeedbackWeight.get(lProfile).add(fdbw);
				yGlobalFeedbackWeight.add(fdbw);

				// Populate exercise success probability
				double sp = learnerData.getExerciseSuccessProb();
				yLearnerSuccessProb.add(sp);

				// Populate activity mode increasing probability
				double ip = learnerData.getActivityModeIncreaseSuccessProb();
				yLearnerIncreaseProb.add(ip);

				learnerI += 1;
				globalI += 1;
				profileI.put(lProfile, profileI.get(lProfile) + 1);
			}

			XYChart learnerDataChart = buildChart(
					lProfile.getClass().getSimpleName() + ": " + learner.getLearnerId() + "\nLearner level over time",
					"Iterations", "");
			XYSeries ldcs1 = learnerDataChart.addSeries("Learner level", xLearnerIteration, yLearnerActivityMode);
			ldcs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Step);
			ldcs1.setYAxisGroup(0);
			learnerDataChart.setYAxisGroupTitle(0, "Learner level");
			XYSeries ldcs2 = learnerDataChart.addSeries("Level increase probability", xLearnerIteration,
					yLearnerIncreaseProb);
			ldcs2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);
			ldcs2.setYAxisGroup(1);
			learnerDataChart.setYAxisGroupTitle(1, "Probability");
			// Be sure that second axis will be drawn on the right
			learnerDataChart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

			XYChart learnerExerciseChart = buildChart("Success and feedback weight over time", "Iterations", "");
			XYSeries lecs1 = learnerExerciseChart.addSeries("Feedback weight", xLearnerIterationShift,
					yLearnerFeedbackWeight);
			lecs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
			lecs1.setYAxisGroup(0);
			learnerExerciseChart.setYAxisGroupTitle(0, "Feedback weight");
			XYSeries lecs2 = learnerExerciseChart.addSeries("Success probability", xLearnerIteration,
					yLearnerSuccessProb);
			lecs2.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);
			lecs2.setYAxisGroup(1);
			learnerExerciseChart.setYAxisGroupTitle(1, "Probability");
			// Be sure that second axis will be drawn on the right
			learnerExerciseChart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

			XYChart learnerResChart = buildChart("Reward and cumulative reward over time", "Iterations", "Rewards");
			XYSeries lrcs1 = learnerResChart.addSeries("Reward", xLearnerIterationShift, yLearnerReward);
			XYSeries lrcs2 = learnerResChart.addSeries("Cumulative Reward", xLearnerIterationShift, yLearnerCumReward);

			@SuppressWarnings("rawtypes")
			List<Chart> learnerCharts = new ArrayList<>();
			learnerCharts.add(learnerDataChart);
			learnerCharts.add(learnerExerciseChart);
			learnerCharts.add(learnerResChart);

			saveCharts(new File(exportPath + learner.getLearnerId() + "_Graph"), learnerCharts, 3, 1);
		}

		XYChart globalDataChart = buildChart("All learners: all profiles" + "\nActivity mode over time", "Iterations",
				"");
		XYSeries gdcs1 = globalDataChart.addSeries("Activity mode", xGlobalIteration, yGlobalActivityMode);
		gdcs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Step);
		XYSeries gdcs2 = globalDataChart.addSeries("Feedback weight", xGlobalIteration, yGlobalFeedbackWeight);

		XYChart globalResChart = buildChart("Reward and cumulative reward over time", "Iterations", "Reward");
		XYSeries grcs1 = globalResChart.addSeries("Reward", xGlobalIteration, yGlobalReward);
		XYSeries grcs2 = globalResChart.addSeries("Cumulative Reward", xGlobalIteration, yGlobalCumReward);

		@SuppressWarnings("rawtypes")
		List<Chart> globalCharts = new ArrayList<>();
		globalCharts.add(globalDataChart);
		globalCharts.add(globalResChart);

		saveCharts(new File(exportPath + "Global_Graph"), globalCharts, 2, 1);
//		saveChart(new File(exportPath + "Global_Graph"), globalResChart);

		for (IProfile profile : profileI.keySet()) {
			XYChart profileDataChart = buildChart(
					"All learners: " + profile.getClass().getSimpleName() + "\nActivity mode over time", "Iterations",
					"");
			XYSeries pdcs1 = profileDataChart.addSeries("Activity mode", xGlobalIteration, yGlobalActivityMode);
			pdcs1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Step);
			XYSeries pdcs2 = profileDataChart.addSeries("Feedback weight", xGlobalIteration, yGlobalFeedbackWeight);

			XYChart profileResChart = buildChart("Reward and cumulative reward over time", "Iterations", "Reward");
			XYSeries prcs1 = profileResChart.addSeries("Reward", xProfileIteration.get(profile),
					yProfileReward.get(profile));
			XYSeries prcs2 = profileResChart.addSeries("Cumulative Reward", xProfileIteration.get(profile),
					yProfileCumReward.get(profile));

			@SuppressWarnings("rawtypes")
			List<Chart> profileCharts = new ArrayList<>();
			profileCharts.add(profileDataChart);
			profileCharts.add(profileResChart);

			saveCharts(new File(exportPath + profile.getClass().getSimpleName() + "_Graph"), profileCharts, 2, 1);
//			saveChart(new File(exportPath + profile.getClass().getSimpleName() + "_Graph"), profileResChart);
		}
	}

	private XYChart buildChart(String title, String xAxisTitle, String yAxisTitle) {
		XYChart chart = new XYChartBuilder().width(800).height(600).title(title).xAxisTitle(xAxisTitle)
				.yAxisTitle(yAxisTitle).build();

		return chart;
	}

	private void saveChart(File f, Chart<? extends Styler, ? extends Series> chart) {
		try {
			BitmapEncoder.saveBitmapWithDPI(chart, f.getCanonicalPath(), BitmapFormat.PNG, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveCharts(File f, @SuppressWarnings("rawtypes") List<Chart> charts, int rows, int cols) {
		try {
			BitmapEncoder.saveBitmap(charts, rows, cols, f.getCanonicalPath(), BitmapFormat.PNG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

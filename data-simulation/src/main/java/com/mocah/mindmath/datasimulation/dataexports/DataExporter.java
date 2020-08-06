/**
 *
 */
package com.mocah.mindmath.datasimulation.dataexports;

import java.io.File;
import java.util.Iterator;

import com.google.gson.Gson;
import com.mocah.mindmath.datasimulation.AppConfig;
import com.mocah.mindmath.datasimulation.json.SimulatedDataContainer;
import com.mocah.mindmath.datasimulation.json.SimulatedDataLearner;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DataExporter extends AbstractExporter {

	public DataExporter(SimulatedDataContainer toExport) {
		super(toExport);
	}

	@Override
	public void export() {
		Gson gson = AppConfig.getGson();

		String content = gson.toJson(toExport);
		File datasets = new File(exportPath + "datasets.json");
		writeInFile(datasets, content);

		File finalQtable = new File(exportPath + "finalQtable.csv");
		writeInFile(finalQtable, toExport.getFinalCSV());

		Iterator<SimulatedDataLearner> it = toExport.getDatasets().values().iterator();
		while (it.hasNext()) {
			SimulatedDataLearner simulatedDataLearner = it.next();

			File qtable = new File(exportPath + simulatedDataLearner.getLearnerId() + "_Qtable.csv");
			writeInFile(qtable, simulatedDataLearner.getLearnerCSV());
		}
	}
}

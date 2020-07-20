/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.google.gson.Gson;
import com.mocah.mindmath.datasimulation.Config;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class DataExporter {
	private SimulatedDataContainer toExport;

	public DataExporter(SimulatedDataContainer toExport) {
		this.toExport = toExport;
	}

	public void export() {
		Gson gson = Config.getGson();

		String pathStr = "simulated" + File.separator + toExport.getDate().getTime() + File.separator;

		String content = gson.toJson(toExport);
		File datasets = new File(pathStr + "datasets.json");
		writeInFile(datasets, content);

		File finalQtable = new File(pathStr + "finalQtable.xml");
		writeInFile(finalQtable, toExport.getFinalCSV());

		Iterator<SimulatedDataLearner> it = toExport.getDatasets().values().iterator();
		while (it.hasNext()) {
			SimulatedDataLearner simulatedDataLearner = it.next();

			File qtable = new File(pathStr + simulatedDataLearner.getLearnerId() + "_Qtable.xml");
			writeInFile(qtable, simulatedDataLearner.getLearnerCSV());
		}
	}

	private void writeInFile(File f, String content) {
		BufferedWriter writer = null;
		try {
			if (f.getParentFile() != null) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();

			writer = new BufferedWriter(new FileWriter(f));
			writer.write(content);
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
			}
		}
	}
}

/**
 *
 */
package com.mocah.mindmath.datasimulation.dataexports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mocah.mindmath.datasimulation.json.SimulatedDataContainer;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractExporter {
	protected SimulatedDataContainer toExport;
	protected String exportPath;

	public AbstractExporter(SimulatedDataContainer toExport) {
		this.toExport = toExport;
		this.exportPath = "simulated" + File.separator + toExport.getDate().getTime() + File.separator;
	}

	public abstract void export();

	protected void writeInFile(File f, String content) {
		if (content == null) {
			content = "";
		}

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

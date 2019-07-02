package ru.droidwelt.prototype8.msa.sqlite;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.GraphicUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;


public class MsaImportSQLite extends AsyncTask < URL, Integer, Long > {

	public String MSA_FILETYPE="";
	public String MSA_FILENAME="";
	public String MSA_FULLFILENAME="";
	private int  bytesRead = 0;
		
	@Override
	protected Long doInBackground(URL... params) {
		publishProgress(0);
		byte[] resall = null;
		int btR;
		try {
			InputStream stream = new FileInputStream(MSA_FULLFILENAME);
			try {
				byte[] buffer = new byte [1024 * 100];
				while ((btR = stream.read(buffer)) > 0) {
					resall = new GraphicUtils().concatArray(resall, buffer);
					bytesRead = bytesRead + btR;
				}

			} catch (IOException ignored) {
			}
			try {
				stream.close();
			} catch (IOException ignored) {
			}

			ContentValues editMessage = new ContentValues();
			editMessage.put("MSA_IMAGE", resall);
			Appl.getDatabase().update("MSA", editMessage, "MSA_ID='" + Appl.MSA_ID + "'", null);
		} catch (FileNotFoundException ignored) {
		}

		return (long) bytesRead;
	}
	
	

	protected void onPostExecute(Long result) {
		try {
            Events.EventsMessage ev = new Events.EventsMessage();
            ev.setMes_code(Events.EB_MSAEDIT_INPORT_FILE);
            ev.setMes_s1(MSA_FILENAME);
            ev.setMes_s2(MSA_FILETYPE);
            ev.setMes_int(bytesRead);
            GlobalBus.getBus().post(ev);
		} catch (Exception ignored) {
		}
	}


}

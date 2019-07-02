

package ru.droidwelt.prototype8.msa.sqlite;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.FileUtils;


public class MsaExportSQLite extends AsyncTask<URL, Integer, Long> {

    public String sFileName = "";
    public String sFileType ="";


    @Override
    protected Long doInBackground(URL... params) {
        File f = new File(Appl.DOWNLOAD_PATH, sFileName+'.'+sFileType);
        try {
            if (!f.createNewFile())
                return null;
        } catch (IOException ignored) {
        }

        byte[] resall = new MsaUtilsSQLite().getByteArrayFromMsaImaget(Appl.MSA_ID);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException ignored) {

        }
        try {
            assert resall != null;
            if (fos != null) {
                fos.write(resall);
                fos.flush();
                fos.close();
            }
        } catch (IOException ignored) {
        }
        return null;
    }


    protected void onPostExecute(Long result) {
        try {
            new FileUtils().startIntentFromFile(sFileName+'.'+sFileType);
        } catch (Exception ignored) {
        }

    }

}

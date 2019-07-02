package ru.droidwelt.prototype8.msa;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.ebus.Events;
import ru.droidwelt.prototype8.utils.ebus.GlobalBus;


public class MsaReduceImageActivity extends AppCompatActivity {

	private Bitmap  myimageBig ; // = MsaEditActivity.imageBig;
	private int h2;
	private int w2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme_Dialog);
		setContentView(R.layout.activity_reduceimage);
		setTitle(getString(R.string.header_reduce));
		TextView tv_info_before = findViewById(R.id.textView_reduce1);
		TextView tv_info_after = findViewById(R.id.textView_reduce3);

        try {
            InputStream is = new FileInputStream(new File(Appl.TEMP_PATH, "ImageToReduce.jpg"));
            myimageBig = BitmapFactory.decodeStream( is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int w = myimageBig.getWidth();
		int h = myimageBig.getHeight();
		int b = myimageBig.getByteCount() / 1024;
		int j;
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			myimageBig.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
			j = bytes.size() / 1024;
		}
		String s = "Размер по X - "+ w +"\n"+
		           "Размер по Y - "+ h +"\n"+
		           "Занимает памяти, кб - "+ b +"\n"+
		           "Размер в сообщении, кб - "+ j +"\n";
		tv_info_before.setText(s);
						
		h2 = h / 2;
		w2 = w / 2;
		int b2;
		int j2;
		{
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(myimageBig, w2,h2, false);
			b2 = resizedBitmap.getByteCount() / 1024;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
			j2 = bytes.size() / 1024;
		}
		String s2 = "Размер по X - " + w2 + "\n" + "Размер по Y - " + h2 + "\n"
				+ "Занимает памяти, кб - " + b2 + "\n"
				+ "Размер в сообщении, кб - " + j2 + "\n";
		tv_info_after.setText(s2);
			
		Button closeButton = findViewById(R.id.button_reduce_close);
		closeButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		        finish();
		    }
		});
		
		Button reduceButton = findViewById(R.id.button_reduce_reduceimage);
		reduceButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
				Events.EventsMessage ev = new Events.EventsMessage();
				ev.setMes_code(Events.EB_MSAEDIT_REDUCE_IMAGE);
				ev.setMes_x (w2);
				ev.setMes_y (h2);
				GlobalBus.getBus().post(ev);
		    	finish();
		    }
		});
		
		if ((w2 < 300) | (h2 < 200) | (j2 < 10)) {
			TextView tv_qwe = findViewById(R.id.textView_reduce_qwe);
			tv_qwe.setText(getText(R.string.s_picture_reduce_dsb));
			reduceButton.setEnabled(false);
		}
		
	}
}

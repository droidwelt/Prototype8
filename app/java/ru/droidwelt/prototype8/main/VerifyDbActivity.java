package ru.droidwelt.prototype8.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;


public class VerifyDbActivity extends AppCompatActivity {
	
	int  kvo_0=0,  kvo_2=0,  kvo_3 =0,  kvo_4=0, sumdeleted = 0;
	int  old=10;
	public  Button bt_vfy_0,bt_vfy_2,bt_vfy_3,bt_vfy_4;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme_Dialog);
		setContentView(R.layout.activity_verifydb);
		setTitle(getString(R.string.s_message_verifydb));
		old = new PrefUtils().getDaysDownLoad();
		if (old<=0 )  old=10;
	        
		bt_vfy_0 = findViewById(R.id.bt_vfy_0);
		bt_vfy_2 = findViewById(R.id.bt_vfy_2);
		bt_vfy_3 = findViewById(R.id.bt_vfy_3);
		bt_vfy_4 = findViewById(R.id.bt_vfy_4);
		bt_vfy_0.setOnClickListener(onClickButtonListtiner);
		bt_vfy_2.setOnClickListener(onClickButtonListtiner);
		bt_vfy_3.setOnClickListener(onClickButtonListtiner);
		bt_vfy_4.setOnClickListener(onClickButtonListtiner);
		verifyOldMessages ();
	}

	OnClickListener onClickButtonListtiner = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {

			case R.id.bt_vfy_0:
				compressSQLiteDatabase(0);
				break;
				
			case R.id.bt_vfy_2:
				compressSQLiteDatabase(2);
				break;
				
			case R.id.bt_vfy_3:
				compressSQLiteDatabase(3);
				break;
				
			case R.id.bt_vfy_4:
				compressSQLiteDatabase(4);
				break;

			default:
				break;
			}
		}
	};

	private  void compressSQLiteDatabase(int mode) {
        deleteRecords (mode);
        verifyOldMessages ();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Appl.getDatabase().execSQL("VACUUM;");
			}
		}).start();
	}


    public  void deleteRecords (int mode) {
		if (mode==0) sumdeleted=sumdeleted+kvo_0;
		if (mode==2) sumdeleted=sumdeleted+kvo_2;
		if (mode==3) sumdeleted=sumdeleted+kvo_3;
		if (mode==4) sumdeleted=sumdeleted+kvo_4;
		String sSQL = 
				"delete from MSA where MSA_STATE="+mode+" and MSA_DATE<date('now','-"+old+" day')";
		Appl.getDatabase().execSQL(sSQL);
	}
		

	
	public  void verifyOldMessages () {
        	String sSQL =
				" select " +
				" (select count(*) from MSA where MSA_STATE=0 and MSA_DATE<date('now','-"+old+" day')) as KVO_0,"+
				" (select count(*) from MSA where MSA_STATE=2 and MSA_DATE<date('now','-"+old+" day')) as KVO_2,"+
				" (select count(*) from MSA where MSA_STATE=3 and MSA_DATE<date('now','-"+old+" day')) as KVO_3,"+
				" (select count(*) from MSA where MSA_STATE=4 and MSA_DATE<date('now','-"+old+" day')) as KVO_4 ";
		Cursor c = Appl.getDatabase().rawQuery(sSQL, null);
		c.moveToFirst();
		kvo_0 = c.getInt(c.getColumnIndex("KVO_0"));
		kvo_2 = c.getInt(c.getColumnIndex("KVO_2"));
		kvo_3 = c.getInt(c.getColumnIndex("KVO_3"));
		kvo_4 = c.getInt(c.getColumnIndex("KVO_4"));		
		c.close();
		bt_vfy_0.setEnabled(kvo_0>0);
		bt_vfy_2.setEnabled(kvo_2>0);
		bt_vfy_3.setEnabled(kvo_3>0);
		bt_vfy_4.setEnabled(kvo_4>0);

		if  (kvo_0<=0) {
			bt_vfy_0.setText("черновики");
		}  else {
            String s="черновики "+kvo_0+" шт";
			bt_vfy_0.setText(s);
		}
		if  (kvo_2<=0) {
			bt_vfy_2.setText("входящие");
		}  else {
            String s="входящие "+kvo_2+" шт";
			bt_vfy_2.setText(s);
		}
		if  (kvo_3<=0) {
			bt_vfy_3.setText("отправленные");
		}  else {
            String s="отправленные "+kvo_3+" шт";
			bt_vfy_3.setText(s);
		}
		if  (kvo_4<=0) {
			bt_vfy_4.setText("исходящие");
		}  else {
            String s="исходящие "+kvo_4+" шт";
			bt_vfy_4.setText(s);
		}
	}
	
		
  /*  0  - черновик
    2 -  входящие
    3 -  отправленные
    4 -  исходящие */
    
}

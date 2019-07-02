package ru.droidwelt.prototype8.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.contactinfo.ContactInfoDataClass;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.NetworkUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.login.LoginActivity;
import ru.droidwelt.prototype8.utils.login.LoginLoader;

public class GalleryActivity extends AppCompatActivity {


    //  private static final String TAG = GalleryActivity.class.getSimpleName();
    private int PAGE_COUNT = 0;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    private ArrayList<GalleryIdDataClass> gsList;
    ProgressBar galpb;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTheme(R.style.AppTheme);
        setTitle(R.string.header_gallery);

        galpb = findViewById(R.id.gal_pb);

        GalleryIdLoader galleryCountLoader = new GalleryIdLoader();
        galleryCountLoader.getGalleryCount(this);

        pager = findViewById(R.id.gal_pager);
        fm = getSupportFragmentManager();
        pagerAdapter = new MyFragmentPagerAdapter(fm);
        pager.setAdapter(pagerAdapter);

        if (new PrefUtils().getGalleryEffect())
            pager.setPageTransformer(true, new ZoomOutPageTransformer());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //  Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mi_gal_download:
                if (new NetworkUtils().checkConnection(true)) {
                    GalleryIdLoader galleryCountLoader = new GalleryIdLoader();
                    galleryCountLoader.getGalleryCount(this);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }

    public void needLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, Appl.EXIT_CODE_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Appl.EXIT_CODE_LOGIN) {
            if (!(intent == null)) {
                String code = intent.getStringExtra("code");
                new LoginLoader().loginUser(this, code);
            }
        }
    }


    public ArrayList<GalleryIdDataClass> getGsList() {
        return gsList;
    }

    public void setGsList(ArrayList<GalleryIdDataClass> gsList) {
        this.gsList = gsList;
        this.PAGE_COUNT = this.gsList.size();
        this.pagerAdapter.notifyDataSetChanged();
        this.galpb.setMax(this.PAGE_COUNT + 1);
    }


    public void isReadyContactInfoData(String frTag, ContactInfoDataClass cic) {
        GalleryFragment fr = (GalleryFragment) fm.findFragmentByTag(frTag);
        if ((fr != null) && (fr.isVisible())) {
            fr.isReadyContactInfoData(cic);
        }
    }

    public void isReadyContactInfoImageData(String frTag, ContactInfoDataClass cic) {
        GalleryFragment fr = (GalleryFragment) fm.findFragmentByTag(frTag);
        if ((fr != null) && (fr.isVisible())) {
            fr.isReadyContactInfoImageData(cic);
        }
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            galpb.setProgress(position + 1);
            String sclt_id = gsList.get(position).getClt_id();

            return new GalleryFragment().newInstance(GalleryActivity.this, position, sclt_id);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

}
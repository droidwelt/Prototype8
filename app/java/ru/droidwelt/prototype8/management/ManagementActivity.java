package ru.droidwelt.prototype8.management;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.ContextThemeWrapper;

import ru.droidwelt.prototype8.R;
import ru.droidwelt.prototype8.management.inspect.InspFragment;
import ru.droidwelt.prototype8.management.opl.OplFragment;
import ru.droidwelt.prototype8.management.price.PriceFragment;
import ru.droidwelt.prototype8.utils.common.Appl;
import ru.droidwelt.prototype8.utils.common.MainUtils;
import ru.droidwelt.prototype8.utils.common.PrefUtils;
import ru.droidwelt.prototype8.utils.login.LoginLoader;

public class ManagementActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        setTheme(R.style.AppTheme);
        setTitle(R.string.header_management);

        ViewPager pager = findViewById(R.id.man_pager);
        FragmentManager fm = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(fm, this);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout; // = new TabLayout(new ContextThemeWrapper(this, R.style.AStyle_PageTab2), null, 0);
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

     //   TabLayout.Tab tab1 = tabLayout.getTabAt(0);
     //   tab1.getCustomView().

    /*    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        }); */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch (requestCode) {

            case Appl.EXIT_CODE_LOGIN:
                if (returnedIntent != null) {
                    String code = returnedIntent.getStringExtra("code");
                    new LoginLoader().loginUser(this, code);
                }
                break;
        }
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{"Оплата", "Ревизия", "Прайс"};
        private Context context;

        MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position) {
                case 0:
                    f = OplFragment.newInstance(ManagementActivity.this);
                    break;

                case 1:
                    f = InspFragment.newInstance(ManagementActivity.this);
                    break;

                case 2:
                    f = PriceFragment.newInstance(ManagementActivity.this);
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
      //      int[] imageResId = {R.drawable.main_oplat, R.drawable.main_inspect, R.drawable.main_price};

        //    Drawable image = context.getResources().getDrawable(imageResId[position]);
        //    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" " + tabTitles[position]);
        //    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        //    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }


    }

}
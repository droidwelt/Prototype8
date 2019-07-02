package ru.droidwelt.prototype8.main;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import ru.droidwelt.prototype8.utils.common.PrefUtils;

public class PrefActivity extends AppCompatActivity {

    PreferenceFragment pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new PrefUtils().animateFinish(this);
    }


    public void createFragment() {
        String mode_global = Objects.requireNonNull(getIntent().getExtras()).getString("MODE_GLOBAL", "Y");

        Bundle bundle = new Bundle();
        bundle.putString("MODE_GLOBAL", mode_global);

        pr = new PrefFragment();
        pr.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(android.R.id.content, pr).commit();
    }


    public void refreshFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(pr);
        fragmentTransaction.attach(pr);
        fragmentTransaction.commit();
    }
}

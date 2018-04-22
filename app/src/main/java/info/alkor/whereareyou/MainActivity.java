package info.alkor.whereareyou;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Set;

import info.alkor.whereareyou.common.PermissionRequester;
import info.alkor.whereareyou.common.TextHelper;
import info.alkor.whereareyou.ui.LocationRequestHelper;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_CONTACT_TO_LOCATE = 1;
    private static final TextHelper TEXT_HELPER = new TextHelper();
    private LocationRequestHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new LocationRequestHelper(this,
                getPermissionRequester());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter viewPagerAdapter = new SectionsPagerAdapter
                (getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        PermissionRequester.ResultCallback callback = new PermissionRequester.ResultCallback() {
            @Override
            public void onPermissionRequestResult(Set<String> permissionRequestResult) {
            }
        };
        getPermissionRequester().requestAllPermissions(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        getPermissionRequester().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_CONTACT_TO_LOCATE == requestCode && Activity.RESULT_OK == resultCode) {
            handlePhoneSelectionForLocation(data);
        }
    }

    private void handlePhoneSelectionForLocation(Intent data) {
        final Uri uri = data.getData();
        if (uri == null) return;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                final int displayNameIdx = cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.DISPLAY_NAME);
                final int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds
                        .Phone.DATA);

                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(displayNameIdx);
                    String normalizedPhoneNumber = TEXT_HELPER.normalizePhone(cursor.getString
                            (phoneIdx));
                    helper.confirmLocationRequest(displayName, normalizedPhoneNumber);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void locatePhone(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone
                .CONTENT_URI);
        startActivityForResult(intent, MainActivity.PICK_CONTACT_TO_LOCATE);
    }

    private PermissionRequester getPermissionRequester() {
        return getWhereAreYouContext().getPermissionRequester(this);
    }

    private WhereAreYouContext getWhereAreYouContext() {
        return (WhereAreYouContext) getApplicationContext();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FloatingActionButton fab = findViewById(R.id.locatePhone);
            fab.show();
            switch (position) {
                case 0:
                    return new ActionsFragment();
                case 1:
                    return new UsersFragment();
            }
            throw new IllegalArgumentException("Index out of bounds");
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.actions_title);
                case 1:
                    return getString(R.string.users_title);
            }
            throw new IllegalArgumentException("Index out of bounds");
        }
    }
}

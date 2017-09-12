package com.version.stutijindal.approject;

/*
Created by Stuti Jindal on 04/10/17

Activity Features:
 -TitleBar
 -Navigation Drawer with Current User Details
 -ViewPager
 -Customized TabLayout with Icons
 -OverFlow Menu
 -BackButton disabled
 -ClearFocus from EditText
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import tekle.oss.android.animation.AnimationFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Fragment_List.OnListItemSelectedListener {
    MyFragmentPagerAdapter myPagerAdapter;
    ViewPager mViewPager;
    String tabs[] = {"Home", "Alerts", "Work", "Groups", "Profile"};
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    public Toolbar myToolBar;
    TabLayout tabLayout;
    TextView headername, headeremail;
    ImageView headerimage;
    FirebaseUser user;
    private Fragment mContent1;
    String name, email;
    Uri photoUrl;
    String fragment = " ";
    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.notification,
            R.drawable.work,
            R.drawable.group,
            R.drawable.ic_person_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewAnimator viewAnimator1 = (ViewAnimator) this.findViewById(R.id.animator1);

        //Navigation Drawer
        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        myToolBar.setLogo(R.drawable.icon);
        setActionBarTitle("Work It Up");
        myToolBar.setBackgroundColor(Color.parseColor("#0D1D37"));
        myToolBar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        Intent intent = this.getIntent();
        if (intent != null) {
            fragment = getIntent().getStringExtra("key");
        }

        Log.e("Key from recycler", "" + fragment);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Navigation Header/'
        View header = navigationView.getHeaderView(0);
        headername = (TextView) header.findViewById(R.id.headername);
        headeremail = (TextView) header.findViewById(R.id.headeremail);
        headerimage = (ImageView) header.findViewById(R.id.headerpic);

        //Firebase Current User
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            if (photoUrl != null)
                Picasso.with(this).load(photoUrl).into(headerimage);
            headername.setText(name);
            headeremail.setText(email);
            String tkn = FirebaseInstanceId.getInstance().getToken();
            Log.e("tkn", tkn);
        }


        navigationView.setNavigationItemSelectedListener(this);


        // View Pager
        myPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tabs.length);
        mViewPager = (ViewPager) findViewById(R.id.pager);//ViewPager
        mViewPager.setAdapter(myPagerAdapter); //connection between viewpager and adapter


        customizeViewPager(); //Animation
        tabLayout = (TabLayout) findViewById(R.id.tabs); // find the tab layout
        tabLayout.setupWithViewPager(mViewPager); //connect tab layout with viewpager
        setupTabIcons();
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#0D1D37"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        AnimationFactory.flipTransition(viewAnimator1, AnimationFactory.FlipDirection.LEFT_RIGHT);
                        super.onTabSelected(tab);
                        tab.getIcon().setColorFilter(Color.parseColor("#0D1D37"), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        if (fragment == null || Objects.equals(fragment, ""))
            mViewPager.setCurrentItem(0);
        else if (fragment.equals("fragment4")) {
            mViewPager.setCurrentItem(3);
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        }
        else if (fragment.equals("fragment3")) {
            mViewPager.setCurrentItem(2);
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        }
        else if(fragment.equals("notification")) {
            mViewPager.setCurrentItem(1);
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        }


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }


    public void setActionBarTitle(String title) {
        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setLogo(android.R.drawable.sym_def_app_icon);
    }

    private void customizeViewPager() {

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_info:
                myToolBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(this, developerinfo.class);
                startActivity(intent);
                return true;
        }
        switch (id) {
            case R.id.menu_search:
//                Toast.makeText(getApplicationContext(), "Clicked search", Toast.LENGTH_LONG).show();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.pager);

        Log.d("fragmentID", "" + fragment);

        switch (id) {
            case R.id.drawer_menu1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.drawer_menu2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.drawer_menu3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.drawer_menu4:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.drawer_menu5:
                mViewPager.setCurrentItem(4);
                break;
            case R.id.drawer_menu6:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Adapter class
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        int count;

        public MyFragmentPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            count = size;
        }

        @Override
        //getItem is called on every swipe of a viewPager and this in turn returns a fragment
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment fragment = HomeFragment.newInstance();
                    return fragment;
                case 1:
                    Fragment fragment1 = NotificationsFragment.newInstance();
                    return fragment1;
                case 2:
                    Fragment fragment2 = WorkFragment.newInstance();
                    return fragment2;
                case 3:
                    Fragment fragment3 = GroupsFragment.newInstance();
                    return fragment3;
                case 4:
                    Fragment fragment4 = ProfileFragment.newInstance();
                    return fragment4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        //title of the fragment being displayed
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            String title = tabs[position];
            return title;
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onListItemSelected() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.ngroupfp, GroupsFragment.newInstance())
//                .addToBackStack(null)
//                .commit();
        mViewPager.setCurrentItem(4);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.content_frame, nGroupFragment.newInstance())
//                .addToBackStack(null)
//                .commit();

    }

//    @Override
//    public void onItemSelected() {
//
//        Toast.makeText(this, "You have clocked in for your shift!",
//                Toast.LENGTH_LONG).show();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.main_content, nGroupFragment.newInstance())
//                .addToBackStack(null)
//                .commit();
//
//    }

    public static void hideKeyboardOnStartActivity(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(MainActivity.this, "On Pause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboardOnStartActivity(MainActivity.this);
//        Toast.makeText(MainActivity.this, "On Resume", Toast.LENGTH_SHORT).show();

    }



}

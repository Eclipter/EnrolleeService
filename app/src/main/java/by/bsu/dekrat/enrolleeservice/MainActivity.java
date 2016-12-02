package by.bsu.dekrat.enrolleeservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import by.bsu.dekrat.enrolleeservice.bean.RestTemplateProvider;
import by.bsu.dekrat.enrolleeservice.bean.SessionHolder;
import by.bsu.dekrat.enrolleeservice.bean.UserInfoProvider;
import by.bsu.dekrat.enrolleeservice.entity.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.newsToolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.newsViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.newsTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Новости"));
        tabLayout.addTab(tabLayout.newTab().setText("Правила"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_news);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_news_view);
        navigationView.setNavigationItemSelectedListener(this);

        User user;

        try {
            user = new GetUserInfoTask().execute().get();
            UserInfoProvider.getInstance().setCurrentUser(user);
        } catch (InterruptedException | ExecutionException e) {
            Log.e("User info retrieving", e.getMessage(), e);
            return;
        }


        TextView headerNameTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerNameTextView);
        TextView headerEmailTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerEmailTextView);

        headerNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        headerEmailTextView.setText(user.getEmail());
    }

    private class GetUserInfoTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            RestTemplate restTemplate = RestTemplateProvider.getInstance().getCommonTemplate();
            String URL = "https://enrollee-service.herokuapp.com/users/current";
            HttpHeaders httpHeaders = SessionHolder.getInstance().getHeadersWithSessionID();
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<User> response = restTemplate.exchange(URL, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<User>() {}, Collections.emptyMap());
            return response.getBody();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_news);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_enrollment) {
            Intent intent = new Intent(this, TestEnrollmentActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_my_tests) {
            Intent intent = new Intent(this, MyTestsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_news);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        PagerAdapter(android.support.v4.app.FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NewsFragment();
                case 1:
                    return new RulesFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

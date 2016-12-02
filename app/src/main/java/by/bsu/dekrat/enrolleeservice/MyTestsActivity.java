package by.bsu.dekrat.enrolleeservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import by.bsu.dekrat.enrolleeservice.bean.RestTemplateProvider;
import by.bsu.dekrat.enrolleeservice.bean.SessionHolder;
import by.bsu.dekrat.enrolleeservice.bean.UserInfoProvider;
import by.bsu.dekrat.enrolleeservice.entity.TestAssignment;
import by.bsu.dekrat.enrolleeservice.entity.User;

public class MyTestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<TestAssignment> assignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.testsToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tests);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_tests_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_news) {
                    Intent intent = new Intent(MyTestsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if(id == R.id.nav_enrollment) {
                    Intent intent = new Intent(MyTestsActivity.this, TestEnrollmentActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_account) {
                    Intent intent = new Intent(MyTestsActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tests);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        User currentUser = UserInfoProvider.getInstance().getCurrentUser();
        TextView headerNameTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerNameTextView);
        TextView headerEmailTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerEmailTextView);
        headerNameTextView.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        headerEmailTextView.setText(currentUser.getEmail());

        mRecyclerView = (RecyclerView) findViewById(R.id.testRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<TestAssignment> assignments;
        try {
            assignments = new GetAssignmentListTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Test assignments", e.getMessage(), e);
            return;
        }

        mAdapter = new TestAssignmentListAdapter(assignments);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private class GetAssignmentListTask extends AsyncTask<Void, Void, List<TestAssignment>> {

        @Override
        protected List<TestAssignment> doInBackground(Void... voids) {
            String URL = "https://enrollee-service.herokuapp.com/testAssignments/" +
                    "search/findByEnrolleeId";
            int userId = UserInfoProvider.getInstance().getCurrentUser().getId();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("enrolleeId",
                    userId);
            RestTemplate restTemplate = RestTemplateProvider.getInstance().getHateoasTemplate();
            HttpEntity<Object> httpEntity = new HttpEntity<>(
                    SessionHolder.getInstance().getHeadersWithSessionID());
            ResponseEntity<PagedResources<TestAssignment>> response = restTemplate.exchange(
                    builder.build().encode().toUriString(),
                    HttpMethod.GET, httpEntity,
                    new ParameterizedTypeReference<PagedResources<TestAssignment>>() {},
                    Collections.emptyMap());
            return new ArrayList<>(response.getBody().getContent());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_tests);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}

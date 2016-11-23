package by.bsu.dekrat.enrolleeservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class TestEnrollmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_enrollment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.enrollmentToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_enrollment);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_enrollment_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_news) {
                    Intent intent = new Intent(TestEnrollmentActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if(id == R.id.nav_my_tests) {
                    Intent intent = new Intent(TestEnrollmentActivity.this, MyTestsActivity.class);
                    startActivity(intent);
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_enrollment);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Spinner universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        Spinner subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);

        Button enrollButton = (Button) findViewById(R.id.enrollButton);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEnroll();
            }
        });
    }

    private void attemptEnroll() {

    }
}

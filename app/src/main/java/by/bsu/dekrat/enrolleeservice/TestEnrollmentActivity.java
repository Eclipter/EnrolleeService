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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import by.bsu.dekrat.enrolleeservice.bean.RestTemplateProvider;
import by.bsu.dekrat.enrolleeservice.bean.SessionHolder;
import by.bsu.dekrat.enrolleeservice.bean.UserInfoProvider;
import by.bsu.dekrat.enrolleeservice.entity.Subject;
import by.bsu.dekrat.enrolleeservice.entity.Test;
import by.bsu.dekrat.enrolleeservice.entity.University;
import by.bsu.dekrat.enrolleeservice.entity.User;

public class TestEnrollmentActivity extends AppCompatActivity {

    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm";
    private static final String TIME_ZONE = "UTC";

    Spinner universitySpinner;
    Spinner subjectSpinner;
    Spinner testSpinner;

    List<University> universities;
    List<Subject> subjects;
    List<Test> tests;

    University currentUniversity;
    Subject currentSubject;
    Test currentTest;

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

                if (id == R.id.nav_news) {
                    Intent intent = new Intent(TestEnrollmentActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_my_tests) {
                    Intent intent = new Intent(TestEnrollmentActivity.this, MyTestsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_account) {
                    Intent intent = new Intent(TestEnrollmentActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_enrollment);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.nav_enrollment);

        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        testSpinner = (Spinner) findViewById(R.id.testSpinner);

        Button enrollButton = (Button) findViewById(R.id.enrollButton);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEnroll();
            }
        });

        User currentUser = UserInfoProvider.getInstance().getCurrentUser();
        TextView headerNameTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerNameTextView);
        TextView headerEmailTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerEmailTextView);
        headerNameTextView.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        headerEmailTextView.setText(currentUser.getEmail());

        new GetUniversitiesTask().execute();

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                currentUniversity = universities.get(pos);
                new GetSubjectsTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSubject = subjects.get(i);
                new GetTestsTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentTest = tests.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private class GetUniversitiesTask extends AsyncTask<Void, Void, Collection<University>> {

        @Override
        protected Collection<University> doInBackground(Void... voids) {
            try {
                currentUniversity = null;
                final String URL = "https://enrollee-service.herokuapp.com/universities";

                RestTemplate restTemplate = RestTemplateProvider.getInstance().getHateoasTemplate();

                HttpEntity<Object> httpEntity = new HttpEntity<>(
                        SessionHolder.getInstance().getHeadersWithSessionID());

                ResponseEntity<PagedResources<University>> response = restTemplate.exchange(URL,
                        HttpMethod.GET, httpEntity,
                        new ParameterizedTypeReference<PagedResources<University>>() {},
                Collections.emptyMap());
                Collection<University> content = response.getBody().getContent();
                TestEnrollmentActivity.this.universities = new ArrayList<>(content);
                return content;
            } catch (Exception ex) {
                Log.e(TestEnrollmentActivity.class.getName(), ex.getMessage(), ex);
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(Collection<University> universities) {
            List<String> universityInfo = new ArrayList<>();
            for(University university : universities) {
                universityInfo.add(university.getName() + " (" + university.getAddress() + ")");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(TestEnrollmentActivity.this,
                    android.R.layout.simple_spinner_item, universityInfo);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            universitySpinner.setAdapter(adapter);
        }
    }

    private class GetSubjectsTask extends AsyncTask<Void, Void, List<Subject>> {

        @Override
        protected List<Subject> doInBackground(Void... voids) {
            try {
                currentSubject = null;
                int id = currentUniversity.getId();
                String URL = "https://enrollee-service.herokuapp.com/universities";
                URL += "/" + id + "/subjects";

                RestTemplate restTemplate = RestTemplateProvider.getInstance().getHateoasTemplate();

                HttpEntity<Object> httpEntity = new HttpEntity<>(
                        SessionHolder.getInstance().getHeadersWithSessionID());
                ResponseEntity<PagedResources<Subject>> response = restTemplate.exchange(URL,
                        HttpMethod.GET, httpEntity, new ParameterizedTypeReference<PagedResources<Subject>>() {},
                        Collections.emptyMap());
                List<Subject> content = new ArrayList<>(response.getBody().getContent());
                TestEnrollmentActivity.this.subjects = content;
                return content;
            } catch (Exception ex) {
                Log.e(TestEnrollmentActivity.class.getName(), ex.getMessage(), ex);
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(List<Subject> subjects) {
            List<String> subjectInfo = new ArrayList<>();
            for(Subject subject : subjects) {
                subjectInfo.add(subject.getName() + " (" + subject.getCode() + ")");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(TestEnrollmentActivity.this,
                    android.R.layout.simple_spinner_item, subjectInfo);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectSpinner.setAdapter(adapter);
        }
    }

    private class GetTestsTask extends AsyncTask<Void, Void, List<Test>> {

        @Override
        protected List<Test> doInBackground(Void... voids) {
            try {
                currentTest = null;
                int universityId = currentUniversity.getId();
                int subjectId = currentSubject.getId();
                String URL = "https://enrollee-service.herokuapp.com/" +
                        "tests/search/findByUniversityIdAndSubjectId";

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).
                        queryParam("universityId", universityId).queryParam("subjectId", subjectId);

                RestTemplate restTemplate = RestTemplateProvider.getInstance().getHateoasTemplate();

                HttpEntity<Object> httpEntity = new HttpEntity<>(
                        SessionHolder.getInstance().getHeadersWithSessionID());
                ResponseEntity<PagedResources<Test>> response = restTemplate.exchange(
                        builder.build().encode().toUriString(),
                        HttpMethod.GET, httpEntity, new ParameterizedTypeReference<PagedResources<Test>>() {},
                        Collections.emptyMap());
                List<Test> content = new ArrayList<>(response.getBody().getContent());
                TestEnrollmentActivity.this.tests = content;
                return content;
            } catch (Exception ex) {
                Log.e(TestEnrollmentActivity.class.getName(), ex.getMessage(), ex);
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(List<Test> tests) {
            List<String> testInfo = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
            format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            for(Test test : tests) {
                testInfo.add(test.getType() + ": " + format.format(test.getDate()));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(TestEnrollmentActivity.this,
                    android.R.layout.simple_spinner_item, testInfo);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            testSpinner.setAdapter(adapter);
        }
    }

    private void attemptEnroll() {
        if(currentUniversity != null && currentSubject != null && currentTest != null) {
            new EnrollOnTestTask().execute();
        }
    }

    private class EnrollOnTestTask extends AsyncTask<Void, Void, Void> {

        boolean failed = false;

        @Override
        protected Void doInBackground(Void... voids) {
            int testId = currentTest.getId();
            String URL = "https://enrollee-service.herokuapp.com/tests/assign/" + testId;
            RestTemplate restTemplate = RestTemplateProvider.getInstance().getCommonTemplate();
            HttpHeaders headers = SessionHolder.getInstance().getHeadersWithSessionID();
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            try {
                restTemplate.exchange(URL, HttpMethod.PUT, entity, Void.class);
            } catch (RestClientException ex) {
                failed = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String text = "Вы успешно зарегистрировались на тест!";
            String errorText = "Не удалось зарегистрироваться на тест. Вы уже на него записались";
            if(!failed) {
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), errorText, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}

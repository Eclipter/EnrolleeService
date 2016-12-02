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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import by.bsu.dekrat.enrolleeservice.bean.PasswordChangeContext;
import by.bsu.dekrat.enrolleeservice.bean.SessionHolder;
import by.bsu.dekrat.enrolleeservice.bean.UserInfoProvider;
import by.bsu.dekrat.enrolleeservice.entity.User;

public class AccountActivity extends AppCompatActivity {

    EditText oldPasswordTextView;
    EditText newPasswordTextView;
    EditText repeatPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.accountToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_account);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_account_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_news) {
                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_my_tests) {
                    Intent intent = new Intent(AccountActivity.this, MyTestsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_enrollment) {
                    Intent intent = new Intent(AccountActivity.this, TestEnrollmentActivity.class);
                    startActivity(intent);
                    finish();
                } else if(id == R.id.nav_account) {
                    Intent intent = new Intent(AccountActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_share) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Привет! " +
                            "Как насчёт оценить новое приложение для регистрации абитуриентов на РТ и ЦТ? :)");
                    try {
                        startActivity(Intent.createChooser(intent, "Поделиться..."));
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(AccountActivity.this, "Нет приложений для того, чтобы поделиться", Toast.LENGTH_SHORT).show();
                    }
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_account);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.headerImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navigationView.setCheckedItem(R.id.nav_account);

        User currentUser = UserInfoProvider.getInstance().getCurrentUser();
        TextView headerNameTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerNameTextView);
        TextView headerEmailTextView = (TextView)
                navigationView.getHeaderView(0).findViewById(R.id.headerEmailTextView);
        headerNameTextView.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        headerEmailTextView.setText(currentUser.getEmail());

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
        TextView passportIdTextView = (TextView) findViewById(R.id.passportIsTextView);
        TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumberTextView);

        nameTextView.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " " +
                currentUser.getMiddleName());
        emailTextView.setText(currentUser.getEmail());
        passportIdTextView.setText(currentUser.getPassportId());
        phoneNumberTextView.setText(currentUser.getPhoneNumber());

        oldPasswordTextView = (EditText) findViewById(R.id.oldPasswordEditText);
        newPasswordTextView = (EditText) findViewById(R.id.newPasswordEditText);
        repeatPasswordTextView = (EditText) findViewById(R.id.repeatPasswordEditText);

        Button changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        oldPasswordTextView.setError(null);
        newPasswordTextView.setError(null);
        repeatPasswordTextView.setError(null);

        String oldPassword = oldPasswordTextView.getText().toString();
        String newPassword = newPasswordTextView.getText().toString();
        String repeatPassword = repeatPasswordTextView.getText().toString();

        String errorMessage = "Поле не должно быть пустым";

        if("".equals(oldPassword)) {
            oldPasswordTextView.setError(errorMessage);
            oldPasswordTextView.requestFocus();
        } else if("".equals(newPassword) || newPassword.length() < 6) {
            errorMessage = "Пароль должен быть длиннее 5 символов";
            newPasswordTextView.setError(errorMessage);
            newPasswordTextView.requestFocus();
        } else if("".equals(repeatPassword)) {
            repeatPasswordTextView.setError(errorMessage);
            repeatPasswordTextView.requestFocus();
        } else if(!newPassword.equals(repeatPassword)) {
            errorMessage = "Пароли должны совпадать";
            repeatPasswordTextView.setError(errorMessage);
            repeatPasswordTextView.requestFocus();
        } else {
            try {
                Boolean loginSuccessful = new LoginTask(oldPassword, newPassword).execute().get();

                if(loginSuccessful) {
                    Boolean changeSuccessful = new ChangePasswordTask(oldPassword, newPassword).execute().get();
                    if(changeSuccessful) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Пароль изменён. Повторите вход",
                                Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                Log.e("Interrupted", e.getMessage(), e);
            }
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private String oldPassword;
        private String newPassword;
        private HttpStatus errorStatus;

        public LoginTask(String password, String newPassword) {
            this.oldPassword = password;
            this.newPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String URL = "https://enrollee-service.herokuapp.com/login";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(),
                    new MappingJackson2HttpMessageConverter()));
            final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

            form.add("login", UserInfoProvider.getInstance().getCurrentUser().getEmail());
            form.add("password", oldPassword);

            ResponseEntity<Object> responseEntity;

            try {
                responseEntity = restTemplate.postForEntity(URL, form, Object.class);
            } catch (HttpClientErrorException ex) {
                Log.e("Authentication", ex.getMessage(), ex);
                errorStatus = ex.getStatusCode();
                return false;
            }
            SessionHolder.getInstance().retrieveSessionID(responseEntity);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean) {
                if(errorStatus.is4xxClientError()) {
                    String errorMessage = "Старый пароль неверен";
                    oldPasswordTextView.setError(errorMessage);
                    oldPasswordTextView.requestFocus();
                }
            }
        }
    }

    private class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private String oldPassword;
        private String newPassword;

        public ChangePasswordTask(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String URL = "https://enrollee-service.herokuapp.com/users";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("oldPassword",
                    oldPassword).queryParam("newPassword", newPassword);
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(converters);

            HttpHeaders headers = SessionHolder.getInstance().getHeadersWithSessionID();
            headers.setContentType(MediaType.APPLICATION_JSON);
            PasswordChangeContext passwordChangeContext =
                    new PasswordChangeContext(oldPassword, newPassword);
            HttpEntity<PasswordChangeContext> httpEntity =
                    new HttpEntity<>(passwordChangeContext, headers);
            try {
                restTemplate.exchange(builder.build().toUriString(),
                        HttpMethod.PATCH, httpEntity, Void.class);
            } catch (RestClientException ex) {
                Log.e("Password change", ex.getMessage(), ex);
                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка при изменении пароля",
                        Toast.LENGTH_LONG);
                toast.show();
                return false;
            }

            return true;
        }
    }
}

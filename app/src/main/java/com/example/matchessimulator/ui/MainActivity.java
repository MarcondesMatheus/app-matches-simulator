package com.example.matchessimulator.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.matchessimulator.R;
import com.example.matchessimulator.data.MatchesAPI;
import com.example.matchessimulator.databinding.ActivityMainBinding;
import com.example.matchessimulator.domain.Match;
import com.example.matchessimulator.ui.adapter.MatchesAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchesAPI matchesApi;
    private MatchesAdapter matchesAdapter = new MatchesAdapter(Collections.EMPTY_LIST);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       binding =  ActivityMainBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       setupHttpClient();
       setupMacthesList();
       setUpMatchRefresh();
       setFloatingActionButton();


    }

    private void setupHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://marcondesmatheus.github.io/matches-simulator-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        matchesApi = retrofit.create(MatchesAPI.class);
    }

    private void setFloatingActionButton() {
        binding.fabSimulator.setOnClickListener(v -> v.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Random random = new Random();
                for (int i = 0; i <matchesAdapter.getItemCount() ; i++) {
                    Match match = matchesAdapter.getMatches().get(i);
                    match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStar() + 1));
                    match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStar() + 1));
                    matchesAdapter.notifyItemChanged(i); //atualiza o adapter

                }
            }
        }));
    }

    private void setUpMatchRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi);
    }

    private void setupMacthesList() {
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        matchesAdapter = new MatchesAdapter(Collections.EMPTY_LIST);
        binding.rvMatches.setAdapter(matchesAdapter);
        findMatchesFromApi();
    }

    private void findMatchesFromApi() {
        binding.srlMatches.setRefreshing(true);
        matchesApi.getMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful()){
                    List<Match> matches = response.body();
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                }
                binding.srlMatches.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                showErrorMessage();
                binding.srlMatches.setRefreshing(false);
            }
        });
    }

    private void showErrorMessage() {
        Snackbar.make(binding.fabSimulator, R.string.error_api, Snackbar.LENGTH_SHORT).show();
    }
}
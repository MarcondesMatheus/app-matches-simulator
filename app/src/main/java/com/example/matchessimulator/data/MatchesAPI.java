package com.example.matchessimulator.data;

import com.example.matchessimulator.domain.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MatchesAPI {

    @GET("matches.json")
    Call<List<Match>> getMatches();
}

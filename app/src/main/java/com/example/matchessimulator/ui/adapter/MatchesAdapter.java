package com.example.matchessimulator.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matchessimulator.databinding.MatchItemBinding;
import com.example.matchessimulator.domain.Match;
import com.example.matchessimulator.ui.DetailActivity;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    public List<Match> getMatches() {
        return matches;
    }

    private List<Match> matches;

    public MatchesAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MatchItemBinding binding = MatchItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Match match = matches.get(position);

        //Adapta os dados da partida (recuperada da API) para o nosso layout
        Glide.with(context).load(match.getHomeTeam().getImage()).into(holder.binding.ivHomeTeam); //circleCrop() arredonda as imagens
        Glide.with(context).load(match.getAwayTeam().getImage()).into(holder.binding.ivAwayTeam);

        holder.binding.tvHomeTeam.setText(match.getHomeTeam().getName());
        if (match.getHomeTeam().getScore() != null) {
            holder.binding.tvHomeTeamScore.setText(String.valueOf(match.getHomeTeam().getScore()));
        }

        holder.binding.tvAwayTeam.setText(match.getAwayTeam().getName());
        if (match.getAwayTeam().getScore() != null){
            holder.binding.tvAwayTeamScore.setText(String.valueOf(match.getAwayTeam().getScore()));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.Extras.MATCH, match);
            context.startActivity(intent);
        });





    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MatchItemBinding binding;

        public ViewHolder(MatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

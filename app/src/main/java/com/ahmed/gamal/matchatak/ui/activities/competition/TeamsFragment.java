package com.ahmed.gamal.matchatak.ui.activities.competition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.gamal.matchatak.R;
import com.ahmed.gamal.matchatak.adapters.TeamsAdapter;
import com.ahmed.gamal.matchatak.model.Team;
import com.ahmed.gamal.matchatak.viewmodels.TeamsViewModel;

import java.util.List;

public class TeamsFragment extends Fragment implements TeamsAdapter.OnTeamClickListener {


    private RecyclerView recyclerView;
    private static String ID = "id";
    private static String SEASON = "season";
    private View progressbar;

    private OnTeamsInteractionListener mListener;

    public TeamsFragment() {
    }

    public static TeamsFragment newInstance(int competitionId, int season) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putInt(ID, competitionId);
        args.putInt(SEASON, season);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TeamsViewModel viewModel = ViewModelProviders.of(this).get(TeamsViewModel.class);
        if (getArguments() != null) {
            int competitionId = getArguments().getInt(ID);
            int season = getArguments().getInt(SEASON);
            viewModel.getCompetitionTeamsList(competitionId, season).observe(this, teams -> {
                if (teams != null)
                    setTeamsToView(teams);
            });
        }
    }

    private void setTeamsToView(List<Team> teams) {
        TeamsAdapter adapter = new TeamsAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setData(teams);
        progressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        progressbar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.rv_teams);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeamsInteractionListener) {
            mListener = (OnTeamsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTeamClick(int teamId) {
        if (mListener != null) {
            mListener.onTeamInteraction(teamId);
        }
    }


    public interface OnTeamsInteractionListener {
        void onTeamInteraction(int teamId);
    }
}

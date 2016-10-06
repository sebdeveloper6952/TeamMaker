package com.example.sebdeveloper6952.teammaker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MakeTeamsActivity extends AppCompatActivity
{

    ListView lv_Team1, lv_Team2;
    TextView tV_Team1, tV_Team2;
    Button btn_MakeTeams;
    CTeamMaker teamMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_teams);

        // get Views
        lv_Team1 = (ListView)findViewById(R.id.lV_Team1);
        lv_Team2 = (ListView)findViewById(R.id.lV_Team2);
        btn_MakeTeams = (Button)findViewById(R.id.MakeTeamsA_btn_MakeTeams);
        tV_Team1 = (TextView)findViewById(R.id.tV_Team1);
        tV_Team2 = (TextView)findViewById(R.id.tV_Team2);

        // grab the starting intent to retrieve the members list
        Intent startingIntent = getIntent();
        try
        {
            List<String> startingList = startingIntent.getStringArrayListExtra("members");
            teamMaker = new CTeamMaker(startingList);
            teamMaker.MakeTeams(getApplicationContext());
            double[] weights = teamMaker.GetTeamsWeight();
            String team1S = "Team 1: " + weights[0];
            String team2S = "Team 2: " + weights[1];
            tV_Team1.setText(team1S);
            tV_Team2.setText(team2S);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // create and set array adapters to listviews
        final ArrayAdapter<String> team1Adapter = new ArrayAdapter<String>(this, R.layout.simple_list_view, teamMaker.team1String);
        final ArrayAdapter<String> team2Adapter = new ArrayAdapter<String>(this, R.layout.simple_list_view, teamMaker.team2String);
        lv_Team1.setAdapter(team1Adapter);
        lv_Team2.setAdapter(team2Adapter);
        team1Adapter.notifyDataSetChanged();
        team2Adapter.notifyDataSetChanged();

        btn_MakeTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    teamMaker.MakeTeams(getApplicationContext());
                    team1Adapter.notifyDataSetChanged();
                    team2Adapter.notifyDataSetChanged();
                    double[] weights = teamMaker.GetTeamsWeight();
                    String team1S = "Team 1: " + weights[0];
                    String team2S = "Team 2: " + weights[1];
                    tV_Team1.setText(team1S);
                    tV_Team2.setText(team2S);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast t = Toast.makeText(getApplicationContext(), "Error trying to make teams.", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }
}

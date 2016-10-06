package com.example.sebdeveloper6952.teammaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity
{
    ListView lv_Members;
    Button btn_addMember, btn_makeTeams, btn_SaveList;
    TextView eT_Name, eT_Weight;
    CTeamMaker teamMaker;
    final String SAVED_SUCCESS_MESSAGE = "Saved successfully.";
    final String SAVED_FAIL_MESSAGE = "Error while trying to save the list.";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // get views
        lv_Members = (ListView)findViewById(R.id.lV_Members);
        btn_addMember = (Button)findViewById(R.id.btn_AddMember);
        btn_makeTeams = (Button)findViewById(R.id.btn_MakeTeams);
        btn_SaveList = (Button)findViewById(R.id.btn_SaveList);
        eT_Name = (TextView)findViewById(R.id.eT_Name);
        eT_Weight = (TextView)findViewById(R.id.eT_Weight);

        // start team maker
        teamMaker = new CTeamMaker();

        // try reading and loading the saved list of members
        try
        {
            teamMaker.ReadListFromFile(this);
        }
        catch(Exception e) {e.printStackTrace();}

        // array adapter is used to send data to listView
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_view, teamMaker.membersString);
        lv_Members.setAdapter(arrayAdapter);

        // delete a from the members list with a long click
        lv_Members.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                teamMaker.DeleteMember(i);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // add the member with the specified data
        btn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    String name = eT_Name.getText().toString();
                    double weight = Double.parseDouble(eT_Weight.getText().toString());
                    // clamp weight
                    if(weight > 10)
                        weight = 10;
                    else if(weight < 0)
                        weight = 0;
                    CMember member = new CMember(name, weight);
                    teamMaker.AddMember(member);
                    arrayAdapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // launch the make teams activity
        btn_makeTeams.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(teamMaker.members.size() < 4)
                {
                    Toast t = Toast.makeText(getApplicationContext(), "Please add at least 4 members.", Toast.LENGTH_SHORT);
                    t.show();
                }
                else
                {
                    Intent makeTeamsIntent = new Intent(v.getContext(), MakeTeamsActivity.class);
                    makeTeamsIntent.putStringArrayListExtra("members", teamMaker.membersString);
                    startActivity(makeTeamsIntent);
                }
            }
        });

        // save list
        btn_SaveList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    teamMaker.SaveListToFile(v.getContext());
                    Toast t = Toast.makeText(getApplicationContext(), SAVED_SUCCESS_MESSAGE, Toast.LENGTH_SHORT);
                    t.show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast t = Toast.makeText(getApplicationContext(), SAVED_FAIL_MESSAGE, Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }
}

package com.example.sebdeveloper6952.teammaker;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

/**
 * Created by sevic69 on 10/2/2016.
 */

public class CTeamMaker
{
    public List<CMember> members;
    public List<CMember> team1;
    public List<CMember> team2;
    public ArrayList<String> membersString;
    public List<String> team1String;
    public List<String> team2String;

    private double teamWeightSpread;
    private Random r;
    private String saveFileName;
    private File saveFile;
    private double team1Weight;
    private double team2Weight;
    private int loops, randomInt;

    public CTeamMaker()
    {
        members = new ArrayList<CMember>();
        team1 = new ArrayList<CMember>();
        team2 = new ArrayList<CMember>();
        membersString = new ArrayList<String>();
        team1String = new ArrayList<String>();
        team2String = new ArrayList<String>();
        r = new Random();
        teamWeightSpread = 2;
        saveFileName = "saveFile.txt";
    }

    // Used to start this object using a provided ArrayList of members
    public CTeamMaker(List<String> list)
    {
        members = new ArrayList<CMember>();
        team1 = new ArrayList<CMember>();
        team2 = new ArrayList<CMember>();
        membersString = new ArrayList<String>();
        team1String = new ArrayList<String>();
        team2String = new ArrayList<String>();
        r = new Random();
        teamWeightSpread = 2;
        saveFileName = "saveFile.txt";
        for(String s : list)
        {
            String[] content = s.split(" ");
            CMember member = new CMember(content[0], Double.parseDouble(content[1]));
            AddMember(member);
        }
    }

    public void SaveListToFile(Context context)
    {
        OutputStreamWriter writer;
        try
        {
            writer = new OutputStreamWriter(context.openFileOutput(saveFileName, Context.MODE_PRIVATE));
            for(String s: membersString)
            {
                writer.write(s + '\n');
            }
            writer.close();
        }
        catch(IOException e) {e.printStackTrace();}
    }

    public void ReadListFromFile(Context context)
    {
        InputStream inputStream;
        try
        {
            inputStream = context.openFileInput(saveFileName);
            if(inputStream != null)
            {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String s = "";
                StringBuilder builder = new StringBuilder();
                while((s = bufferedReader.readLine()) != null)
                {
                    String[] memberAsString = s.split(" ");
                    CMember member = new CMember(memberAsString[0], Double.parseDouble(memberAsString[1]));
                    AddMember(member);
                }
            }
        }
        catch(IOException e) {e.printStackTrace();}
    }

    public void MakeTeams(Context c)
    {
        team1Weight = team2Weight = 0;
        randomInt = loops = 0;
        do
        {
            // always perform this operation before, to prevent errors.
            members.clear();
            for(String s : membersString)
            {
                String[] sA = s.split(" ");
                CMember m = new CMember(sA[0], Double.parseDouble(sA[1]));
                members.add(m);
            }
            team1.clear();
            team2.clear();
            team1String.clear();
            team2String.clear();

            loops = Math.round(members.size() / 2f);
            for(int i = 0; i < loops; i++)
            {
                try
                {
                    randomInt = r.nextInt(members.size());
                    team1.add(members.get(randomInt));
                    members.remove(randomInt);
                    randomInt = r.nextInt(members.size());
                    team2.add(members.get(randomInt));
                    members.remove(randomInt);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    break;
                }
                team1Weight = CalculateTeamWeight(team1);
                team2Weight = CalculateTeamWeight(team2);
            }
        }
        while(Math.abs(team1Weight - team2Weight) > teamWeightSpread);
        /* the last step is to add the names of the members of each team to the teamStrings
           because the teamStrings are the ones that provide data to the listviews on GUI. */
        for(CMember m : team1) team1String.add(m.toString());
        for(CMember m : team2) team2String.add(m.toString());
    }


    public double CalculateTeamWeight(List<CMember> team)
    {
        double weight = 0;
        for (CMember m: team)
        {
            weight += m.weight;
        }
        return weight;
    }

    public void AddMember(CMember m)
    {
        this.members.add(m);
        this.membersString.add(m.toString());
    }

    public void DeleteMember(int index)
    {
        this.members.remove(index);
        this.membersString.remove(index);
    }

    public void SetMembersString(ArrayList<String> list)
    {
        this.membersString = list;
    }

    public double[] GetTeamsWeight()
    {
        double[] weights = {team1Weight, team2Weight};
        return weights;
    }
}

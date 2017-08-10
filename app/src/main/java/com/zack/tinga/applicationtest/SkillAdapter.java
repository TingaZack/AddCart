package com.zack.tinga.applicationtest;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/07/31.
 */

public class SkillAdapter extends ArrayAdapter<Skill> {

    private Activity context;
    private List<Skill> skills;

    public SkillAdapter(@NonNull Activity context, ArrayList<Skill> skills) {
        super(context, R.layout.list_skill_layout, skills);
        this.skills = skills;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View skillsViewItems = inflater.inflate(R.layout.list_skill_layout, null, false);

        TextView textViewName = skillsViewItems.findViewById(R.id.textViewName);
        TextView textViewRating = skillsViewItems.findViewById(R.id.textViewRating);

        Skill skill = skills.get(position);
        textViewName.setText(skill.getName());
        textViewRating.setText(String.valueOf(skill.getHobbieRating()));

        return skillsViewItems;
    }
}

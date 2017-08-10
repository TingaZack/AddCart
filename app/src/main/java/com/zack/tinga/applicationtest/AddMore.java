package com.zack.tinga.applicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddMore extends AppCompatActivity {

    EditText editTextSkill;
    TextView textView_Name;
    Button buttonAdd;
    SeekBar seekBarRating;
    ListView listViewSkill;

    DatabaseReference mDatabaseSkills;

    List<Skill> skillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more);

        skillList = new ArrayList<Skill>();

        String name = getIntent().getExtras().getString("name");
        String id = getIntent().getExtras().getString("id");

        mDatabaseSkills = FirebaseDatabase.getInstance().getReference("skill").child(id);

        editTextSkill = (EditText) findViewById(R.id.editText_skill);
        textView_Name = (TextView) findViewById(R.id.textViewPerson);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        listViewSkill = (ListView) findViewById(R.id.listSkill);
        buttonAdd = (Button) findViewById(R.id.button_add);

        textView_Name.setText(name);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveHobbies();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseSkills.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                skillList.clear();
                for (DataSnapshot skillSnapshot : dataSnapshot.getChildren()){

                    Skill skill = skillSnapshot.getValue(Skill.class);

                    String id = skill.getId();
                    skill.getHobbieRating();
                    String name = skill.getName();
                    skillList.add(skill);

                    Toast.makeText(getApplicationContext(), "Name: " + name, Toast.LENGTH_SHORT).show();

                }

                SkillAdapter skillAdapter = new SkillAdapter(AddMore.this, (ArrayList<Skill>) skillList);
                listViewSkill.setAdapter(skillAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveHobbies() {

        String skill = editTextSkill.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if (!TextUtils.isEmpty(skill)){

            String id = mDatabaseSkills.push().getKey();
            Skill skills = new Skill(id, skill, rating);
            mDatabaseSkills.child(id).setValue(skills);

            Toast.makeText(getApplicationContext(), "successfully added!", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getApplicationContext(), "Hobbie cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }
}

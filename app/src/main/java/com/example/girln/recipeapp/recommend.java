package com.example.girln.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recommend extends AppCompatActivity {

    RecyclerView mRV;
    RecyclerView.LayoutManager mLM;
    DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    List<String> keylist = new ArrayList<>();
    String key = null;
    private DatabaseReference mData;
    private ArrayList<item_recipe> item_recipeArrayList = new ArrayList<>();
    private List RList;
    private Map tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        key = getIntent().getStringExtra("searchText");

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Recipe");

        mRV = findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);

        mLM = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLM);

        mData = FirebaseDatabase.getInstance().getReference();

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object Recipe_ids = dataSnapshot.child("Recipes").getValue();
                tmp = (HashMap) Recipe_ids;
                keylist.addAll(tmp.keySet());

                for (String str : keylist) {
                    Object data = dataSnapshot.child("Recipes").child(str).getValue();
                    String temp = data.toString();
                    if (temp.contains(key)) {
                        Map n = (HashMap) data;

                        String uid = (String) n.get("userID");
                        String title = (String) n.get("recipeName");
                        ArrayList pic = (ArrayList) n.get("cookingPictures");
                        ArrayList inge = (ArrayList) n.get("cookingIngredients");
                        ArrayList steps = (ArrayList) n.get("cookingSteps");
                        ArrayList tags = (ArrayList) n.get("cookingTags");

                        item_recipeArrayList.add(new item_recipe(uid, pic, title, 0.0, inge, steps, tags, key, false));

                    }
                }
                MyAdapter myAdapter = new MyAdapter(item_recipeArrayList);
                mRV.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }
}


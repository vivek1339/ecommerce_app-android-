package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class product_list extends AppCompatActivity {
    DatabaseReference dbref;
    RecyclerView re;
    List<item> list;
    ImageView bmImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent intent=getIntent();

        String type=intent.getStringExtra("type");
        dbref = FirebaseDatabase.getInstance().getReference().child("product").child(type);
        re = (RecyclerView) findViewById(R.id.rcv2);
        re.setLayoutManager(new LinearLayoutManager(this));
        re.setHasFixedSize(true);
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("loading");
        progressdialog.show();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                bmImage=(ImageView)findViewById(R.id.image);
                // StringBuffer stringbuffer = new StringBuffer();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    item userdetails = dataSnapshot1.getValue(item.class);
                    item listdata = new item();
                    String name = userdetails.getName();
                    int price = userdetails.getPrice();
                    String url=userdetails.geturl();
                    listdata.setuid(dataSnapshot1.getKey());
                    listdata.settype(userdetails.gettype());
                    listdata.setDesc(userdetails.getDesc());
                    listdata.setCreator(userdetails.getCreator());
                    listdata.setName(name);
                    listdata.setPrice(price);
                    listdata.seturl(url);
                    list.add(listdata);
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                RecyclerviewAdapter recycler = new RecyclerviewAdapter(list,getApplicationContext(),"product_list");
                re.setItemAnimator(new DefaultItemAnimator());
                re.setAdapter(recycler);

                progressdialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
    }

    }




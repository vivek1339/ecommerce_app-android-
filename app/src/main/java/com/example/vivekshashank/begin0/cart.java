
package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class cart extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //FirebaseRecyclerAdapter<item,viewHolder> f;
    int count=0;
    int p1;
    private FirebaseAuth fauth;
    RecyclerView re;
    DatabaseReference dbref;
    ArrayList<item> list;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img=(ImageView)findViewById(R.id.image);
        re = (RecyclerView) findViewById(R.id.rcv1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fauth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference().child("users").child(fauth.getCurrentUser().getDisplayName().toString()).child("cart");
        re = (RecyclerView) findViewById(R.id.rcv1);
        re.setLayoutManager(new LinearLayoutManager(this));
        re.setHasFixedSize(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final ProgressDialog progressdialog = new ProgressDialog(cart.this);
        progressdialog.setMessage("loading");
        progressdialog.show();

        dbref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                count=0;
               int price=0;
                // StringBuffer stringbuffer = new StringBuffer();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    item userdetails = dataSnapshot1.getValue(item.class);
                    item listdata = new item();
                    String name = userdetails.getName();
                    int Price = userdetails.getPrice();
                    String url=userdetails.geturl();
                    listdata.setuid(userdetails.getuid());
                    listdata.settype(userdetails.gettype());
                    listdata.setDesc(userdetails.getDesc());
                    listdata.setCreator(userdetails.getCreator());
                    listdata.setName(name);
                    listdata.setPrice(Price);
                    listdata.seturl(url);
                    list.add(listdata);
                    price+=Price;
                    count++;
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                if(count==0)
                {


                    ((TextView)findViewById(R.id.price2)).setVisibility(TextView.GONE);
                }
                else
                {
                    ((ImageView)findViewById(R.id.imageView3)).setVisibility(ImageView.GONE);
                    ((TextView)findViewById(R.id.textView3)).setVisibility(TextView.GONE);
                    ((TextView) findViewById(R.id.price)).setText(String.valueOf(price));
                }
                RecyclerviewAdapter recycler = new RecyclerviewAdapter(list,getApplicationContext(),"cart");
                re.setItemAnimator(new DefaultItemAnimator());
                re.setAdapter(recycler);
                progressdialog.dismiss();
                if(price==0)
                {
                    ((TextView)findViewById(R.id.price)).setVisibility(TextView.GONE);
                }
                ((TextView)findViewById(R.id.price)).setText(String.valueOf(price));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
    }
    public void checkout(View view)
    {
            Intent intent=new Intent(this,checkout.class);
            startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            Intent intent=new Intent(this,cart.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.wishlist) {
            Intent intent=new Intent(this,wishlist.class);
            startActivity(intent);
            finish();
        }  else if (id == R.id.category) {
            Intent intent=new Intent(this,catogery.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.settings) {
            Intent intent=new Intent(this,settings.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.artist)
        {
            Intent intent=new Intent(this,artist_in_you.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

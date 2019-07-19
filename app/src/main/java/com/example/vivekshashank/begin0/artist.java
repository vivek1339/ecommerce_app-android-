
package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class artist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int GALLERY_REQUEST=1;
    ImageView image;
    private StorageReference sref;
    Uri imageuri,downloaduri;
    Button submit;
    String Desc,Name,Price,Creator,Type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText name=(EditText) findViewById(R.id.name);
        EditText price=(EditText)findViewById(R.id.price);
        EditText creator=(EditText)findViewById(R.id.creator);
        EditText desc=(EditText)findViewById(R.id.desc);
        image=(ImageView)findViewById(R.id.image);
        submit=(Button)findViewById(R.id.submit);
        Spinner type=(Spinner)findViewById(R.id.type);
        Type=type.getPrompt().toString();
        Name=name.getText().toString();
        Creator=creator.getText().toString();
        Price=price.getText().toString();
        Desc=desc.getText().toString();
        sref= FirebaseStorage.getInstance().getReference();
        image.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GALLERY_REQUEST);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd=new ProgressDialog(artist.this);
                pd.setMessage("uploading");
                pd.show();
                if(!TextUtils.isEmpty(Name)&&!TextUtils.isEmpty(Desc)&&!TextUtils.isEmpty(Price)&&!TextUtils.isEmpty(Creator))
                {
                    StorageReference sref1=sref.child("uploads").child(imageuri.getLastPathSegment());
                    sref1.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloaduri=taskSnapshot.getDownloadUrl();
                        }

                    });
                    pd.dismiss();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onActivityResult(int req_code,int res_code,Intent data)
    {
        super.onActivityResult(req_code,res_code,data);
        if(req_code==GALLERY_REQUEST&&res_code==RESULT_OK)
        {
            imageuri=data.getData();
            image.setImageURI(imageuri);
        }
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
        } else if (id == R.id.wishlist) {
            Intent intent=new Intent(this,wishlist.class);
            startActivity(intent);



        } else if (id == R.id.category) {
            Intent intent=new Intent(this,catogery.class);
            startActivity(intent);
        }
        else if(id == R.id.settings) {
            Intent intent=new Intent(this,settings.class);
            startActivity(intent);

        }
        else if(id==R.id.artist)
        {
            Intent intent=new Intent(this,artist.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

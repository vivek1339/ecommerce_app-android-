package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class artist_in_you extends AppCompatActivity {
private static final int GALLERY_REQUEST=1;
        private ImageView Image;

private StorageReference sref;
        Uri imageuri=null,downloaduri=null;
        private Button Submit;
        EditText desc,name,price,creator;
        String Desc,Name,Creator,Type,url=null;
        Spinner type;
        ProgressDialog pd;
        Integer Price;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_in_you);

        name=(EditText) findViewById(R.id.name);
        price=(EditText)findViewById(R.id.price);
        creator=(EditText)findViewById(R.id.creator);
        desc=(EditText)findViewById(R.id.desc);
        Image=findViewById(R.id.image);
        Submit=(Button)findViewById(R.id.submit);
        type=(Spinner)findViewById(R.id.type);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        sref= FirebaseStorage.getInstance().getReference();




        }
        public void submit(View view)
        {


                Type=type.getSelectedItem().toString();
                Name=name.getText().toString();
                Creator=creator.getText().toString();
                try {
                        Price = Integer.parseInt(price.getText().toString());
                }
                catch (Exception e)
                {
                        Toast.makeText(this,"enter a proper number",Toast.LENGTH_LONG).show();
                        return;
                }
                Desc=desc.getText().toString();
                if(!TextUtils.isEmpty(Name)&&!TextUtils.isEmpty(Desc)&&Price!=null&&!TextUtils.isEmpty(Creator)&&imageuri!=null)
                {
                        pd.setMessage("uploading");
                        pd.show();
                        StorageReference sref1=sref.child("uploads/"+ UUID.randomUUID());
                        sref1.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        downloaduri=taskSnapshot.getDownloadUrl();
                                        url=downloaduri.toString();
                                        if(url==null)
                                        {
                                                Toast.makeText(artist_in_you.this,"upload failed",Toast.LENGTH_LONG).show();
                                        }
                                        Toast.makeText(artist_in_you.this,"image uploaded",Toast.LENGTH_LONG).show();
                                        Boolean a=addtodb(Name,Type,Creator,Price,Desc,url);
                                        if(a)
                                        {
                                                pd.dismiss();
                                        }
                                }



                        });


                }
                else if(imageuri==null)
                {

                        Toast.makeText(this,"image uri problem",Toast.LENGTH_LONG).show();
                }
                else if(Price==null)
                {
                        Toast.makeText(this,"ensure the price is right",Toast.LENGTH_LONG).show();
                }
                else
                {

                        Toast.makeText(this,"fill all the fields correctly",Toast.LENGTH_LONG).show();
                }
        }

        private Boolean addtodb(String name, String type, String creator,int price, String desc, String url) {
        String uid=UUID.randomUUID().toString();
                        item product=new item(name,type,creator,price,desc,url,uid);
                DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                DatabaseReference db2=db.child("product").child(type);
                DatabaseReference db1=db2.child(uid);
                db1.setValue(product);
                db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("my product").child(uid).setValue(product);
                Toast.makeText(this, "added to market",
                        Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }

        public void addimg(View view)
        {
                Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GALLERY_REQUEST);
        }

@Override
public void onActivityResult(int req_code,int res_code,Intent data)
        {
        super.onActivityResult(req_code,res_code,data);
        if(req_code==GALLERY_REQUEST&&res_code==RESULT_OK&&data.getData()!=null)
        {
        imageuri=data.getData();
        try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                Image.setImageBitmap(bitmap);
        }
        catch (Exception e){}
        Image.setImageURI(imageuri);
        }
        else
        {
                Toast.makeText(this,"upload failed ",Toast.LENGTH_LONG).show();
        }
        }
@Override
public void onBackPressed() {

        super.onBackPressed();
        }


@Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
        }


        }

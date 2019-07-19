package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class product extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseAuth auth;
    int count;
    String url="";
    String p_id,id;
    TextView Name,desc,price,creator;
    ImageView img;
    TouchImageView img2;
    ProgressDialog pd;
    item item1;
    private String fullScreenInd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        img=(ImageView)findViewById(R.id.image);
        Name=(TextView)findViewById(R.id.name);
        creator=(TextView)findViewById(R.id.creator);
        desc=(TextView)findViewById(R.id.desc);
        price=(TextView)findViewById(R.id.price);
        p_id = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        ref=FirebaseDatabase.getInstance().getReference().child("product").child(p_id).child(id);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        pd=new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                item1=dataSnapshot.getValue(item.class);
                Name.setText(item1.getName());
                creator.setText(item1.getCreator());
                price.setText(String.valueOf(item1.getPrice()));
                desc.setText(item1.getDesc());
                Picasso.with(product.this).load(item1.geturl()).into(img);
                pd.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fullScreenInd = getIntent().getStringExtra("fullScreenIndicator");
        if ("y".equals(fullScreenInd)) {

            setContentView(R.layout.imageview);
            img2=(TouchImageView) findViewById(R.id.img2);
            ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    item1=dataSnapshot.getValue(item.class);
                    Name.setText(item1.getName());
                    creator.setText(item1.getCreator());
                    price.setText(String.valueOf(item1.getPrice()));
                    desc.setText(item1.getDesc());
                    Picasso.with(product.this).load(item1.geturl()).into(img2);
                    pd.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
           // img2.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            //img2.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            //img2.setAdjustViewBounds(false);
            //img2.setScaleType(ImageView.ScaleType.FIT_XY);


        }
        else
        {
            ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    String price1="Rs."+String.valueOf(item1.getPrice());
                    item1=dataSnapshot.getValue(item.class);
                    Name.setText(item1.getName());
                    creator.setText(item1.getCreator());
                    price.setText(price1);
                    desc.setText(item1.getDesc());
                    Picasso.with(product.this).load(item1.geturl()).into(img);
                    pd.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(product.this,
                        product.class);

                if("y".equals(fullScreenInd)){
                    intent.putExtra("fullScreenIndicator", "");
                    intent.putExtra("type",p_id);
                    intent.putExtra("id",id);
                }else{
                    intent.putExtra("fullScreenIndicator", "y");
                    intent.putExtra("type",p_id);
                    intent.putExtra("id",id);
                }
                product.this.startActivity(intent);
            }
        });


    }
    public void add(View view)
    {   auth=FirebaseAuth.getInstance();
    String user=auth.getCurrentUser().getDisplayName();
     DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users").child(user).child("cart");
     DatabaseReference db1=db.child(item1.getuid());
     db1.setValue(item1);

        Toast.makeText(product.this, "added to cart.",
                Toast.LENGTH_SHORT).show();



    }
    public void addtow(View view)
    {
        auth=FirebaseAuth.getInstance();
        String user=auth.getCurrentUser().getDisplayName();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users").child(user).child("wishlist");
        DatabaseReference db1=db.push();
        db1.setValue(item1);
        Toast.makeText(product.this, "added to whishlist",
                Toast.LENGTH_SHORT).show();

    }
}

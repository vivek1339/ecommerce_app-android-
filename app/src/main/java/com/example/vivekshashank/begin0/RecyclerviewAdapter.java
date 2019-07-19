package com.example.vivekshashank.begin0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by csa on 3/6/2017.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyHolder> {

    public List<item> listdata;
    Context context;
    ProgressDialog pd;
    int pos;
    String Type,uid,class2;

    public RecyclerviewAdapter(List<item> listdata, Context context,String class2) {
        this.listdata = listdata;
        this.context = context;
        this.class2=class2;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        Toast.makeText(context,context.getClass().getName(),Toast.LENGTH_LONG).show();
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    public void onBindViewHolder(MyHolder holder, final int position) {

       final item data = listdata.get(position);


        holder.name.setText(data.getName());
        holder.price.setText(String.valueOf(data.getPrice()));
        String url = data.geturl();
        uid=data.getuid();
        Type=data.gettype();

        //holder.mview.setOnClickListener(new mylistner(position,context));
        holder.mview.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

               Intent intent=new Intent(context,product.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",data.getuid());
                intent.putExtra("type",data.gettype());
                context.startActivity(intent);


            }
        });
        Picasso.with(context).load(url).into(holder.image);
        if(class2=="product_list")
        {
            holder.delete.setVisibility(ImageButton.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db1;
                int cart_value;
                DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                db1=db.child(class2).child(data.getuid());
                if(class2=="cart")
                {
                    db.child("cart_value").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                db1.removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        View mview;
        Button delete;

        public MyHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            delete=(Button)itemView.findViewById(R.id.delete);
            mview=itemView;
        }
    }


}

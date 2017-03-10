package com.example.lzc.daliylife.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzc.daliylife.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzc on 2016/11/10.
 */
public class RecyclerViewTest extends AppCompatActivity {
    @BindView(R.id.recycler)
    RecyclerView mRecycleView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hello");
        toolbar.setSubtitle("lzc");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("myAction", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RecyclerViewTest.this, "ok", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(new MyRecycleAdapter());
    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {

        private final LayoutInflater mInflater;

        public MyRecycleAdapter() {
            this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @SuppressLint("InflateParams")
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(mInflater.inflate(R.layout.recycler_itemview, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mImageView.setImageResource(R.drawable.ic_menu_share);
            holder.mTextView.setText(getResources().getString(R.string.app_name));
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private final ImageView mImageView;
            private final TextView mTextView;

            public MyViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.mImageView);
                mTextView = (TextView) itemView.findViewById(R.id.mTextView);
            }
        }
    }

}

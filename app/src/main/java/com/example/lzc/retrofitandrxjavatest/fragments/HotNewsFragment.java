package com.example.lzc.retrofitandrxjavatest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lzc.retrofitandrxjavatest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzc on 2017/2/7.
 */

public class HotNewsFragment extends Fragment {
    Unbinder unBinder;
    @BindView(R.id.hot_recycler)
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hot_news_fragment, null);
        unBinder= ButterKnife.bind(this,rootView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HotNewsAdapter());
        return rootView;
    }

    private class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.MyHolder>{

        private LayoutInflater mInflater;
        public HotNewsAdapter() {
            mInflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder myHolder=new MyHolder(mInflater.inflate(R.layout.hot_news_adapter,null));
            return myHolder;
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
                holder.textView.setText("hello recyclerView");
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView textView;

            public MyHolder(View itemView) {
                super(itemView);
                textView= (TextView) itemView.findViewById(R.id.hot_text);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }
}

package com.zhuzichu.library.ui.country.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuzichu.library.R;
import com.zhuzichu.library.dto.DTOCountry;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/9/7.
 */
public class CountryAdapter extends IndexableAdapter<DTOCountry> {
    private LayoutInflater mInflater;

    public CountryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_lable, parent, false);
        return new ViewHolderLable(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_country, parent, false);
        return new ViewHolderCountry(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        ViewHolderLable holderLable=(ViewHolderLable)holder;
        holderLable.mTvTitle.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, DTOCountry entity) {
        ViewHolderCountry holderLable=(ViewHolderCountry)holder;
        holderLable.mTvTitle.setText(entity.getLabel());
        holderLable.mTvCode.setText("+"+entity.getCode());
    }

    private class ViewHolderLable extends RecyclerView.ViewHolder {
        TextView mTvTitle;

        public ViewHolderLable(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    private class ViewHolderCountry extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvCode;

        public ViewHolderCountry(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.item_title);
            mTvCode = (TextView) itemView.findViewById(R.id.item_code);
        }
    }
}

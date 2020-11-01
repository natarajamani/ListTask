package com.example.repositorytask.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.repositorytask.R;
import com.example.repositorytask.model.RepositoryData;
import com.example.repositorytask.utils.LogUtils;


public class ItemAdapter extends PagedListAdapter<RepositoryData, ItemAdapter.RepositoryViewHolder> {

    Context context;

    public ItemAdapter( Context context) {
        super(DIFF_CALLBACK);
        this.context=context;

    }

    private static final DiffUtil.ItemCallback<RepositoryData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RepositoryData>() {
                @Override
                public boolean areItemsTheSame(@NonNull RepositoryData oldItem, @NonNull RepositoryData newItem) {
                    return oldItem.getName() == newItem.getName();
                }

                @Override
                public boolean areContentsTheSame(@NonNull RepositoryData oldItem, @NonNull RepositoryData newItem) {


                    return newItem.getName().equals(oldItem.getName());
                }
            };

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_repo_list, parent, false);

        return new ItemAdapter.RepositoryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        try {
            final RepositoryData data = getItem(position);
            holder.tv_title.setText(data.getName());
            holder.tv_authorname.setText(data.getAuthor());

            holder.tv_star.setText(String.valueOf(data.getStars()));
            holder.tv_language.setText(data.getLanguage());
            holder.iv_language.setColorFilter(Color.parseColor(data.getLanguageColor()));

            if(data.getAvatar()!=null){
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_default)
                        .error(R.drawable.ic_default);

                Glide.with(context).load(data.getAvatar()).apply(options).into(holder.iv_AuthorImage);
            }

            if(data.getDescription()!=null&&data.getDescription().length()>0){
                holder.tv_description.setVisibility(View.VISIBLE);
                holder.tv_description.setText(data.getDescription());
            }else{
                holder.tv_description.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            LogUtils.printException(e);
        }

    }


    public class RepositoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title, tv_description,tv_authorname,tv_language,tv_star;
        public ImageView iv_AuthorImage,iv_star,iv_language;

        public RepositoryViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_description);
            iv_AuthorImage=view.findViewById(R.id.iv_AuthorImage);
            tv_authorname = view.findViewById(R.id.tv_Authorname);
            tv_language = view.findViewById(R.id.tv_language);
            tv_star = view.findViewById(R.id.tv_star);
            iv_star = view.findViewById(R.id.iv_star);
            iv_language = view.findViewById(R.id.iv_language);

        }
    }

}

package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/3/29
 */
public class HouseCollectionAdapter extends RecyclerView.Adapter<HouseCollectionAdapter.EstatesViewHolder> {
    private Context context;
    private List mList;

    public HouseCollectionAdapter(Context context, List list) {
        this.context = context;
        this.mList = list;
//        LayoutInflater.from(context);
    }

    @Override
    public EstatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * View view = View.inflate(context, R.layout.item_articles_list, null);
         return new ArticlesListViewHolder(view);
         */
        return null;
    }

    @Override
    public void onBindViewHolder(EstatesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    class EstatesViewHolder extends RecyclerView.ViewHolder {

        public EstatesViewHolder(View itemView) {
            super(itemView);

        }
    }
}

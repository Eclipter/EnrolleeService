package by.bsu.dekrat.enrolleeservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.bsu.dekrat.enrolleeservice.entity.NewsContent;

public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder> {

    private final List<NewsContent.NewsItem> mValues;

    public MyNewsRecyclerViewAdapter(List<NewsContent.NewsItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView newsTitleTextView;
        final TextView newsContentTextView;
        final TextView newsDateTextView;
        NewsContent.NewsItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            newsTitleTextView = (TextView) view.findViewById(R.id.newsTitleTextView);
            newsContentTextView = (TextView) view.findViewById(R.id.newsContentTextView);
            newsDateTextView = (TextView) view.findViewById(R.id.newsDateTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + newsContentTextView.getText() + "'";
        }
    }
}

package by.bsu.dekrat.enrolleeservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.bsu.dekrat.enrolleeservice.entity.TestAssignment;

/**
 * Created by USER on 21.11.2016.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private List<TestAssignment> assignments;

    public TestListAdapter(List<TestAssignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.testAssignment = assignments.get(position);
        holder.textView.setText(assignments.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textView;
        TestAssignment testAssignment;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            textView = (TextView) view.findViewById(R.id.testInfoTextView);
        }
    }
}

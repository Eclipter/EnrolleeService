package by.bsu.dekrat.enrolleeservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import by.bsu.dekrat.enrolleeservice.entity.TestAssignment;

/**
 * Created by USER on 21.11.2016.
 */

public class TestAssignmentListAdapter extends RecyclerView.Adapter<TestAssignmentListAdapter.ViewHolder> {

    private Random random = new Random();
    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm";
    private static final String TIME_ZONE = "UTC";

    private List<TestAssignment> assignments;

    public TestAssignmentListAdapter(List<TestAssignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public TestAssignmentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TestAssignment assignment = assignments.get(position);
        holder.testAssignment = assignment;
        holder.testInfoPrimaryTextView.setText(String.format("%s: %s (%s)", assignment.getTest().getType(),
                assignment.getTest().getSubject().getName(), assignment.getTest().getSubject().getCode()));
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        holder.testInfoDateTextView.setText(format.format(assignment.getTest().getDate()));
        holder.testInfoPlaceTextView.setText(String.format("%s (%s), ауд. %s",
                assignment.getTest().getRoom().getUniversity().getName(),
                assignment.getTest().getRoom().getUniversity().getAddress(),
                assignment.getTest().getRoom().getNumber()));

        if(assignment.getPoints() != 0) {
            holder.testInfoPointsTextView.setText(assignment.getPoints());
        } else {
            holder.testInfoPointsTextView.setText("Н/Д");
            holder.testInfoPointsTextView.setAlpha(0.2f);
        }

        holder.testPaymentTextView.setText(
                String.format("Номер заказа в ЕРИП: %d", random.nextInt(1000000)));
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView testInfoPrimaryTextView;
        TextView testInfoDateTextView;
        TextView testInfoPlaceTextView;
        TextView testInfoPointsTextView;
        TextView testPaymentTextView;
        TestAssignment testAssignment;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            testInfoPrimaryTextView = (TextView) view.findViewById(R.id.testInfoPrimaryText);
            testInfoDateTextView = (TextView) view.findViewById(R.id.testDateTextView);
            testInfoPlaceTextView = (TextView) view.findViewById(R.id.testInfoPlaceTextView);
            testInfoPointsTextView = (TextView) view.findViewById(R.id.pointsTextView);
            testPaymentTextView = (TextView) view.findViewById(R.id.testPaymentTextView);
        }
    }
}

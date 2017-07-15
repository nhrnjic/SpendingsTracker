package com.example.nihadhrnjic.spendingstracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.SpendingsGoal;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by nihadhrnjic on 7/6/17.
 */

public class GoalsFragment extends Fragment {

    private RecyclerView mGoalsList;
    private Realm mRealm;
    private GoalAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        mAdapter = new GoalAdapter();
        mGoalsList = (RecyclerView) view.findViewById(R.id.goals_list_id);
        mGoalsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGoalsList.setAdapter(mAdapter);

        return view;
    }

    public void setTitle(){
        getActivity().setTitle("All Goals");
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder{

        private TextView mGoalAmount;
        private TextView mMonth;

        public GoalViewHolder(View view){
            super(view);
            mGoalAmount = (TextView) view.findViewById(R.id.goal_amount_id);
            mMonth = (TextView) view.findViewById(R.id.goal_month_id);
        }

        public void setupItem(SpendingsGoal goal){
            mGoalAmount.setText(goal.Amount + " KM");
            mMonth.setText(goal.getDate().toString("MMMM,yyyy"));
        }
    }

    public class GoalAdapter extends RecyclerView.Adapter<GoalViewHolder>{

        private RealmResults<SpendingsGoal> mGoals;

        public GoalAdapter(){
            mGoals = mRealm.where(SpendingsGoal.class).findAll();
            mGoals.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SpendingsGoal>>() {
                @Override
                public void onChange(RealmResults<SpendingsGoal> spendingsGoals, OrderedCollectionChangeSet changeSet) {
                    if(changeSet == null){
                        return;
                    }

                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        notifyGoalUpdated(range.startIndex, range.length);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRealm.where(SpendingsGoal.class).findAll().size();
        }

        @Override
        public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.goal_list_item, parent, false);
            return new GoalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GoalViewHolder holder, int position) {

            SpendingsGoal goal = mGoals.get(position);
            holder.setupItem(goal);
        }

        public void notifyGoalUpdated(int start, int length){
            for(int i = start; i < length; i++){
                mAdapter.notifyItemChanged(start);
            }
        }
    }
}


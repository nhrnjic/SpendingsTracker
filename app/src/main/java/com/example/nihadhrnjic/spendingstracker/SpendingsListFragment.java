package com.example.nihadhrnjic.spendingstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.Category;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by nihadhrnjic on 6/21/17.
 */

public class SpendingsListFragment extends Fragment {

    private RecyclerView mSpendingsList;
    private FloatingActionButton mNewItem;

    private Realm mRealmInstance;
    private SpendingsItemAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealmInstance = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spendings_list, container, false);

        mAdapter = new SpendingsItemAdapter();
        mSpendingsList = (RecyclerView) view.findViewById(R.id.spendings_list_id);
        mSpendingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpendingsList.setAdapter(mAdapter);

        mNewItem = (FloatingActionButton) view.findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    public class SpendingsItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mItemName;
        private TextView mItemPrice;
        private TextView mItemCategory;
        private TextView mItemDate;

        public SpendingsItemViewHolder(View view){
            super(view);

            mItemName = (TextView) view.findViewById(R.id.spendings_item_name);
            mItemPrice = (TextView) view.findViewById(R.id.spendings_item_amount);
            mItemCategory = (TextView) view.findViewById(R.id.spendings_item_category);
            mItemDate = (TextView) view.findViewById(R.id.spendings_item_date);
        }

        public void setupModel(SpendingsItem item){
            mItemName.setText(item.Name);
            mItemPrice.setText("Amount: "+ item.Amount + " KM");
            mItemCategory.setText(item.Category.Name);
            mItemDate.setText("Date spent:   "+item.getDate().toString("dd/MM/yyyy"));
        }
    }

    public class SpendingsItemAdapter extends RecyclerView.Adapter<SpendingsItemViewHolder>{

        @Override
        public int getItemCount() {
            return mRealmInstance.where(SpendingsItem.class).findAll().size();
        }

        @Override
        public SpendingsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.spendings_item, parent, false);

            return new SpendingsItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SpendingsItemViewHolder holder, int position) {

            SpendingsItem item = mRealmInstance.where(SpendingsItem.class)
                    .findAllSorted(new String[]{ "Date", "Amount" }, new Sort[]{ Sort.DESCENDING, Sort.DESCENDING })
                    .get(position);

            holder.setupModel(item);
        }
    }

    public void updateList(){
        mAdapter.notifyDataSetChanged();
    }

    public void setTitle(){
        getActivity().setTitle("All Spendings");
    }
}

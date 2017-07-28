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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nihadhrnjic.spendingstracker.models.Category;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsGoal;
import com.example.nihadhrnjic.spendingstracker.models.SpendingsItem;

import java.util.ArrayList;
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
    private List<SpendingsItem> mItemsForDeletion;
    private boolean mShowSpendingCheckbox;
    private MenuItem mDeleteItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealmInstance = Realm.getDefaultInstance();
        mItemsForDeletion = new ArrayList<>();
        mShowSpendingCheckbox = false;
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_spendings, menu);
        mDeleteItem = menu.findItem(R.id.delete_item_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_item_id:
                mRealmInstance.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for(SpendingsItem spendingsItem: mItemsForDeletion){
                            float currentAmount = LocalPreferences.getFloat(getActivity(), getString(R.string.pref_money));
                            LocalPreferences.saveFloat(getActivity(), getString(R.string.pref_money), currentAmount + (float)spendingsItem.Amount);
                            spendingsItem.deleteFromRealm();
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;
        }

        return true;
    }

    public class SpendingsItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener{

        SpendingsItem mSpendingsItem;
        private TextView mItemName;
        private TextView mItemPrice;
        private TextView mItemCategory;
        private TextView mItemDate;
        private CheckBox mItemChecked;

        @Override
        public void onClick(View v) {
            if(mShowSpendingCheckbox){
                mShowSpendingCheckbox = false;
                mItemsForDeletion.clear();
                mItemChecked.setVisibility(View.INVISIBLE);
                mDeleteItem.setVisible(false);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(!mShowSpendingCheckbox){
                mShowSpendingCheckbox = true;
                mItemChecked.setVisibility(View.VISIBLE);
                mDeleteItem.setVisible(true);
                mAdapter.notifyDataSetChanged();
                return false;
            }else{
                return false;
            }
        }

        public SpendingsItemViewHolder(View view){
            super(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            mItemName = (TextView) view.findViewById(R.id.spendings_item_name);
            mItemPrice = (TextView) view.findViewById(R.id.spendings_item_amount);
            mItemCategory = (TextView) view.findViewById(R.id.spendings_item_category);
            mItemDate = (TextView) view.findViewById(R.id.spendings_item_date);
            mItemChecked = (CheckBox) view.findViewById(R.id.spendings_delete_id);

            mItemChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        addItemForDeletion(mSpendingsItem);
                    }else{
                        removeItemForDeletion(mSpendingsItem);
                    }
                }
            });
        }

        public void setupModel(SpendingsItem item){
            mSpendingsItem = item;
            mItemName.setText(item.Name);
            mItemPrice.setText(item.Amount + " KM");
            mItemCategory.setText(item.Category.Name);
            mItemDate.setText(item.getDate().toString("dd/MM/yyyy"));
            mItemChecked.setChecked(false);

            if(mShowSpendingCheckbox){
                mItemChecked.setVisibility(View.VISIBLE);
            }else{
                mItemChecked.setVisibility(View.INVISIBLE);
            }
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

    private void addItemForDeletion(SpendingsItem item){
        mItemsForDeletion.add(item);
    }

    private void removeItemForDeletion(SpendingsItem item){
        for(int i = 0; i < mItemsForDeletion.size(); i++){
            if(mItemsForDeletion.get(i).equals(item)){
                mItemsForDeletion.remove(i);
            }
        }
    }

}

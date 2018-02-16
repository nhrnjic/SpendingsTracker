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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by nihadhrnjic on 6/21/17.
 */

public class SpendingsListFragment extends Fragment {

    private RecyclerView mSpendingsList;

    private Realm mRealmInstance;
    private SpendingsItemAdapter mAdapter;
    private List<String> mItemsForDeletion;
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
                        for(String spendingsItemId: mItemsForDeletion){
                            SpendingsItem spendingsItem = getItemById(spendingsItemId);
                            if(spendingsItem != null){
                                float currentAmount = LocalPreferences.getFloat(getActivity(), getString(R.string.pref_money));
                                LocalPreferences.saveFloat(getActivity(), getString(R.string.pref_money), currentAmount + (float)spendingsItem.Amount);
                                spendingsItem.deleteFromRealm();
                            }
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
            mItemDate = (TextView) view.findViewById(R.id.spendings_item_date);
            mItemChecked = (CheckBox) view.findViewById(R.id.spendings_delete_id);

            mItemChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        addItemForDeletion(mSpendingsItem.Id);
                    }else{
                        removeItemForDeletion(mSpendingsItem.Id);
                    }
                }
            });
        }

        public void setupModel(SpendingsItem item){
            mSpendingsItem = item;
            mItemName.setText(item.Name);
            mItemPrice.setText(item.Amount + " KM");
            mItemDate.setText(item.getDate().toString("dd/MM/yyyy"));
            mItemChecked.setChecked(false);

            if(mShowSpendingCheckbox){
                mItemChecked.setVisibility(View.VISIBLE);
            }else{
                mItemChecked.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class SpendingsItemGroupDividerViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener{

        SpendingsItem mSpendingsItem;
        private TextView mItemName;
        private TextView mItemPrice;
        private TextView mItemCategory;
        private TextView mItemDate;
        private TextView mGroupTitle;
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

        public SpendingsItemGroupDividerViewHolder(View view){
            super(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            mItemName = (TextView) view.findViewById(R.id.spendings_item_name);
            mItemPrice = (TextView) view.findViewById(R.id.spendings_item_amount);
            mItemDate = (TextView) view.findViewById(R.id.spendings_item_date);
            mItemChecked = (CheckBox) view.findViewById(R.id.spendings_delete_id);
            mGroupTitle = (TextView) view.findViewById(R.id.group_title_id);

            mItemChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        addItemForDeletion(mSpendingsItem.Id);
                    }else{
                        removeItemForDeletion(mSpendingsItem.Id);
                    }
                }
            });
        }

        public void setupModel(SpendingsItem item, int viewType){
            mSpendingsItem = item;
            mItemName.setText(item.Name);
            mItemPrice.setText(item.Amount + " KM");
            mItemDate.setText(item.getDate().toString("dd/MM/yyyy"));
            mItemChecked.setChecked(false);
            mGroupTitle.setText(getGroupName(viewType));

            if(mShowSpendingCheckbox){
                mItemChecked.setVisibility(View.VISIBLE);
            }else{
                mItemChecked.setVisibility(View.INVISIBLE);
            }
        }

        private String getGroupName(int viewType){
            switch (viewType){
                case 0: return "Today";
                case 1: return "Yesterday";
                case 2: return "Older";
                default: return "";
            }
        }
    }

    public class SpendingsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<SpendingsItem> mItems = null;
        private List<SpendingsItem> mGroupItems = null;

        public SpendingsItemAdapter(){
            mItems = mRealmInstance.where(SpendingsItem.class)
                    .findAllSorted(new String[]{ "Date", "Amount" }, new Sort[]{ Sort.DESCENDING, Sort.DESCENDING });
            mGroupItems = getStartItemsForGroup();
        }

        @Override
        public int getItemCount() {
            return mRealmInstance.where(SpendingsItem.class).findAll().size();
        }

        @Override
        public int getItemViewType(int position) {
            SpendingsItem item = mItems.get(position);
            return mGroupItems.indexOf(item);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if(viewType == -1){
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.spendings_item, parent, false);
                return new SpendingsItemViewHolder(view);
            }else {
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.spending_item_group_first, parent, false);
                return new SpendingsItemGroupDividerViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SpendingsItem item = mItems.get(position);
            int viewType = getItemViewType(position);

            if(viewType == -1){
                SpendingsItemViewHolder viewHolder = (SpendingsItemViewHolder)holder;
                viewHolder.setupModel(item);
            }else{
                SpendingsItemGroupDividerViewHolder viewHolder = (SpendingsItemGroupDividerViewHolder) holder;
                viewHolder.setupModel(item, viewType);
            }
        }
    }

    public void updateList(){
        mAdapter.notifyDataSetChanged();
    }

    public void setTitle(){
        getActivity().setTitle("All Spendings");
    }

    private SpendingsItem getItemById(String id){
        return mRealmInstance.where(SpendingsItem.class)
                .equalTo("Id", id).findFirst();
    }

    private void addItemForDeletion(String id){
        mItemsForDeletion.add(id);
    }

    private void removeItemForDeletion(String id){
        for(int i = 0; i < mItemsForDeletion.size(); i++){
            if(mItemsForDeletion.get(i).equals(id)){
                mItemsForDeletion.remove(i);
            }
        }
    }

    private List<SpendingsItem> getStartItemsForGroup(){

        List<SpendingsItem> startGroupItems = new ArrayList<>();
        DateTime begin = new DateTime().withTime(0,0,0,0);
        DateTime end = new DateTime().withTime(23,59,59,59);

        for(int i = 0; i < 3; i++){

            List<SpendingsItem> result = mRealmInstance.where(SpendingsItem.class)
                    .between("Date", begin.toDate(), end.toDate())
                    .findAllSorted(new String[]{ "Date", "Amount" }, new Sort[]{ Sort.DESCENDING, Sort.DESCENDING });

            if(result.size() > 0){
                startGroupItems.add(result.get(0));
            }

            begin = begin.minusDays(1);
            end = end.minusDays(1);
        }

        return startGroupItems;
    }
}

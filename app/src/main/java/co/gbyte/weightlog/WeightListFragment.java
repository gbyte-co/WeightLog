package co.gbyte.weightlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;

/**
 * Created by walt on 19/10/16.
 *
 */
public class WeightListFragment extends Fragment {

    private RecyclerView mWeightRecycleView;
    private WeightAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_list, container, false);
        mWeightRecycleView = (RecyclerView) view.findViewById(R.id.weight_recycler_view);
        mWeightRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weight_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_weight:
                Intent intent = WeightActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        WeightLab weightLab = WeightLab.get(getActivity());
        List<Weight> weights = weightLab.getWeights();

        if (mAdapter == null) {
            mAdapter = new WeightAdapter(weights);
            mWeightRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setWeights(weights);
            mAdapter.notifyDataSetChanged();
            // ToDo: Don't forget to switch to:
            //mAdapter.notifyItemChanged(<position>);
            //
            // Note:
            // The challenge is discovering which position has
            // changed and reloading the correct item.
        }
    }

    private class WeightHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Weight mWeight;

        TextView mDateTextView;
        TextView mTimeTextView;
        TextView mWeightTextView;

        WeightHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_weight_date_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_weight_time_text_view);
            mWeightTextView =
                    (TextView) itemView.findViewById(R.id.list_item_weight_weight_text_view);
        }

        void bindWeight(Weight weight) {
            mWeight = weight;
            mDateTextView.setText(DateFormat.getDateFormat(getActivity())
                                  .format(mWeight.getTime()));
            mTimeTextView.setText(DateFormat.getTimeFormat(getActivity())
                                  .format(mWeight.getTime()));
            mWeightTextView.setText(mWeight.getWeightString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = WeightActivity.newIntent(getActivity(), mWeight.getId());
            startActivity(intent);
        }
    }

    private class WeightAdapter extends RecyclerView.Adapter<WeightHolder> {
        private List<Weight> mWeights;

        WeightAdapter(List<Weight> weights) {
            mWeights = weights;
        }

        @Override
        public WeightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_weight, parent, false);
            return new WeightHolder(view);
        }

        @Override
        public void onBindViewHolder(WeightHolder holder, int position) {
            Weight weight = mWeights.get(position);
            holder.bindWeight(weight);
        }

        @Override
        public int getItemCount() {
            return mWeights.size();
        }

        void setWeights(List<Weight> weights) {
            mWeights = weights;
        }
    }
}

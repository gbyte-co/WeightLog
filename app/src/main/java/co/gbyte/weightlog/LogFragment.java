package co.gbyte.weightlog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;
import co.gbyte.weightlog.utils.Bmi;
import co.gbyte.weightlog.utils.Time;

/**
 * Created by walt on 19/10/16.
 *
 */
public class LogFragment extends Fragment {

    private RecyclerView mWeightRecycleView;
    private WeightAdapter mAdapter;
    private Context mContext;
    private Weight mWeight = null;
    private Menu mMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_weight_log, container, false);
        mWeightRecycleView = (RecyclerView) view.findViewById(R.id.weight_recycler_view);
        mWeightRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        FloatingActionButton addFab =
                (FloatingActionButton) this.getActivity().findViewById(R.id.fab_new_weight);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = WeightActivity.newIntent(mContext);
                startActivity(intent);
            }
        });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(!settings.contains(getString(R.string.bmi_pref_key))) {
            // The app is running for the first time. Ask user for basic settings.
            showSettings();
        }

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
        inflater.inflate(R.menu.fragment_log_menu, menu);
        mMenu = menu;
        updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                showSettings();
                return true;
            case R.id.menu_item_edit_weight:
                if (mWeight != null) {
                    Intent intent = WeightActivity.newIntent(mContext, mWeight.getId());
                    startActivity(intent);
                }
                return true;
            case R.id.menu_item_test:
                FractionPartPickerTestDialog testDialog = new FractionPartPickerTestDialog();
				testDialog.show(getActivity().getFragmentManager(), "Test Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
        Intent intent = new Intent(mContext, SettingsActivity.class);
        startActivity(intent);
    }

    private void updateUI() {
        WeightLab weightLab = WeightLab.get(mContext);
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

    private void updateMenu () {
        MenuItem editMenuItem = mMenu.findItem(R.id.menu_item_edit_weight);
        editMenuItem.setVisible(mWeight != null);
    }

    private class WeightHolder extends RecyclerView.ViewHolder {

        TextView mDateCompactTV;
        TextView mDateExtendedTV;
        //TextView mTimeCompactTV;
        TextView mTimeExtendedTV;

        TextView mWeightCompactTV;
        TextView mWeightExtendedTV;

        TextView mWeightChangeCompactTV;
        TextView mWeightChangeExtendedTV;

        RelativeLayout mCompactLayout;
        LinearLayout mExtendedLayout;

        LinearLayout mExtendedWeightChangeView;

        ImageView mCompactNoteIcon;
        TextView mExtendedNote;

        WeightHolder(View itemView) {
            super(itemView);

            mCompactLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_compact);
            mExtendedLayout = (LinearLayout) itemView.findViewById(R.id.list_item_extended);

            mDateCompactTV = (TextView) itemView.findViewById(R.id.weight_date_compact_tv);
            mDateExtendedTV = (TextView) itemView.findViewById(R.id.weight_date_extended_tv);

            mTimeExtendedTV = (TextView) itemView.findViewById(R.id.weight_time_extended_tv);

            mWeightCompactTV = (TextView) itemView.findViewById(R.id.weight_compact_tv);
            mWeightExtendedTV = (TextView) itemView.findViewById(R.id.weight_extended_tv);

            mWeightChangeCompactTV =
                    (TextView) itemView.findViewById(R.id.weight_change_compact_tv);
            mWeightChangeExtendedTV =
                    (TextView) itemView.findViewById(R.id.weight_log_extended_change_tv);

            mExtendedWeightChangeView =
                    (LinearLayout) itemView.findViewById(R.id.weight_log_extended_change_view);

            mCompactNoteIcon = (ImageView) itemView.findViewById(R.id.compat_note_icon);
            mExtendedNote = (TextView) itemView.findViewById(R.id.weight_note_extended_tv);
        }

        void bindWeight(Weight weight, Double weightChange) {

            Date weightTime = weight.getTime();
            mDateCompactTV.setText(Time.getDateString(mContext, "", weightTime));
            mDateExtendedTV.setText(Time.getShortDateString(mContext, weightTime));

            String time = Time.getTimeString(mContext, weightTime);
            //mTimeCompactTV.setText(time);
            mTimeExtendedTV.setText(time);

            mWeightCompactTV.setText(weight.getWeightStringKg());
            mWeightExtendedTV.setText(weight.getWeightStringKg());

            String note = weight.getNote();
            if(note == null || note.isEmpty()) {
                mCompactNoteIcon.setImageBitmap(null);
                mExtendedNote.setVisibility(View.GONE);
            } else {
                mCompactNoteIcon.setImageResource(R.drawable.ic_icon_note_dark);
                mExtendedNote.setText(note);
                mExtendedNote.setVisibility(View.VISIBLE);
            }

            if (weightChange != null) {
                mExtendedWeightChangeView.setVisibility(View.VISIBLE);
                if (weightChange < 0) {
                    mWeightChangeCompactTV.setText(
                            String.format(Locale.getDefault(), "%.1f", weightChange));
                    mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightLoss));
                    mWeightChangeExtendedTV.setText(
                            String.format(Locale.getDefault(), "%.1f", weightChange));
                    mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightLoss));
                    mWeightCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightLossDark));
                    mWeightExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightLossDark));
                } else if (weightChange > 0) {
                    mWeightChangeCompactTV.setText(
                            String.format(Locale.getDefault(), "+%.1f", weightChange));
                    mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightGain));
                    mWeightChangeExtendedTV.setText(
                            String.format(Locale.getDefault(), "+%.1f", weightChange));
                    mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightGain));
                    mWeightCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightGainDark));
                    mWeightExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorWeightGainDark));
                } else {
                    mWeightChangeCompactTV.setText(
                            String.format(Locale.getDefault(), "%.1f", weightChange));
                    mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorSecondaryText));
                    mWeightChangeExtendedTV.setText(
                            String.format(Locale.getDefault(), "%.1f", weightChange));
                    mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorSecondaryText));
                    mWeightCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorSecondaryText));
                    mWeightExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.colorSecondaryText));
                }
            } else {
                mWeightChangeCompactTV.setText("");
                mExtendedWeightChangeView.setVisibility(View.GONE);
                mWeightCompactTV.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorPrimaryText));
                mWeightExtendedTV.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorPrimaryText));
            }

        }
    }

    private class WeightAdapter extends RecyclerView.Adapter<WeightHolder> {
        private List<Weight> mWeights;
        private int mSelectedPos = -1;

        WeightAdapter(List<Weight> weights) {
            mWeights = weights;
        }

        @Override
        public WeightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.list_item_weight, parent, false);

            return new WeightHolder(view);
        }

        @Override
        public void onBindViewHolder(WeightHolder holder, final int position) {
            final Weight weight = mWeights.get(position);
            Double difference;
            if (position < mWeights.size() - 1) {
                Weight prevWeight = mWeights.get(position + 1);
                difference =  (double) (weight.getWeight() - prevWeight.getWeight()) / 1000.0;
            } else {
                difference = null;
            }

            RelativeLayout compactLayout =
                    (RelativeLayout) holder.itemView.findViewById(R.id.list_item_compact);
            LinearLayout extendedLayout =
                    (LinearLayout) holder.itemView.findViewById(R.id.list_item_extended);
            Bmi.updateAssessmentView(mContext,
                    holder.itemView,
                    R.id.assessment_compact_layout,
                    R.id.bmi_compact_tv,
                    weight.bmi(),
                    false);
            Bmi.updateAssessmentView(mContext,
                    holder.itemView,
                    R.id.assessment_compact_layout,
                    R.id.bmi_compact_tv,
                    weight.bmi(),
                    false);

            if (mSelectedPos == position){
                compactLayout.setVisibility(View.GONE);
                extendedLayout.setVisibility(View.VISIBLE);

                Bmi.updateAssessmentView(mContext,
                        holder.itemView,
                        R.id.assessment_extended_layout,
                        R.id.bmi_extended_tv,
                        weight.bmi(),
                        true);
                // ToDo: Why do I have to call it twice to make it work ??
                Bmi.updateAssessmentView(mContext,
                        holder.itemView,
                        R.id.assessment_extended_layout,
                        R.id.bmi_extended_tv,
                        weight.bmi(),
                        true);
            }else{
                compactLayout.setVisibility(View.VISIBLE);
                extendedLayout.setVisibility(View.GONE);
            }
            holder.itemView.setSelected(mSelectedPos == position);
            mWeight = mSelectedPos == -1 ? null : mWeights.get(mSelectedPos);

            holder.bindWeight(weight, difference);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.isSelected()) {
                        notifyItemChanged(mSelectedPos);
                        mSelectedPos = -1;
                        mWeight = null;
                        notifyItemChanged(mSelectedPos);
                    } else {
                        notifyItemChanged(mSelectedPos);
                        mSelectedPos = position;
                        mWeight = weight;
                        notifyItemChanged(mSelectedPos);
                    }
                    updateMenu();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mWeights.size();
        }

        private void setWeights(List<Weight> weights) {
            mWeights = weights;
        }
    }
}

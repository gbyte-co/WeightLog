package co.gbyte.weightlog;


import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {


    private LineChart mLineChart = null;

    // Required empty public constructor
    public ChartFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        mLineChart  = (LineChart) v.findViewById(R.id.lineChart);

        mLineChart.setData(updateChart(mLineChart));
        mLineChart.invalidate();

        return v;
    }

    private LineData  updateChart(LineChart chart) {
        List<Weight> weights = WeightLab.get(getContext()).getWeights();
        Weight weightArray[] = new Weight[weights.size()];
        weightArray = weights.toArray(weightArray);

        List<Entry>  weightEntries = new ArrayList<>();

        for (int i = weightArray.length - 1 ; i >= 0; i--) {
            Date time = weightArray[i].getTime();
            float interval = time.getTime() / 1000;
            Entry entry = new Entry( interval, (float) weights.get(i).getWeight() / 1000);
            weightEntries.add(entry);
        }

        LineDataSet weightDataSet = new LineDataSet(weightEntries, "Weight");
        weightDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        weightDataSet.setColor(Color.BLUE);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(weightDataSet);

        return new LineData(weightDataSet);
    }
}

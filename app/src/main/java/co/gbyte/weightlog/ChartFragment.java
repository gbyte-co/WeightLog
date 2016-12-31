package co.gbyte.weightlog;


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

        WeightLab weightLab = WeightLab.get(getActivity());
        List<Weight> weightList = weightLab.getWeights();

        Weight weightArray[] = new Weight[weightList.size()];
        weightArray = weightList.toArray(weightArray);


        List<Entry>  weightEntries = new ArrayList<>();
        for (int i = weightArray.length - 1 ; i >= 0; i--) {
            Date time = weightArray[i].getTime();
            float interval = time.getTime() / 1000;
            Entry entry = new Entry( interval, (float) weightList.get(i).getWeight() / 1000);
            weightEntries.add(entry);
        }

        LineDataSet weights = new LineDataSet(weightEntries, "Weight");
        weights.setAxisDependency(YAxis.AxisDependency.RIGHT);
        weights.setColor(Color.BLUE);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(weights);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();

        return v;
    }

}

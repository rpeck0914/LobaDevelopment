package rpeck.loba_login_register;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarDetailsFragment extends Fragment {

    private RecyclerView mBarRecyclerView;
    private BarAdapter mAdapter;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int timerCounter;

    final Handler handler = new Handler();

    CityStateSpinnerFragment mCityStateSpinnerFragment = new CityStateSpinnerFragment();

    public BarDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_details, container, false);

        mBarRecyclerView = (RecyclerView) v.findViewById(R.id.bar_recycler_view);
        mBarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        startTimer();
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void startTimer() {
        mTimer = new Timer();
        initializeTimerTask();
        timerCounter = 0;
        mTimer.schedule(mTimerTask, 3000, 1000);
    }

    public void stopTimerTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void initializeTimerTask() {
        mTimerTask = new TimerTask(){
            public void run() {
                handler.post(new Runnable(){
                    public void run() {
                        updateUI();
                        timerCounter++;
                        if(timerCounter > 4) {
                            stopTimerTask();
                        }
                    }
                });
            }
        };
    }

    public void updateUI() {
        BarLab barLab = BarLab.get(getActivity());
        List<BarIDs> barids = barLab.getBars();

        mAdapter = new BarAdapter(barids);
        mBarRecyclerView.setAdapter(mAdapter);
    }

    private class BarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBarNameTextView;
        //private int mSelectedBarID;

        private BarIDs mBarIDs;

        public BarHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            
            mBarNameTextView = (TextView) itemView.findViewById(R.id.bar_name);
        }

        public void bindBar(BarIDs barIDs) {
            mBarIDs = barIDs;

            mBarNameTextView.setText(mBarIDs.mBarNames);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class BarAdapter extends RecyclerView.Adapter<BarHolder> {

        private List<BarIDs> mBarIds;

        public BarAdapter(List<BarIDs> barID) {
            mBarIds = barID;
        }

        @Override
        public BarHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_bar, viewGroup, false);

            return new BarHolder(v);
        }

        @Override
        public void onBindViewHolder(BarHolder barHolder, int i) {
            BarIDs barid = mBarIds.get(i);
            barHolder.bindBar(barid);
        }

        @Override
        public int getItemCount() {
            return mBarIds.size();
        }
    }
}

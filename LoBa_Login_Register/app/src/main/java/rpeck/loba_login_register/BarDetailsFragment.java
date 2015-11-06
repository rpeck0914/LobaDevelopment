package rpeck.loba_login_register;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarDetailsFragment extends Fragment {

    private RecyclerView mBarRecyclerView;

    private BarAdapter mAdapter;

    public BarDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_details, container, false);

        mBarRecyclerView = (RecyclerView) v.findViewById(R.id.bar_recycler_view);

        mBarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        //// TODO: 11/5/2015 Get This Finished and Working

        //BarLab barLab = BarLab.get(getActivity());
        //List<Bar> bars = barLab.getBars();
        //mAdapter = new BarAdapter(bars);
        //mBarRecyclerView.setAdapter(mAdapter);

    }

    private class BarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBarNameTextView;
        private int mSelectedBarID;

        //private Bar mBar

        public BarHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            
            mBarNameTextView = (TextView) itemView.findViewById(R.id.bar_name);
        }
        
        //// TODO: 11/5/2015 need to get a bar class and bar singleton made.
        public void bindBar(/** Bar bar */) {
            //mBar = bar;

            //mBarNameTextView.setText(mBar.getName());
        }

        @Override
        public void onClick(View v) {
            //mSelectedBarID = mBar.getName();
        }

        public int getSelectedBarID() {
            return mSelectedBarID;
        }
    }

    private class BarAdapter extends RecyclerView.Adapter<BarHolder> {

        //private List<Bar> mBars;

        public BarAdapter(/**List<Bar> bars*/) {
            //mBars = bars;
        }

        @Override
        public BarHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_bar, viewGroup, false);

            return new BarHolder(v);
        }

        @Override
        public void onBindViewHolder(BarHolder barHolder, int i) {
            //Bar bar = mBars.get(position);
            //barHolder.bindBar(bar);
        }

        @Override
        public int getItemCount() {
            //return mbars.size();
            return 0;
        }


    }
}

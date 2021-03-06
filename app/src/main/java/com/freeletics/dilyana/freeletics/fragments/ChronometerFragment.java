package com.freeletics.dilyana.freeletics.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.freeletics.dilyana.freeletics.R;
import com.freeletics.dilyana.freeletics.model.actions.Action;
import com.freeletics.dilyana.freeletics.model.users.User;
import com.freeletics.dilyana.freeletics.model.users.UsersManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChronometerFragment extends Fragment {


    private Chronometer chronometer;
    private Button start;
    private Button stop;
    private Button restart;
    private Button count;
    private TextView counerTv;
    private static int counter;
    private Button finishExcercise;
    private long timeWhenStopped = 0;
    private long timeOfExercise;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chronometer, container, false);

        counter=1;
        chronometer = (Chronometer) root.findViewById(R.id.chronometer);
        start = (Button) root.findViewById(R.id.start_button);
        stop = (Button) root.findViewById(R.id.stop_button);
        restart = (Button) root.findViewById(R.id.restart_button);
        count = (Button) root.findViewById(R.id.count_button);
        counerTv = (TextView) root.findViewById(R.id.counter_tv);
        finishExcercise = (Button) root.findViewById(R.id.finish_button);

        chronometer.start();

        UsersManager manager = UsersManager.getInstance();
        final User user = manager.getLoggedUser();
        final boolean[] flag = {false};
        final boolean[] flag2 = {false};

        timeOfExercise = SystemClock.elapsedRealtime() - chronometer.getBase();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2[0] == false) {
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    flag[0] = false;
                    flag2[0] = true;
                    showElapsedTime();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag[0] ==false) {
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    flag[0] =true;
                    flag2[0] = false;
                    showElapsedTime();
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped=0;
                flag[0]=false;
                counter=0;
                counerTv.setText(String.valueOf(counter));
                showElapsedTime();
            }
        });

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counerTv.setText(String.valueOf(counter));
                counter++;
            }
        });

        finishExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                user.setLevel();
                Bundle bundle = getArguments();
                if(bundle!= null) {
                    Action action = (Action) bundle.getSerializable("action");
                    action.setBestTime(((double)(SystemClock.elapsedRealtime() - chronometer.getBase())) / 1000);
                    user.addFinishedAction(action);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    MyProfileFragment myProfileFragment = new MyProfileFragment();
                    bundle.putString("fragment", "Chronometer Fragment");
                    myProfileFragment.setArguments(bundle);
                    ft.replace(R.id.fragment_container, myProfileFragment).commit();
                }
                timeOfExercise = 0;

            }
        });

        return root;
    }
    private void showElapsedTime() {
        long elapsedsecond = (SystemClock.elapsedRealtime() - chronometer.getBase())/1000;
    }
}

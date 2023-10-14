package com.robinson.notewithreminder.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.robinson.notewithreminder.R;
import com.robinson.notewithreminder.utilities.CommonParameters;

public class TimeDialogFragment extends DialogFragment {
    View view;
    Button save_timepick, cancel_timepick;
    TimePicker tp;
    String time_get_time, date_get_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.pop_time, container, false);
        save_timepick = (Button) view.findViewById(R.id.time_save_picker);
        cancel_timepick = (Button) view.findViewById(R.id.time_cancel_picker);
        tp = (TimePicker) view.findViewById(R.id.tp);


        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value1")))
            time_get_time = getArguments().getString("time_value1");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value1")))
            date_get_time = getArguments().getString("date_value1");

        if (!time_get_time.equalsIgnoreCase("ignore")) {
            String[] time_arr = time_get_time.split(":", 2);
            for (String a : time_arr)
                System.out.println("Holathis1" + a);

            int Hour = Integer.parseInt(time_arr[0]);
            int Minute = Integer.parseInt(time_arr[1]);
            tp.setHour(Hour);
            tp.setMinute(Minute);
        }

        save_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"Save timepick",Toast.LENGTH_SHORT).show();
                String timeOn = tp.getHour() + ":" + tp.getMinute();
                System.out.println("MEWW" + timeOn);
                String[] time_arr = timeOn.split(":", 2);

                for (String a : time_arr)
                    System.out.println("Holathis1" + a);

                System.out.println("Holathis12" + time_arr[0]);
                System.out.println("Holathis12" + time_arr[1]);


                androidx.fragment.app.FragmentManager fragmentManager5 = getFragmentManager();


                InfoDialogFragment maa3 = new InfoDialogFragment();
                Bundle bundleTime_back = new Bundle();
              //  bundleTime_back.putString("time_value", timeOn);
              //  bundleTime_back.putString("date_value", date_get_time);

                bundleTime_back.putString(CommonParameters.noteTime, timeOn);
                bundleTime_back.putString(CommonParameters.noteDate, date_get_time);

                maa3.setArguments(bundleTime_back);

                assert fragmentManager5 != null;
                maa3.show(fragmentManager5, "Back to dialog save time");


                dismiss();
            }
        });
        cancel_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"Cancel timepick",Toast.LENGTH_SHORT).show();
                androidx.fragment.app.FragmentManager fragmentManager6 = getFragmentManager();


                InfoDialogFragment ma3 = new InfoDialogFragment();
                Bundle bundleTime_back = new Bundle();
                //bundleTime_back.putString("time_value", time_get_time);
                //bundleTime_back.putString("date_value", date_get_time);

                bundleTime_back.putString(CommonParameters.noteTime, time_get_time);
                bundleTime_back.putString(CommonParameters.noteDate, date_get_time);

                ma3.setArguments(bundleTime_back);

                assert fragmentManager6 != null;
                ma3.show(fragmentManager6, "Back to dialog cancel time");


                dismiss();
            }
        });
        return view;
    }


}

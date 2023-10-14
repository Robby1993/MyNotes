package com.robinson.notewithreminder.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.robinson.notewithreminder.R;
import com.robinson.notewithreminder.utilities.CommonParameters;

public class DateDialogFragment extends DialogFragment {
    View view;
    Button save_datepick, cancel_datepick;
    DatePicker dp;
    String time_get_date, date_get_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.pop_date, container, false);
        save_datepick = (Button) view.findViewById(R.id.date_save_picker);
        cancel_datepick = (Button) view.findViewById(R.id.date_cancel_picker);
        dp = (DatePicker) view.findViewById(R.id.dp);
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value2")))
            time_get_date = getArguments().getString("time_value2");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value2")))
            date_get_date = getArguments().getString("date_value2");


        if (!date_get_date.equalsIgnoreCase("ignore")) {
            String[] date_arr = date_get_date.split("-", 3);
            for (String b : date_arr)
                System.out.println("Holathis2" + b);
            int Year = Integer.parseInt(date_arr[2]);
            int Month = Integer.parseInt(date_arr[1]);
            int Day = Integer.parseInt(date_arr[0]);
            dp.updateDate(Year - 1 + 1, Month - 1, Day - 1 + 1);
        }


        save_datepick.setOnClickListener(v -> {
            int m = Integer.parseInt(String.valueOf(dp.getMonth())) + 1;
            String dateOn = dp.getDayOfMonth() + "-" + m + "-" + dp.getYear();
            System.out.println("MEWW" + dateOn);

            String[] date_arr = dateOn.split("-", 3);
            for (String b : date_arr)
                System.out.println("Holathis2" + b);

            System.out.println("Holathis12" + date_arr[0]);
            System.out.println("Holathis12" + date_arr[1]);
            System.out.println("Holathis12" + date_arr[2]);

            InfoDialogFragment popInfo = new InfoDialogFragment();
            Bundle bundleDate_back = new Bundle();
            bundleDate_back.putString(CommonParameters.noteTime, time_get_date);
            bundleDate_back.putString(CommonParameters.noteDate, dateOn);

            popInfo.setArguments(bundleDate_back);

            assert getFragmentManager() != null;
            popInfo.show(getFragmentManager(), "Back to dialog save date");


            dismiss();
        });
        cancel_datepick.setOnClickListener(v -> {
            androidx.fragment.app.FragmentManager fragmentManager8 = getFragmentManager();
            InfoDialogFragment maa8 = new InfoDialogFragment();
            Bundle bundleTime_back = new Bundle();
          //  bundleTime_back.putString("time_value", time_get_date);
           // bundleTime_back.putString("date_value", date_get_date);

            bundleTime_back.putString(CommonParameters.noteTime, time_get_date);
            bundleTime_back.putString(CommonParameters.noteDate, date_get_date);

            maa8.setArguments(bundleTime_back);

            assert fragmentManager8 != null;
            maa8.show(fragmentManager8, "Back to dialog cancel date");

            dismiss();
        });

        return view;
    }

}

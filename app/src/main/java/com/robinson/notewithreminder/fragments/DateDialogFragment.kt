package com.robinson.notewithreminder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.robinson.notewithreminder.databinding.PopDateBinding
import com.robinson.notewithreminder.utilities.CommonParameters
import java.util.Calendar

class DateDialogFragment : DialogFragment() {

    private var _popDateBinding: PopDateBinding? = null
    private val popDateBinding get() = _popDateBinding!!

    private var receivedTime: String? = null
    private var receviedDate: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _popDateBinding = PopDateBinding.inflate(inflater, container, false)





        if (arguments != null) {
            receivedTime = requireArguments().getString(CommonParameters.noteTime)
            receviedDate = requireArguments().getString(CommonParameters.noteDate)
        }


        disablePreviousDate()

        if (!receviedDate.equals("ignore", ignoreCase = true)) {
            val dateArr = receviedDate!!.split("-".toRegex(), limit = 3).toTypedArray()
            val yearInt = dateArr[2].toInt()
            val monthInt = dateArr[1].toInt()
            val dayInt = dateArr[0].toInt()
            popDateBinding.datePicker.updateDate(yearInt - 1 + 1, monthInt - 1, dayInt - 1 + 1)
        }
        popDateBinding.btnConfirm.setOnClickListener { v: View? ->
            val m = popDateBinding.datePicker.month.toString().toInt() + 1
            val dateOn = popDateBinding.datePicker.dayOfMonth.toString() + "-" + m + "-" + popDateBinding.datePicker.year
            val dateArr = dateOn.split("-".toRegex(), limit = 3).toTypedArray()

            println("dateArr 1" + dateArr[0])
            println("dateArr 2" + dateArr[1])
            println("dateArr 3" + dateArr[2])

            val infoDialogFragment = InfoDialogFragment()
            val bundle = Bundle()
            bundle.putString(CommonParameters.noteTime, receivedTime)
            bundle.putString(CommonParameters.noteDate, dateOn)
            infoDialogFragment.arguments = bundle
            fragmentManager?.let { infoDialogFragment.show(it, "Back to dialog save date") }
            dismiss()
        }
        popDateBinding.btnCancel.setOnClickListener { v: View? ->
            val infoDialogFragment = InfoDialogFragment()
            val bundle = Bundle()
            bundle.putString(CommonParameters.noteTime, receivedTime)
            bundle.putString(CommonParameters.noteDate, receviedDate)
            infoDialogFragment.arguments = bundle
            fragmentManager?.let { infoDialogFragment.show(it, "Back to dialog cancel date") }
            dismiss()
        }
        return popDateBinding.root
    }

    private fun disablePreviousDate(){
        // Get the current date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Set the minimum date
        popDateBinding.datePicker.minDate = calendar.timeInMillis;

        // Set the minimum date to the current date
        popDateBinding.datePicker.init(currentYear, currentMonth, currentDay) { _, year, monthOfYear, dayOfMonth ->
            // Handle the date selection
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _popDateBinding = null
    }
}
package com.robinson.notewithreminder.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.robinson.notewithreminder.databinding.PopTimeBinding
import com.robinson.notewithreminder.utilities.CommonParameters
import java.util.Calendar

class TimeDialogFragment : DialogFragment() {

    private var _popTimeBinding: PopTimeBinding? = null
    private val popTimeBinding get() = _popTimeBinding!!

    private var receivedTime: String? = null
    private var receviedDate: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _popTimeBinding = PopTimeBinding.inflate(inflater, container, false)
        if (arguments != null) {
            receivedTime = requireArguments().getString(CommonParameters.noteTime)
            receviedDate = requireArguments().getString(CommonParameters.noteDate)
        }

        if (!receivedTime.equals("ignore", ignoreCase = true)) {
            val timeArr = receivedTime!!.split(":".toRegex(), limit = 2).toTypedArray()
            popTimeBinding.timePicker.hour = timeArr[0].toInt()
            popTimeBinding.timePicker.minute = timeArr[1].toInt()
        }

        popTimeBinding.btnConfirm.setOnClickListener {
            val timeOn =
                popTimeBinding.timePicker.hour.toString() + ":" + popTimeBinding.timePicker.minute
            val timeArr = timeOn.split(":".toRegex(), limit = 2).toTypedArray()
            for (a in timeArr) println("timeArr: $a")
            println("timeArr 1" + timeArr[0])
            println("timeArr 2" + timeArr[1])

            val infoDialogFragment = InfoDialogFragment()
            val bundle = Bundle()
            bundle.putString(CommonParameters.noteTime, timeOn)
            bundle.putString(CommonParameters.noteDate, receviedDate)
            infoDialogFragment.arguments = bundle
            fragmentManager?.let { it1 -> infoDialogFragment.show(it1, "Back to dialog save time") }
            dismiss()
        }
        popTimeBinding.btnCancel.setOnClickListener {
            val infoDialogFragment = InfoDialogFragment()
            val bundle = Bundle()
            bundle.putString(CommonParameters.noteTime, receivedTime)
            bundle.putString(CommonParameters.noteDate, receviedDate)
            infoDialogFragment.arguments = bundle
            fragmentManager?.let { it1 ->
                infoDialogFragment.show(
                    it1,
                    "Back to dialog cancel time"
                )
            }
            dismiss()
        }
        return popTimeBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _popTimeBinding = null
    }

}
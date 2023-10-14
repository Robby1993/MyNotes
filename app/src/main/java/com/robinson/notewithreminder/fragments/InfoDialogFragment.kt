package com.robinson.notewithreminder.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.robinson.notewithreminder.databinding.PopInfoBinding
import com.robinson.notewithreminder.screen.NoteUpdateActivity
import com.robinson.notewithreminder.utilities.CommonParameters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InfoDialogFragment : DialogFragment() {

    private var receivedTime: String? = null
    private var receviedDate: String? = null

    private var _popInfoBinding: PopInfoBinding? = null
    private val popInfoBinding get() = _popInfoBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _popInfoBinding = PopInfoBinding.inflate(inflater, container, false)
        if (arguments != null) {
            receivedTime = requireArguments().getString(CommonParameters.noteTime)
            receviedDate = requireArguments().getString(CommonParameters.noteDate)
        }

        val sdf1 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentTime1 = sdf1.format(Date())
        val currentDate1 = sdf.format(Date())
        if (TextUtils.isEmpty(receivedTime) || receivedTime.equals("ignore", ignoreCase = true)) {
            popInfoBinding.tvTime.text = currentTime1
        } else {
             popInfoBinding.tvTime.text = receivedTime
        }
        if (TextUtils.isEmpty(receviedDate) || receviedDate.equals("ignore", ignoreCase = true)) {
            popInfoBinding.tvDate.text = currentDate1
        } else {
            popInfoBinding.tvDate.text = receviedDate
        }

        popInfoBinding.ivBack.setOnClickListener { v: View? ->
            dismiss()
        }
        popInfoBinding.llTime.setOnClickListener {
            val timeDialogFragment = TimeDialogFragment()
            val bundle = Bundle()
            bundle.putString(CommonParameters.noteTime, receivedTime)
            bundle.putString(CommonParameters.noteDate, receviedDate)
            timeDialogFragment.arguments = bundle
            fragmentManager?.let { it1 -> timeDialogFragment.show(it1, "Time Picker Fragment Show") }
            dismiss()
        }
        popInfoBinding.llDate.setOnClickListener { v: View? ->
            val dateDialogFragment = DateDialogFragment()
            val bundleDate = Bundle()
            bundleDate.putString(CommonParameters.noteTime, receivedTime)
            bundleDate.putString(CommonParameters.noteDate, receviedDate)
            dateDialogFragment.arguments = bundleDate
            fragmentManager?.let { dateDialogFragment.show(it, "Date Picker Fragment Show") }
            dismiss()
        }
        popInfoBinding.btnDelete.setOnClickListener { v: View? ->
            Toast.makeText(context, "Reminder Removed!", Toast.LENGTH_SHORT).show()
            val m3a1 = activity as NoteUpdateActivity?
            m3a1!!.deleteRem()
            dismiss()
        }
        popInfoBinding.btnSet.setOnClickListener { v: View? ->
            Toast.makeText(context, "Reminder Added!", Toast.LENGTH_SHORT).show()
            val m3a = activity as NoteUpdateActivity?
            m3a!!.setDateTime( popInfoBinding.tvTime.text.toString(),  popInfoBinding.tvDate.text.toString())
            dismiss()
        }
        return popInfoBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _popInfoBinding = null
    }

}
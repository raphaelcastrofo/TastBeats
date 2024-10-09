package com.devspace.taskbeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class CreateTaskBottomSheet(
    private val onCreateClicked: (TaskUiData) -> Unit,
    private val categoryList: List<CategoryUiData>

) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.creat_task_botto_sheet, container, false)

        val btnCreate = view.findViewById<Button>(R.id.btn_Task_create)
        val tieTaskName = view.findViewById<TextInputEditText>(R.id.tie_Task_name)

        btnCreate.setOnClickListener {
            val name = tieTaskName.text.toString()
            onCreateClicked.invoke(name)
            dismiss()
        }

        val categoryStr : List<String> = categoryList.map { it.name }

        val spinner: Spinner = view.findViewById(R.id.sp_categories)
// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categoryStr.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        return view
    }
}
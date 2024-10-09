package com.devspace.taskbeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
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

        var taskCategory : String? = null

        btnCreate.setOnClickListener {
            val name = tieTaskName.text.toString()
            if (taskCategory != null) {
                onCreateClicked.invoke(
                    TaskUiData(
                        name = name,
                        category = requireNotNull(taskCategory)
                    )
                )
                dismiss()
            }else{
              Snackbar.make(btnCreate, "Please select a category", Snackbar.LENGTH_LONG).show()
            }
        }

        val categoryStr: List<String> = categoryList.map { it.name }

        val spinner: Spinner = view.findViewById(R.id.sp_categories)
        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categoryStr.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskCategory = categoryStr.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        return view
    }
}
package com.devspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var tasks = listOf<TaskUiData>()
    private var categories = listOf<CategoryUiData>()


    private val categoryAdapter = CategoryListAdapter()
    private val taskAdapter = TaskListAdapter()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatsDataBase::
            class.java, "database-task-beat"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy {
        db.getCategoryDao()
    }

    private val taskDao: TaskDao by lazy {
        db.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)
        val fabCreateTask = findViewById<FloatingActionButton>(R.id.fab_create_task)

        fabCreateTask.setOnClickListener {
            val createTaskBottomSheet = CreateTaskBottomSheet(
                { taskToBeCreated ->
                    val taskEntityToBeInsert =  TaskEntity(
                        name = taskToBeCreated.name,
                        category = taskToBeCreated.category
                )
                    insertTask(taskEntityToBeInsert)
                },
                categories
            )

            createTaskBottomSheet.show(
                supportFragmentManager,
                "createTaskBottonSheet"
            )
        }

        categoryAdapter.setOnClickListener { selected ->
            if (selected.name == "+") {
                val createCategoryBottomSheet = CreateCategoryBottomSheet { categoryName ->
                    val categoryEntity = CategoryEntity(
                        name = categoryName,
                        isSelected = false
                    )
                    insertCategory(categoryEntity)
                }

                createCategoryBottomSheet.show(
                    supportFragmentManager,
                    "createCategoryBottonSheet"
                )

            } else {
                val categoryTemp = categories.map { item ->
                    when {
                        item.name == selected.name && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.name != selected.name && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                }

                categories = categoryTemp
                val selectedCategory =
                    categoryTemp.find { it.name == selected.name && !it.isSelected }

                val taskTemp =
                    if (selectedCategory != null || selected.name == "ALL") {
                        tasks
                    } else {
                        tasks.filter { it.category == selected.name }
                    }
                taskAdapter.submitList(taskTemp)
                categoryAdapter.submitList(categoryTemp)


            }
        }

        rvCategory.adapter = categoryAdapter
        GlobalScope.launch(Dispatchers.IO) {
            getCategoriesFromDataBase()
        }


        rvTask.adapter = taskAdapter
        GlobalScope.launch(Dispatchers.IO) {
            getTaskFromDataBase()
        }
    }


    private fun getCategoriesFromDataBase() {

        val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
        val categoriesUIData = categoriesFromDb.map {
            CategoryUiData(
                name = it.name,
                isSelected = it.isSelected
            )
        }.toMutableList()

        categoriesUIData.add(
            CategoryUiData(
                name = "+",
                isSelected = false
            )
        )
        GlobalScope.launch(Dispatchers.Main) {
            categories = categoriesUIData
            categoryAdapter.submitList(categoriesUIData)
        }
    }


    private fun getTaskFromDataBase() {

        val tasksFromDb: List<TaskEntity> = taskDao.getAll()
        val tasksUIData = tasksFromDb.map {
            TaskUiData(
                name = it.name,
                category = it.category
            )
        }

        GlobalScope.launch(Dispatchers.Main) {
            tasks = tasksUIData
            taskAdapter.submitList(tasksUIData)

        }
    }


    private fun insertCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insert(categoryEntity)
            getCategoriesFromDataBase()
        }
    }

    private fun insertTask(taskEntity: TaskEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            taskDao.insert(taskEntity)
            getTaskFromDataBase()
        }
    }
}






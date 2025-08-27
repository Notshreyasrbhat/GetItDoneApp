package com.project.getitdone.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.project.getitdone.R
import com.project.getitdone.data.model.TaskList
import com.project.getitdone.databinding.ActivityMainBinding
import com.project.getitdone.databinding.DialogAddBinding
import com.project.getitdone.databinding.DialogAddTaskListBinding
import com.project.getitdone.databinding.TabButtonBinding
import com.project.getitdone.ui.tasks.StarredTasksFragment
import com.project.getitdone.ui.tasks.TasksFragment
import com.project.getitdone.util.InputValidator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var currentTaskLists :List<TaskList> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflate
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 2. Attach to activity
        setContentView(binding.root)

        // 3. Use binding safely with .apply
        binding.apply {

            lifecycleScope.launch {
                viewModel.getTaskLists().collectLatest { taskLists ->

                    currentTaskLists =taskLists

                    pager.adapter = PagerAdapter(this@MainActivity, taskLists)
                    pager.currentItem = 1

                    TabLayoutMediator(tabs, pager) { tab, position ->
                        when (position) {
                            0 -> tab.icon = ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.icon_star_filled
                            )
                            taskLists.size + 1 -> {
                                val buttonBinding = TabButtonBinding.inflate(layoutInflater)
                                tab.customView = buttonBinding.root.apply {
                                    setOnClickListener { showAddTaskListDialog() }
                                }
                            }
                            else -> tab.text = taskLists[position - 1].name
                        }
                    }.attach()
                }
            }

            fab.setOnClickListener { showAddTaskDialog() }
        }
    }

    private fun showAddTaskListDialog() {
        val dialogBinding = DialogAddTaskListBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.add_new_list)
            .setView(dialogBinding.root)
            .setPositiveButton("Create"){ dialog, _ ->
                viewModel.addNewTaskList(dialogBinding.editTextListName.text?.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){ dialog,_->
                dialog.dismiss()
            }
            .show()

    }


    private fun showAddTaskDialog() {
        DialogAddBinding.inflate(layoutInflater).apply {
            val dialog = BottomSheetDialog(this@MainActivity)
            dialog.setContentView(root)

            buttonSave.isEnabled = false
            editTextTaskTitle.addTextChangedListener { input ->
                buttonSave.isEnabled = InputValidator.isInputValid(input?.toString())
            }

            buttonShowDetails.setOnClickListener {
                editTextTaskDetails.visibility =
                    if (editTextTaskDetails.isVisible) View.GONE else View.VISIBLE
            }

            binding.pager.currentItem

            buttonSave.setOnClickListener {

                val selectedTaskListId= currentTaskLists[binding.pager.currentItem -1].id

                viewModel.createTask(
                    title = editTextTaskTitle.text.toString(),
                    description = editTextTaskDetails.text.toString(),
                    listId = selectedTaskListId

                )
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    inner class PagerAdapter(activity: MainActivity, private val taskLists: List<TaskList>) : FragmentStateAdapter(activity) {
        override fun getItemCount() = taskLists.size + 2

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> StarredTasksFragment()
                taskLists.size + 1 -> Fragment()
                else-> TasksFragment(taskLists[position -1].id)
            }
        }

    }
}
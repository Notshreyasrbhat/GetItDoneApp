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

    // ViewModel tied to this Activity’s lifecycle.
    // Provides task list + task data and handles persistence.
    private val viewModel: MainViewModel by viewModels()

    // ViewBinding object for this Activity's layout
    private lateinit var binding: ActivityMainBinding

    // Currently available task lists (updated from DB via ViewModel)
    private var currentTaskLists :List<TaskList> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            // Start collecting task lists from ViewModel.
            // Every update (new list added, list renamed, etc.)
            // will refresh the ViewPager and Tabs dynamically.
            lifecycleScope.launch {
                viewModel.getTaskLists().collectLatest { taskLists ->

                    currentTaskLists = taskLists

                    // ViewPager adapter rebuilt whenever task list changes
                    pager.adapter = PagerAdapter(this@MainActivity, taskLists)

                    // Default to "All Tasks" tab (index 1, after Starred tab)
                    pager.currentItem = 1

                    // Connect TabLayout with ViewPager
                    TabLayoutMediator(tabs, pager) { tab, position ->
                        when (position) {
                            // First tab = Starred tasks
                            0 -> tab.icon = ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.icon_star_filled
                            )
                            // Last tab = "Add List" button (custom view)
                            taskLists.size + 1 -> {
                                val buttonBinding = TabButtonBinding.inflate(layoutInflater)
                                tab.customView = buttonBinding.root.apply {
                                    setOnClickListener { showAddTaskListDialog() }
                                }
                            }
                            // Middle tabs = task lists (dynamic)
                            else -> tab.text = taskLists[position - 1].name
                        }
                    }.attach()
                }
            }

            // Floating Action Button adds a new task in currently selected list
            fab.setOnClickListener { showAddTaskDialog() }
        }
    }

    /**
     * Shows a dialog for creating a new Task List.
     * Uses MaterialAlertDialog for consistency with Material Design.
     */
    private fun showAddTaskListDialog() {
        val dialogBinding = DialogAddTaskListBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.add_new_list)
            .setView(dialogBinding.root)
            .setPositiveButton("Create"){ dialog, _ ->
                // Delegate creation logic to ViewModel
                viewModel.addNewTaskList(dialogBinding.editTextListName.text?.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){ dialog,_->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Shows a BottomSheetDialog for adding a new Task to
     * the currently active Task List.
     */
    private fun showAddTaskDialog() {
        DialogAddBinding.inflate(layoutInflater).apply {
            val dialog = BottomSheetDialog(this@MainActivity)
            dialog.setContentView(root)

            // Initially disable save until valid input is given
            buttonSave.isEnabled = false
            editTextTaskTitle.addTextChangedListener { input ->
                buttonSave.isEnabled = InputValidator.isInputValid(input?.toString())
            }

            // Toggle visibility of optional task details
            buttonShowDetails.setOnClickListener {
                editTextTaskDetails.visibility =
                    if (editTextTaskDetails.isVisible) View.GONE else View.VISIBLE
            }

            // Save button creates the task in the selected list
            buttonSave.setOnClickListener {
                // pager.currentItem - 1 accounts for the "Starred" tab at index 0
                val selectedTaskListId = currentTaskLists[binding.pager.currentItem - 1].id

                // Ask ViewModel to create task in DB
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

    /**
     * PagerAdapter manages tab navigation.
     * - Position 0 = Starred tasks
     * - Middle positions = user-created task lists
     * - Last position = Empty fragment placeholder (Add List button)
     */
    inner class PagerAdapter(activity: MainActivity, private val taskLists: List<TaskList>) : FragmentStateAdapter(activity) {
        override fun getItemCount() = taskLists.size + 2 // +2 for Starred + Add List

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> StarredTasksFragment()
                taskLists.size + 1 -> Fragment() // "Add List" placeholder
                else -> TasksFragment(taskLists[position - 1].id)
            }
        }
    }
}

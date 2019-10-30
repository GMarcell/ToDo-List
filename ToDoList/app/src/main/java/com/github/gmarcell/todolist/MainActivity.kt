package com.github.gmarcell.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.gmarcell.todolist.data.Does
import com.github.gmarcell.todolist.adapters.todoAdapter
import com.github.gmarcell.todolist.viewmodels.DoesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var doesViewModel: DoesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddNote.setOnClickListener {
            startActivityForResult(
                Intent(this, AddEditDoesActivity::class.java),
                ADD_NOTE_REQUEST
            )
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = todoAdapter()

        recycler_view.adapter = adapter

        doesViewModel = ViewModelProviders.of(this).get(DoesViewModel::class.java)

        doesViewModel.getAllNotes().observe(this, Observer<List<Does>> {
            adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                doesViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Does Deleted!", Toast.LENGTH_SHORT).show()
            }
        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : todoAdapter.OnItemClickListener {
            override fun onItemClick(does: Does) {
                val intent = Intent(baseContext, AddEditDoesActivity::class.java)
                intent.putExtra(AddEditDoesActivity.EXTRA_ID, does.id)
                intent.putExtra(AddEditDoesActivity.EXTRA_TITLE, does.title)
                intent.putExtra(AddEditDoesActivity.EXTRA_DESCRIPTION, does.description)
                intent.putExtra(AddEditDoesActivity.EXTRA_PRIORITY, does.priority)
                intent.putExtra(AddEditDoesActivity.EXTRA_DUE_DATE, does.duedate)

                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_all_notes -> {
                doesViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newNote = Does(
                data!!.getStringExtra(AddEditDoesActivity.EXTRA_TITLE),
                data.getStringExtra(AddEditDoesActivity.EXTRA_DESCRIPTION),
                data.getIntExtra(AddEditDoesActivity.EXTRA_PRIORITY, 1),
                data.getStringExtra(AddEditDoesActivity.EXTRA_DUE_DATE)
            )
            doesViewModel.insert(newNote)

            Toast.makeText(this, "Does saved!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddEditDoesActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Could not update! Error!", Toast.LENGTH_SHORT).show()
            }

            val updateNote = Does(
                data!!.getStringExtra(AddEditDoesActivity.EXTRA_TITLE),
                data.getStringExtra(AddEditDoesActivity.EXTRA_DESCRIPTION),
                data.getIntExtra(AddEditDoesActivity.EXTRA_PRIORITY, 1),
                data.getStringExtra(AddEditDoesActivity.EXTRA_DUE_DATE)
            )
            updateNote.id = data.getIntExtra(AddEditDoesActivity.EXTRA_ID, -1)
            doesViewModel.update(updateNote)

        } else {
            Toast.makeText(this, "Does not saved!", Toast.LENGTH_SHORT).show()
        }


    }
}



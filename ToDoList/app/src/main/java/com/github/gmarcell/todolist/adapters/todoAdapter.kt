package com.github.gmarcell.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.gmarcell.todolist.R
import com.github.gmarcell.todolist.data.Does
import kotlinx.android.synthetic.main.note_item.view.*

class todoAdapter : ListAdapter<Does, todoAdapter.NoteHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Does>() {
            override fun areItemsTheSame(oldItem: Does, newItem: Does): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Does, newItem: Does): Boolean {
                return oldItem.title == newItem.title && oldItem.description == newItem.description
                        && oldItem.priority == newItem.priority && oldItem.duedate == newItem.duedate
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentDoes: Does = getItem(position)

        holder.textViewTitle.text = currentDoes.title
        holder.textViewPriority.text = currentDoes.priority.toString()
        holder.textViewDescription.text = currentDoes.description
        holder.textViewDueDate.text = currentDoes.duedate
    }

    fun getNoteAt(position: Int): Does {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewTitle: TextView = itemView.text_view_title
        var textViewPriority: TextView = itemView.text_view_priority
        var textViewDescription: TextView = itemView.text_view_description
        var textViewDueDate: TextView = itemView.text_view_duedate
    }

    interface OnItemClickListener {
        fun onItemClick(does: Does)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

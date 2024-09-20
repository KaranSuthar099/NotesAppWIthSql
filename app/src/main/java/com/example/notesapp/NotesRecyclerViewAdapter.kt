package com.example.notesapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotesRecyclerViewAdapter(var noteList: List<NoteModal>, var context: Context) :
    RecyclerView.Adapter<NotesRecyclerViewAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView = itemView.findViewById<TextView>(R.id.noteViewTitle)
        var subtitleTextView = itemView.findViewById<TextView>(R.id.noteViewSubtitle)
        var card = itemView.findViewById<CardView>(R.id.card)
        var delete = itemView.findViewById<Button>(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_view, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = noteList[position]
        val currentNoteContentLength = currentNote.content.length

        holder.titleTextView.text = currentNote.title
        val content = currentNote.content
        holder.subtitleTextView.text = content.subSequence(
            0,
            if (currentNoteContentLength > 40) 40 else currentNoteContentLength
        )

        holder.card.setOnClickListener {
            val intent = Intent(context, NoteViewActivity::class.java)
            intent.putExtra("NOTE", currentNote)
            context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Delete Note ?")
            alertDialog.setMessage("Are you sure you want to Delete this Note")
            alertDialog.setNegativeButton("NO"){ _ , _-> }
            alertDialog.setPositiveButton("Yes") { _, _ ->
                run {
                    val db = SqliteDatabase(context)
                    db.deleteNote(currentNote)

                    updateList(db.readData())
                }
            }.show()
        }


    }

    fun updateList(noteList: List<NoteModal>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }
}
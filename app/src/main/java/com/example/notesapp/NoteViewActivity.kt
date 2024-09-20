package com.example.notesapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityNoteViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteViewBinding
    private var dbHandler = SqliteDatabase(this)
    private var note: NoteModal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null && intent.hasExtra("NOTE")){
            note = intent.getSerializableExtra("NOTE") as NoteModal
            binding.titleInputEditText.setText(note?.title)
            binding.contentInputEditText.setText(note?.content)

        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.titleInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charsequence: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                charsequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
                if (charsequence.isNullOrEmpty()) {
                    showTitleEmptyError()
                } else {
                    removeTitleEmptyError()
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.SaveButton -> saveNote() //TODO implement new note and edit note functionality
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val alertDialog = MaterialAlertDialogBuilder(this@NoteViewActivity)
                alertDialog.setTitle("Confirm Exit")
                alertDialog.setMessage("Are you sure you want to Exit")
                alertDialog.setNegativeButton("Cancel") { dialogInterface, i -> }
                alertDialog.setPositiveButton("Exit") { dialogInterface, i ->
                    run {
                        this@NoteViewActivity.finish()
                    }
                }
                alertDialog.setCancelable(false)

                alertDialog.show()
            }
        })
    }

    private fun isEditMode():Boolean {
        return note != null
    }

    private fun removeTitleEmptyError() {
        binding.titleInputLayout.error = ""
    }

    private fun showTitleEmptyError() {
        binding.titleInputLayout.error = "Title Can't be Empty"
    }

    private fun saveNote() {
        val title = binding.titleInputEditText.text.toString()
        val content = binding.contentInputEditText.text.toString()

        if (title.isEmpty()) {
            showToast("title Can't be Empty")
            showTitleEmptyError()
            return
        }



        try {
            if(isEditMode()){
                note!!.title = title
                note!!.content = content
                dbHandler.updateNote(note!!)
                showToast("Note Updated")

            }else {
                dbHandler.addNote(title, content)
                showToast("Note Added")
            }
            finish()
        } catch (e: Exception) {
            showToast("Error adding Note")
        }
    }

    private fun updateNote(){

    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
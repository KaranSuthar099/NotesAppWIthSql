package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesList: List<NoteModal>
    private lateinit var db: SqliteDatabase
    private lateinit var adapter: NotesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.floating.setOnClickListener {
            val intent = Intent(this, NoteViewActivity::class.java)
            startActivity(intent)
        }

        db = SqliteDatabase(this)
        notesList = db.readData()

        binding.mainActivityRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        adapter = NotesRecyclerViewAdapter(notesList, this)
        binding.mainActivityRecyclerView.adapter = adapter

        binding.mainActivityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scrolling down, shrink the FAB
                    binding.floating.shrink()
                } else if (dy < 0) {
                    // Scrolling up, expand the FAB
                    binding.floating.extend()
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        adapter.updateList(db.readData())


    }
}
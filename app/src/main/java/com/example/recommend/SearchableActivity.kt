package com.example.recommend

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_searchable.*
import java.util.ArrayList

class SearchableActivity : AppCompatActivity() {
    private lateinit var adapter: NewsAdapter
    private val mDataList: MutableList<NewsData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userdao = AppDatabase.getDatabase(this).userDao()
        adapter = NewsAdapter(this, mDataList, object : NewsAdapter.NewsListener {
            override fun onClickItem(url: String?,pos:Int) {
                val intent = Intent(this@SearchableActivity, NewsActivity::class.java)
                intent.putExtra(NewsActivity.KEY_URL, url)
                intent.putExtra("title", mDataList[pos].title)
                intent.putExtra("image", mDataList[pos].pic)
                intent.putExtra("url", mDataList[pos].url)
                startActivity(intent)
            }
        })
        rec.adapter = adapter
        rec.layoutManager = GridLayoutManager(this, 1)
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                for(i in userdao.find(query))
                    mDataList.add(i)
                adapter.notifyDataSetChanged()
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
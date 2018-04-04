package com.android.shop.shopapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import com.android.shop.shopapp.R
import com.android.shop.shopapp.dao.ProductModel
import com.android.shop.shopapp.data.GroupAdapter
import com.android.shop.shopapp.model.network.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group.*




/**
 * Created by myron on 3/29/18.
 */
class SearchActivity : BaseActivity() {
    var list = arrayListOf<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
//        //模糊查询
//        var group = intent.getStringExtra(GROUP)

//        swipeRefreshLayout.setOnRefreshListener {
//            getProductsByGroup("")
//            swipeRefreshLayout.isRefreshing = false
//        }
//        swipeRefreshLayout.isRefreshing = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@SearchActivity, DividerItemDecoration.HORIZONTAL))
            adapter = GroupAdapter(this@SearchActivity, list)
        }


        handleIntent(intent);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.search)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.isIconified = true

        //*** setOnQueryTextFocusChangeListener ***
//        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener() {
//
//            override fun onFocusChange(v: View, hasFocus: Boolean) {
//
//            }
//        })

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//
//                return false
//            }
//
//            override fun onQueryTextChange(searchQuery: String): Boolean {
////                myAppAdapter.filter(searchQuery.trim { it <= ' ' })
////                listView.invalidate()
//                return true
//            }
//        })
//        searchItem?.setOnActionExpandListener(MenuItem.OnActionExpandListener {})
//
//        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener() {
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                // Do something when collapsed
//                return true  // Return true to collapse action view
//            }
//
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                // Do something when expanded
//                return true  // Return true to expand action view
//            }
//        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    //    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
    override fun onNewIntent(intent: Intent) {
        handleIntent(intent);
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            getProductsByGroup(query)
        }
    }

    private fun getProductsByGroup(text: String) {
        val productService = RetrofitHelper().getProductsService()
        mCompositeDisposable.add(productService.search(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    list = t.products!!
                    (recyclerView.adapter as GroupAdapter).contents = list
                    recyclerView.adapter?.notifyDataSetChanged()
//                    swipeRefreshLayout.isRefreshing = false
                }, {
                    Toast.makeText(this@SearchActivity, "请求数据失败", Toast.LENGTH_LONG).show()
                }))
    }

}
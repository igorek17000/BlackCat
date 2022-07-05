package com.catfast.safetool.ui

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.ServersObtainer
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.databinding.ActivityServersBinding
import com.catfast.safetool.ui.adapter.ServersAdapter

class ServersActivity : BasicView<ActivityServersBinding>() {

    override val vb: ActivityServersBinding by lazy { ActivityServersBinding.inflate(layoutInflater) }

    override fun basicClicks() {
        vb.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun basicObservers() {

    }

    override fun basicRunners() {
        immersiveStatusBar(vb.appbarLayout, false)
        initAdapter()
    }

    private lateinit var mAdapter: ServersAdapter

    private fun initAdapter() {
        mAdapter = ServersAdapter(activity) { onChanged(it) }
        vb.list.layoutManager = GridLayoutManager(activity, 2)
        vb.list.adapter = mAdapter
        mAdapter.setListData(ServersObtainer.conns)
    }

    private fun onChanged(position: Int) {
        val connItem = ServersObtainer.conns[position]
        val isChangedConn = !connItem.selected
        ServersObtainer.conns.forEach { it.selected = false }
        connItem.selected = true
        setResult(RESULT_OK, Intent().apply { putExtra("isChangedConn", isChangedConn) })
        finish()
    }

}
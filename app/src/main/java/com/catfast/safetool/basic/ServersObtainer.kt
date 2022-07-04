package com.catfast.safetool.basic

import com.catfast.safetool.R
import com.catfast.safetool.basic.exts.app
import com.catfast.safetool.bean.ConnItem
import com.github.shadowsocks.Core
import com.github.shadowsocks.database.ProfileManager
import kotlin.random.Random

object ServersObtainer {

    val conns = mutableListOf<ConnItem>()
    private val fasters = mutableListOf<String>()

    fun initFasters(list: MutableList<String>? = null) {
        if (list.isNullOrEmpty()) {
            initDefaultFasters()
        } else {
            fasters.clear()
            fasters.addAll(list)
        }
    }

    fun initConns(list: MutableList<ConnItem>? = null) {
        if (list.isNullOrEmpty()) {
            initDefaultConns()
        } else {
            conns.clear()
            conns.add(ConnItem(name = "Super Fast Server", selected = true))
            conns.addAll(list)
        }
    }

    private fun initDefaultFasters() {
        fasters.clear()
        fasters.add("United States")
        fasters.add("Canada")
    }

    private fun initDefaultConns() {
        conns.clear()
        conns.add(ConnItem(name = "Super Fast Server", selected = true))
        conns.add(ConnItem(name = "United States", city = "San Francisco", hostName = "13.113.62.150"))
        conns.add(ConnItem(name = "United States", city = "Chicago", hostName = "13.113.62.150"))
        conns.add(ConnItem(name = "Canada", city = "Toronto", hostName = "13.113.62.150"))
    }

    private fun getConnItem(): ConnItem {
        val connItem = conns.first { it.selected }
        return if ("Super Fast Server" == connItem.name) {
            val fasterItemName = fasters.random()
            val randomConnItem = try {
                conns.filter { it.name == fasterItemName }.random()
            } catch (e: Exception) {
                conns[Random.nextInt(1, conns.size)]
            }
            ConnItem(
                name = "Super Fast Server",
                hostName = randomConnItem.hostName,
                port = randomConnItem.port,
                way = randomConnItem.way,
                pwd = randomConnItem.pwd
            )
        } else connItem
    }

    fun switchOn() {
        val connItem = getConnItem()
        ProfileManager.createProfile().apply {
            name = app.getString(R.string.app_name)
            catName = connItem.name
            host = connItem.hostName
            password = connItem.pwd
            remotePort = connItem.port
            method = connItem.way
        }.also {
            ProfileManager.updateProfile(it)
            Core.switchProfile(it.id)
            Core.startService()
        }
    }
}
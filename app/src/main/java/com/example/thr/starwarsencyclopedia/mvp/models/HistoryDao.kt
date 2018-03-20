package com.example.thr.starwarsencyclopedia.mvp.models

import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import io.realm.Case
import io.realm.Realm

class HistoryDao {

    fun addData(selectedData: ItemBaseDetails){
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync {
            val rows = it.where(ItemBaseDetails::class.java)
                    .equalTo("url", selectedData.url)
                    .findAll()

            rows.deleteAllFromRealm()
            it.copyToRealm(selectedData)
        }
        realm.close()
    }

    fun loadAllData(): ArrayList<ItemBaseDetails> {
        val realm = Realm.getDefaultInstance()
        val allData = realm.copyFromRealm(realm.where(ItemBaseDetails::class.java).findAll().reversed())
        realm.close()

        return allData as ArrayList<ItemBaseDetails>
    }

    fun searchDataByName(nameFragment: String): ArrayList<ItemBaseDetails> {
        val realm = Realm.getDefaultInstance()
        val searchData = realm.copyFromRealm(realm.where(ItemBaseDetails::class.java)
                .contains("name", nameFragment, Case.INSENSITIVE).findAll())
        realm.close()

        return searchData as ArrayList<ItemBaseDetails>
    }

    fun deleteAllData() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync {
            val rows = it.where(ItemBaseDetails::class.java).findAll()
            rows.deleteAllFromRealm()
        }
        realm.close()
    }
}
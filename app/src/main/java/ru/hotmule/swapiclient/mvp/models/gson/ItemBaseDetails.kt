package ru.hotmule.swapiclient.mvp.models.gson

import android.os.Parcel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.util.*

open class ItemBaseDetails : RealmObject {

    @Expose
    @SerializedName("name", alternate = ["title"])
    lateinit var name: String
    @Expose
    lateinit var url: String
    @Expose
    lateinit var created: Date

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        url = parcel.readString()
    }

    constructor(name: String, url: String, created: Date) {
        this.name = name
        this.url = url
        this.created = created
    }

    constructor()
}
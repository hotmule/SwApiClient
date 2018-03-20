package com.example.thr.starwarsencyclopedia.mvp.models.gson

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.io.Serializable
import java.util.*

open class ItemBaseDetails : RealmObject, Parcelable {

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemBaseDetails> {
        override fun createFromParcel(parcel: Parcel): ItemBaseDetails {
            return ItemBaseDetails(parcel)
        }

        override fun newArray(size: Int): Array<ItemBaseDetails?> {
            return arrayOfNulls(size)
        }
    }
}
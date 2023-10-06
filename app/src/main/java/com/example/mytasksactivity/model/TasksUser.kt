package com.example.mytasksactivity.model

import android.os.Parcel
import android.os.Parcelable

data class TasksUser(
    val created_at: String,
    val id: Int,
    val is_done: Boolean,
    val student_id: Int,
    val title: String,
    val updated_at: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeInt(id)
        parcel.writeByte(if (is_done) 1 else 0)
        parcel.writeInt(student_id)
        parcel.writeString(title)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TasksUser> {
        override fun createFromParcel(parcel: Parcel): TasksUser {
            return TasksUser(parcel)
        }

        override fun newArray(size: Int): Array<TasksUser?> {
            return arrayOfNulls(size)
        }
    }

}
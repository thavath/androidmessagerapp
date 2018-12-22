package com.example.user.messagerapp.model

import java.io.Serializable
import java.util.*

class Room(var uid: String, var buildingId: String, var userId: String,
           var price: Double, var entryDate: Date, var startDate: Date,
           var endDate: Date, var roomImageUri: String, var roomSize: String, var roomStatus: Boolean) : Serializable {
    constructor() :this ("","","",0.0, Date(), Date(), Date(),"","",false)
}

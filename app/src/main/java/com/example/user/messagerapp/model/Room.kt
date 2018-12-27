package com.example.user.messagerapp.model

import java.io.Serializable

class Room(var uid: String, var buildingName: String, var userId: String,
           var price: Double, var roomImageUri: String, var roomSize: String, var roomDescription: String,
           var roomStatus: Boolean) : Serializable {
    constructor() :this ("","","",0.0, "", "","",false)
}

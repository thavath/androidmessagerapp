package com.example.user.messagerapp.model

import java.io.Serializable

class Building(var uid: String ,var userId: String, var buildingName: String ,var entryDate: String,
               var address: String ,var buildingStatus: Boolean, var buildingImage: String) : Serializable {
    constructor() :this ("","","", "","",false, "")
}

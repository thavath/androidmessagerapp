package com.example.user.messagerapp.model

import java.io.Serializable
import java.util.*

class Building(var uid: String, var userId: String, var entryDate: Date, var address: String ,var roomStatus: Boolean) : Serializable {
    constructor() :this ("","",Date(),"",false)
}

package com.example.user.messagerapp.model

import java.io.Serializable

class Booked (var uid: String, var roomId: String, var userId: String,
              var stayDate: String, var leaveDate: String, var bookedDate: String, var totalMonths: Double, var totalPeople: Int) : Serializable {
    constructor() :this ("","","","", "", "",0.0, 1)
}
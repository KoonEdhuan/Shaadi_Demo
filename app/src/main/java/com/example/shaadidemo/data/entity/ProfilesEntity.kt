package com.example.shaadidemo.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfilesEntity(
    @PrimaryKey val uuid: String,
    val gender: String,
    val email: String,
    val phone: String,
    val cell: String,
    val nat: String,

    @Embedded(prefix = "name_")
    val name: Name,

    @Embedded(prefix = "location_")
    val location: Location,

    @Embedded(prefix = "dob_")
    val dob: Dob,

    @Embedded(prefix = "registered_")
    val registered: Registered,

    @Embedded(prefix = "picture_")
    val picture: Picture,

    val education: String,
    val religion: String,
    val matchScore: Int,
    val matchStatus: String = "PENDING"
)

data class Name(val title: String, val first: String, val last: String)

data class Dob(val date: String, val age: Int)

data class Registered(val date: String, val age: Int)

data class Picture(val large: String, val medium: String, val thumbnail: String)

data class Location(
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    @Embedded val street: Street,
    @Embedded val coordinates: Coordinates,
    @Embedded val timezone: Timezone
)

data class Street(val number: Int, val name: String)
data class Coordinates(val latitude: String, val longitude: String)
data class Timezone(val offset: String, val description: String)

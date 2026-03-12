package com.example.shaadidemo.model

data class ProfilesResponse(
    val results: List<RemoteUser>,
    val info: Info
)

data class RemoteUser(
    val gender: String,
    val name: RemoteName,
    val location: RemoteLocation,
    val email: String,
    val login: RemoteLogin,
    val dob: RemoteDate,
    val registered: RemoteDate,
    val phone: String,
    val cell: String,
    val id: RemoteId,
    val picture: RemotePicture,
    val nat: String
)

data class RemoteName(
    val title: String,
    val first: String,
    val last: String
)

data class RemoteLocation(
    val street: RemoteStreet,
    val city: String,
    val state: String,
    val country: String,
    val postcode: Int,
    val coordinates: RemoteCoordinates,
    val timezone: RemoteTimezone
)

data class RemoteStreet(
    val number: Int,
    val name: String
)
data class RemoteCoordinates(
    val latitude: String,
    val longitude: String
)
data class RemoteTimezone(
    val offset: String,
    val description: String
)
data class RemoteLogin(
    val uuid: String
)
data class RemoteDate(
    val date: String,
    val age: Int
)
data class RemoteId(
    val name: String?,
    val value: String?
)
data class RemotePicture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
data class Info(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String
)

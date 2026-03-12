package com.example.shaadidemo.data.mapper

import com.example.shaadidemo.data.entity.Coordinates
import com.example.shaadidemo.data.entity.Dob
import com.example.shaadidemo.data.entity.Location
import com.example.shaadidemo.data.entity.Name
import com.example.shaadidemo.data.entity.Picture
import com.example.shaadidemo.data.entity.ProfilesEntity
import com.example.shaadidemo.data.entity.Registered
import com.example.shaadidemo.data.entity.Street
import com.example.shaadidemo.data.entity.Timezone
import com.example.shaadidemo.model.RemoteUser
import kotlin.math.abs

fun mapToEntity(remote: RemoteUser): ProfilesEntity {

    val education = listOf("Bachelor's", "Master's", "PhD").random()
    val religion = listOf("Hindu", "Christian", "Sikh").random()

    return ProfilesEntity(
        uuid = remote.login.uuid,
        gender = remote.gender,
        email = remote.email,
        phone = remote.phone,
        cell = remote.cell,
        nat = remote.nat,
        name = Name(remote.name.title, remote.name.first, remote.name.last),
        location = Location(
            city = remote.location.city,
            state = remote.location.state,
            country = remote.location.country,
            postcode = remote.location.postcode,
            street = Street(remote.location.street.number, remote.location.street.name),
            coordinates = Coordinates(
                remote.location.coordinates.latitude,
                remote.location.coordinates.longitude
            ),
            timezone = Timezone(
                remote.location.timezone.offset,
                remote.location.timezone.description
            )
        ),
        dob = Dob(remote.dob.date, remote.dob.age),
        registered = Registered(remote.registered.date, remote.registered.age),
        picture = Picture(remote.picture.large, remote.picture.medium, remote.picture.thumbnail),

        // Custom Fields
        education = education,
        religion = religion,
        matchScore = calculateScore(
            remote.dob.age,
            remote.location.city,
            remote.location.state,
            remote.location.country,
            education,
            religion
        )
    )
}

private fun calculateScore(
    age: Int,
    city: String,
    state: String,
    country: String,
    education: String,
    religion: String
): Int {
    val myAge = 58
    val myCity = "Courbevoie"
    val myState = "Bouches-du-Rhône"
    val myCountry = "France"
    val myEducation = "Master's"
    val myReligion = "Hindu"

    var totalScore = 0

    // Age Factor (Max 30 points)
    val ageDiff = abs(age - myAge)
    val ageScore = (30 - (ageDiff * 3)).coerceAtLeast(0)
    totalScore += ageScore

    // Location Factor (Max 40 points)
    if (country.equals(myCountry, ignoreCase = true)) {
        totalScore += 10
        if (state.equals(myState, ignoreCase = true)) {
            totalScore += 10
            if (city.equals(myCity, ignoreCase = true)) {
                totalScore += 20
            }
        }
    }

    // Education Factor (Max 15 points)
    if (education.equals(myEducation, ignoreCase = true)) {
        totalScore += 15
    } else if (education.isNotEmpty()) {
        totalScore += 10
    }

    // Religion Factor (Max 15 points)
    if (religion.equals(myReligion, ignoreCase = true)) {
        totalScore += 15
    }

    return totalScore.coerceIn(0, 100)
}
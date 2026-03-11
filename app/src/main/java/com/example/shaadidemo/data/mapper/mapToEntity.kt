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

//fun mapToEntity(remote: Result): ProfilesEntity {
//    return ProfilesEntity(
//        uuid = remote.login.uuid,
//        gender = remote.gender,
//        email = remote.email,
//        phone = remote.phone,
//        cell = remote.cell,
//        nat = remote.nat,
//        name = Name(remote.name.title, remote.name.first, remote.name.last),
//        location = Location(
//            city = remote.location.city,
//            state = remote.location.state,
//            country = remote.location.country,
//            postcode = remote.location.postcode.toString(),
//            street = Street(remote.location.street.number, remote.location.street.name),
//            coordinates = Coordinates(
//                remote.location.coordinates.latitude,
//                remote.location.coordinates.longitude
//            ),
//            timezone = Timezone(
//                remote.location.timezone.offset,
//                remote.location.timezone.description
//            )
//        ),
//        dob = Dob(remote.dob.date, remote.dob.age),
//        registered = Registered(remote.registered.date, remote.registered.age),
//        picture = Picture(remote.picture.large, remote.picture.medium, remote.picture.thumbnail),
//
//        // Custom Fields
//        education = listOf("Bachelor's", "Master's", "PhD").random(),
//        religion = listOf("Hindu", "Christian", "Sikh", "Muslim").random(),
//        matchScore = calculateScore(remote.dob.age, remote.location.city)
//    )
//}
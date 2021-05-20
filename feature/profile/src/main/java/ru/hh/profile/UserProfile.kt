package ru.hh.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserProfile(
    val id: String,
    val name: String,
    val photoUrl: String
) : Parcelable {

    companion object {
        val predefined1 = UserProfile(
            id = "1",
            name = "First profile",
            photoUrl = "https://gravatar.com/avatar/aaf90758048d52c2c6c3ba9aba0388d6?s=400&d=robohash&r=x"
        )

        val predefined2 = UserProfile(
            id = "2",
            name = "Second profile",
            photoUrl = "https://gravatar.com/avatar/9533d95bc106af86ba8b010fc7056fc1?s=400&d=robohash&r=x"
        )
    }

}
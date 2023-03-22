package com.adikmt.orm.helperfuncs

import com.adikmt.dtos.UserFollowingData
import com.adikmt.dtos.UserName
import com.adikmt.dtos.UserResponse
import com.adikmt.orm.UserEntity
import org.jetbrains.exposed.sql.ResultRow


fun ResultRow.fromResultRow(): UserResponse = UserResponse(
    userId = this[UserEntity.id].value,
    userName = this[UserEntity.username],
    userEmail = this[UserEntity.email],
    userBio = this[UserEntity.bio]
)

fun toFollowerData(
    userName: UserName,
    usersFollowing: List<UserResponse>,
    othersFollowing: List<UserResponse>
): UserFollowingData = UserFollowingData(
    userName = userName.value,
    usersFollowing = usersFollowing,
    noUsersFollowing = usersFollowing.size,
    noOthersFollowingUser = othersFollowing.size,
    othersFollowingUser = othersFollowing
)
package com.huseyincan.financemobile.data.model

data class UserResponse(
    val user: User,
    val portfolios: List<Portfolio>
)

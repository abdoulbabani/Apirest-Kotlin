package com.example.auth.repositories

import com.example.auth.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserModel,Int> {
    fun findByEmail(email:String):UserModel?

}
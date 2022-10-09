package com.example.auth.services

import com.example.auth.models.UserModel
import com.example.auth.repositories.UserRepository
import org.springframework.stereotype.Service


@Service
class UserService(private val userRepository: UserRepository) {

    fun save(userModel: UserModel) :UserModel{
        return userRepository.save(userModel)
    }

    fun findByEmail(email:String):UserModel?{
        return userRepository.findByEmail(email);
    }

    fun findById(id:Int):UserModel{
        return userRepository.findById(id).orElse(null)
    }
}
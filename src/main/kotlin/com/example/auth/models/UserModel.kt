package com.example.auth.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column
    var name = ""

    @Column(unique = true)
    var email = ""


    @Column
    var password = ""
        @JsonIgnore
        get() = field
        set(value) {
            val passwordEncode = BCryptPasswordEncoder();
            field = passwordEncode.encode(value);
        }

    fun comparepasword(password:String):Boolean{
        return BCryptPasswordEncoder().matches(password,this.password);
    }


}
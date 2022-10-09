package com.example.auth.controllers

import com.example.auth.dto.LoginDto
import com.example.auth.dto.RegisterDto
import com.example.auth.exception.Message
import com.example.auth.models.UserModel
import com.example.auth.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("api")
class AutoController(private val userService :UserService) {

    @PostMapping("register")
    fun register(@RequestBody registerDto: RegisterDto):ResponseEntity<UserModel>{
        val user = UserModel ();
        user.email=registerDto.email;
        user.password=registerDto.password;
        user.name=registerDto.name;
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("login")
    fun login(@RequestBody loginDto: LoginDto, response: HttpServletResponse):ResponseEntity<Any>{
       val user=userService.findByEmail(loginDto.email)?:return ResponseEntity.badRequest().body(Message("user not found"))

        if(!user.comparepasword(loginDto.password)) return ResponseEntity.badRequest().body(Message("password invalid"));

             val issuer=user.id.toString();
             val jwt= Jwts.builder().setIssuer(issuer).setExpiration(Date(System.currentTimeMillis()+60*24*1000))
                 .signWith(SignatureAlgorithm.HS256, "secret").compact()

            val cookie=Cookie("jwt",jwt);
            cookie.isHttpOnly=true
        response.addCookie(cookie)

        return ResponseEntity.ok(Message("succes"))
    }
    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt:String?):ResponseEntity<Any>{
        try {
            if(jwt==null)
            {
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }
            val body=Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
            return ResponseEntity.ok(this.userService.findById(body.issuer.toInt()))

        }catch (e:Exception){
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }

    }
    @PostMapping("logout")
    fun logout(response: HttpServletResponse):ResponseEntity<Any>{
        var cookie=Cookie("jwt","")
        cookie.maxAge=0
        response.addCookie(cookie)
        return ResponseEntity.ok(Message("succes"))
    }
}
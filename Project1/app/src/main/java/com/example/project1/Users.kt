package com.example.project1

object Users {
    private val users = mutableListOf<UserModel>()
    private var currentUser: UserModel? = null

    fun addUser(user:UserModel){
        users.add(user)
        this.currentUser = user
    }

    fun login(profile:UserModel):Boolean{
        users.forEach{
            if(profile.userName == it.userName && profile.password == it.password){
                currentUser = it
                return true
            }
        }
        return false
    }

    fun logout(){
        currentUser = null
    }

    fun getCurrentUser():UserModel?{
        return currentUser
    }
}
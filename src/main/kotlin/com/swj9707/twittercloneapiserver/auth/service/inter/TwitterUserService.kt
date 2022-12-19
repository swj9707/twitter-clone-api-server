package com.swj9707.twittercloneapiserver.auth.service.inter

import com.swj9707.twittercloneapiserver.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.auth.dto.UserResDTO


interface TwitterUserService {
    fun existsUser(email : String) : Boolean

    fun createUser(userRegistReq: UserReqDTO.Req.Register) : UserResDTO.Res.Register

    fun editUserProfile(editProfileReq : UserReqDTO.Req.EditProfile) : UserResDTO.Res.EditProfile

    fun editUserPassword(editUserPasswordReq : UserReqDTO.Req.EditPassword) : UserResDTO.Res.EditPassword

    fun login(req: UserReqDTO.Req.Login) : UserResDTO.Res.Login

    fun reissue(refreshToken : String, accessToken : String) : UserResDTO.Res.TokenInfo

    fun logout(accessToken: String) : UserResDTO.Res.Logout
}
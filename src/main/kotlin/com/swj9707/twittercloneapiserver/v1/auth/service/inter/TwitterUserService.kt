package com.swj9707.twittercloneapiserver.v1.auth.service.inter

import com.swj9707.twittercloneapiserver.v1.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.auth.dto.UserResDTO
import java.util.*


interface TwitterUserService {
    fun createUser(userRegistReq: UserReqDTO.Req.Register) : UserResDTO.Res.Register

    fun editUserProfile(editProfileReq : UserReqDTO.Req.EditProfile) : UserResDTO.Res.EditProfile

    fun editUserPassword(editUserPasswordReq : UserReqDTO.Req.EditPassword) : UserResDTO.Res.EditPassword

    fun login(req: UserReqDTO.Req.Login) : UserResDTO.Res.Login

    fun reissue(refreshToken : String) : UserResDTO.Res.TokenInfo

    fun logout(accessToken: String) : UserResDTO.Res.Logout
}
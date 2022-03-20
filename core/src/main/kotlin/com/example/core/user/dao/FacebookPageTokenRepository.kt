package com.example.core.user.dao

import com.example.core.model.FacebookPageToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FacebookPageTokenRepository: JpaRepository<FacebookPageToken, Long> {

}
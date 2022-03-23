package com.example.core.utils

import com.example.core.model.socialmedia.PostDto
import com.example.core.model.socialmedia.PublishPostDto
import com.restfb.Parameter
import org.springframework.stereotype.Component

@Component
class PublishPostDtoMapper {


    fun mapToFacebookParams(dto: PublishPostDto): Array<Parameter> {
        return arrayOf(
            Parameter.with("message", dto.content),
        )
    }
}
package com.example.core.mapper

import com.example.core.dto.PublishPostDto
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
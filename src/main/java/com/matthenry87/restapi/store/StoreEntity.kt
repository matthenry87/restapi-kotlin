package com.matthenry87.restapi.store

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class StoreEntity(
        @Id
        var id: String? = null,
        var name: String? = null,
        var address: String? = null,
        var phone: String? = null,
        var status: Status? = null
)

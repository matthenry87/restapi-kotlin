package com.matthenry87.restapi.store

import com.matthenry87.restapi.store.StoreController.CreateStore
import com.matthenry87.restapi.store.StoreController.UpdateStore
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class StoreModel (

    var id: String? = null,
    @field:NotEmpty(groups = [UpdateStore::class, CreateStore::class]) var name: String? = null,
    @field:NotEmpty(groups = [UpdateStore::class, CreateStore::class])var address: String? = null,
    @field:NotEmpty(groups = [UpdateStore::class, CreateStore::class])var phone: String? = null,
    @field:NotNull(groups = [UpdateStore::class]) var status: Status? = null
)

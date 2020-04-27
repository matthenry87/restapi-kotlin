package com.matthenry87.restapi.store

import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface StoreMapper {

    fun toEntity(store: StoreModel): StoreEntity

    fun toModel(storeEntity: StoreEntity): StoreModel

}
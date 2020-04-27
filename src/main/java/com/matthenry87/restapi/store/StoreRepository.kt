package com.matthenry87.restapi.store

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoreRepository : MongoRepository<StoreEntity, String> {

    fun findByName(name: String): Optional<StoreEntity>

    fun findByNameAndIdNot(name: String, id: String): Optional<StoreEntity>

}
package com.matthenry87.restapi.store

import com.matthenry87.restapi.exception.AlreadyExistsException
import com.matthenry87.restapi.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {

    val stores: List<StoreEntity>
        get() = storeRepository.findAll()

    fun getStore(id: String): StoreEntity {

        return storeRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun createStore(store: StoreEntity) {

        storeRepository.findByName(store.name!!)
                .ifPresent { throw AlreadyExistsException() }

        store.status = Status.OPEN

        storeRepository.save(store)
    }

    fun updateStore(store: StoreEntity) {

        storeRepository.findByNameAndIdNot(store.name!!, store.id!!)
                .ifPresent { throw AlreadyExistsException("store with name already exists") }

        storeRepository.findById(store.id)
                .orElseThrow { NotFoundException() }

        storeRepository.save(store)
    }

    fun deleteStore(id: String) {

        storeRepository.findById(id)
                .orElseThrow { NotFoundException() }

        storeRepository.deleteById(id)
    }

}
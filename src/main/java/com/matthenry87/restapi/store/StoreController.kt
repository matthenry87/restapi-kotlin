package com.matthenry87.restapi.store

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/store")
class StoreController(private val storeService: StoreService, private val storeMapper: StoreMapper) {

    @GetMapping
    fun get(): List<StoreModel> {

        return storeService.stores.stream()
                .map { storeEntity: StoreEntity -> storeMapper.toModel(storeEntity) }
                .collect(Collectors.toList())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String?): StoreModel? {

        val storeEntity = storeService.getStore(id!!)

        return storeMapper.toModel(storeEntity)
    }

    @PostMapping
    fun post(@RequestBody @Validated(CreateStore::class) storeModel: StoreModel): ResponseEntity<StoreModel> {

        val storeEntity = storeMapper.toEntity(storeModel)

        storeService.createStore(storeEntity)

        storeModel.id = storeEntity.id
        storeModel.status = Status.OPEN

        return ResponseEntity(storeModel, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun put(@RequestBody @Validated(UpdateStore::class) storeModel: StoreModel, @PathVariable id: String): StoreModel {

        storeModel.id = id

        val storeEntity = storeMapper.toEntity(storeModel)

        storeService.updateStore(storeEntity)

        return storeModel
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String?): ResponseEntity<Any> {

        storeService.deleteStore(id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    internal interface UpdateStore
    internal interface CreateStore

}

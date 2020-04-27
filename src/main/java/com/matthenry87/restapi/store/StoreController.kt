package com.matthenry87.restapi.store;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    public StoreController(StoreService storeService, StoreMapper storeMapper) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
    }

    @GetMapping
    public List<StoreModel> get() {

        return storeService.getStores().stream()
                .map(storeMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StoreModel getById(@PathVariable String id) {

        var storeEntity = storeService.getStore(id);

        return storeMapper.toModel(storeEntity);
    }

    @PostMapping
    public ResponseEntity<StoreModel> post(@RequestBody @Validated(CreateStore.class) StoreModel storeModel) {

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.createStore(storeEntity);

        storeModel.setId(storeEntity.getId());
        storeModel.setStatus(Status.OPEN);

        return new ResponseEntity<>(storeModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public StoreModel put(@RequestBody @Validated(UpdateStore.class) StoreModel storeModel, @PathVariable String id) {

        storeModel.setId(id);

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.updateStore(storeEntity);

        return storeModel;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {

        storeService.deleteStore(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

interface UpdateStore {}
interface CreateStore {}

}

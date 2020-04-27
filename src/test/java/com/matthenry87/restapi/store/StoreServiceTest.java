package com.matthenry87.restapi.store;

import com.matthenry87.restapi.exception.AlreadyExistsException;
import com.matthenry87.restapi.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    private StoreService storeService;

    @BeforeEach
    void init() {

        MockitoAnnotations.initMocks(this);

        storeService = new StoreService(storeRepository);
    }

    @Test
    void getStores_works() {
        // Act
        storeService.getStores();

        // Assert
        verify(storeRepository).findAll();
    }

    @Test
    void getStore_works() {
        // Arrange
        var id = "id";

        when(storeRepository.findById(id)).thenReturn(Optional.of(new StoreEntity()));

        // Act
        storeService.getStore(id);

        // Assert
        verify(storeRepository).findById(id);
    }

    @Test
    void getStore_throwsDoesntExistException_whenNoStoreFound() {
        // Arrange
        var id = "id";

        when(storeRepository.findById(id)).thenReturn(Optional.empty());

        // Act/Assert
        assertThrows(NotFoundException.class, () -> storeService.getStore(id));
    }

    @Test
    void createStore_works() {
        // Arrange
        var store = new StoreEntity();
        store.setName("name");

        // Act
        storeService.createStore(store);

        // Assert
        verify(storeRepository).findByName(store.getName());
        verify(storeRepository).save(store);
    }

    @Test
    void createStore_throwsAlreadyExistsException_whenStoreNameExists() {
        // Arrange
        var store = new StoreEntity();
        store.setName("name");

        when(storeRepository.findByName(store.getName())).thenReturn(Optional.of(store));

        // Act/Assert
        assertThrows(AlreadyExistsException.class, () -> storeService.createStore(store));
    }

    @Test
    void updateStore_works() {
        // Arrange
        var store = new StoreEntity();
        store.setId("id");
        store.setName("name");
        store.setStatus(Status.OPEN);

        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // Act
        storeService.updateStore(store);

        // Assert
        verify(storeRepository).findById(store.getId());
        verify(storeRepository).save(store);
    }

    @Test
    void updateStore_throwsDoesntExistException_whenStoreNameExists() {
        // Arrange
        var store = new StoreEntity();
        store.setName("name");

        when(storeRepository.findByName(store.getName())).thenReturn(Optional.of(store));

        // Act/Assert
        assertThrows(AlreadyExistsException.class, () -> storeService.createStore(store));
    }

    @Test
    void deleteStore_works() {
        // Arrange
        var id = "id";

        var store = new StoreEntity();
        store.setId(id);

        when(storeRepository.findById(id)).thenReturn(Optional.of(store));

        // Act
        storeService.deleteStore(id);

        // Assert
        verify(storeRepository).deleteById(id);
    }

    @Test
    void deleteStore_throwsDoesntExistException_whenStoreDoesntExist() {
        // Arrange
        var name = "name";

        when(storeRepository.findByName(name)).thenReturn(Optional.empty());

        // Act/Assert
        assertThrows(NotFoundException.class, () -> storeService.deleteStore(name));
    }

}
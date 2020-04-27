package com.matthenry87.restapi.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthenry87.restapi.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {

        MockitoAnnotations.initMocks(this);

        var storeController = new StoreController(storeService, storeMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(storeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void get_works() throws Exception {
        // Arrange
        when(storeService.getStores()).thenReturn(Arrays.asList(new StoreEntity(), new StoreEntity()));
        when(storeMapper.toModel(any(StoreEntity.class))).thenReturn(new StoreModel(), new StoreModel());

        // Act/Assert
        mockMvc.perform(get("/store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getById_works() throws Exception {
        // Arrange
        var storeEntity = new StoreEntity();
        var storeModel = createStoreModel();

        when(storeService.getStore("1")).thenReturn(storeEntity);
        when(storeMapper.toModel(storeEntity)).thenReturn(storeModel);

        // Act/Assert
        mockMvc.perform(get("/store/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"address\":\"123 High St\",\"name\":\"Store Name\"," +
                        "\"phone\":\"3039993456\",\"status\":\"OPEN\"}"));

        verify(storeService).getStore("1");
        verify(storeMapper).toModel(storeEntity);
    }

    @Test
    void post_works() throws Exception {
        // Arrange
        var storeModel = createStoreModel();

        var json = objectMapper.writeValueAsString(storeModel);

        var storeEntity = new StoreEntity();

        when(storeMapper.toEntity(any(StoreModel.class))).thenReturn(storeEntity);

        // Act/Assert
        mockMvc.perform(post("/store")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"address\":\"123 High St\",\"name\":\"Store Name\"," +
                        "\"phone\":\"3039993456\",\"status\":\"OPEN\"}"));

        verify(storeService).createStore(storeEntity);
    }

    @Test
    void put_works() throws Exception {
        // Arrange
        var storeModel = createStoreModel();

        var json = objectMapper.writeValueAsString(storeModel);

        var storeEntity = new StoreEntity();

        when(storeMapper.toEntity(any(StoreModel.class))).thenReturn(storeEntity);

        // Act/Assert
        mockMvc.perform(put("/store/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"address\":\"123 High St\",\"name\":\"Store Name\"," +
                        "\"phone\":\"3039993456\",\"status\":\"OPEN\"}"));

        verify(storeService).updateStore(storeEntity);
    }

    @Test
    void put_Returns400_whenStatusOmitted() throws Exception {
        // Arrange
        var storeModel = createStoreModel();
        storeModel.setStatus(null);

        var json = objectMapper.writeValueAsString(storeModel);

        var storeEntity = new StoreEntity();

        when(storeMapper.toEntity(any(StoreModel.class))).thenReturn(storeEntity);

        // Act/Assert
        mockMvc.perform(put("/store/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void put_Returns400_whenAddressOmitted() throws Exception {
        // Arrange
        var storeModel = createStoreModel();
        storeModel.setAddress(null);

        var json = objectMapper.writeValueAsString(storeModel);

        var storeEntity = new StoreEntity();

        when(storeMapper.toEntity(any(StoreModel.class))).thenReturn(storeEntity);

        // Act/Assert
        mockMvc.perform(put("/store/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_works() throws Exception {
        // Act/Assert
        mockMvc.perform(delete("/store/1"))
                .andExpect(status().isNoContent());

        verify(storeService).deleteStore("1");
    }

    private StoreModel createStoreModel() {

        var storeModel = new StoreModel();

        storeModel.setName("Store Name");
        storeModel.setAddress("123 High St");
        storeModel.setPhone("3039993456");
        storeModel.setStatus(Status.OPEN);

        return storeModel;
    }

}
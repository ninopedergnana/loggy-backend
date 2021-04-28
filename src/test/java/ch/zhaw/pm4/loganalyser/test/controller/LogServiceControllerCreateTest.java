package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import ch.zhaw.pm4.loganalyser.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogServiceControllerCreateTest {

    @MockBean
    LogServiceService logServiceService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogService() {
        // prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogService.json");

        doNothing().when(logServiceService).createLogService(any());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).createLogService(any());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogServiceWithCorruptedJSON() {
        //prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogServiceWithCorruptedJSON.json");

        doNothing().when(logServiceService).createLogService(any());

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.METHOD_ARGUMENT_NOT_VALID_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[*]", containsInAnyOrder(
                            LogServiceDTO.LOG_DIRECTORY_VALIDATION_MESSAGE,
                            LogServiceDTO.NAME_VALIDATION_MESSAGE,
                            LogServiceDTO.LOG_CONFIG_VALIDATION_MESSAGE,
                            LogServiceDTO.LOCATION_VALIDATION_MESSAGE
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        //verify
        verify(logServiceService, times(0)).createLogService(any());
    }

    @Test
    void testCreateLogServiceWithNonExistingLogConfig() {
        //prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogServiceWithNonExistingLogConfig.json");
        String exceptionMessage = "Log config does not exists";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logServiceService).createLogService(any());

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.RECORD_NOT_FOUND_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        //verify
        verify(logServiceService, times(1)).createLogService(any());
    }

}

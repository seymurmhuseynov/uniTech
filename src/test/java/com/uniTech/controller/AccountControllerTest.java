package com.uniTech.controller;

import com.uniTech.models.RequestTransfer;
import com.uniTech.models.ResponseData;
import com.uniTech.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService mockAccountService;

    @Test
    void testMyAccounts() throws Exception {
        doReturn(new ResponseData<>(null)).when(mockAccountService).selectMyAccount(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/myAccounts")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testMyAccounts_AccountServiceReturnsNoItem() throws Exception {
        // Setup
        doReturn(ResponseData.ok()).when(mockAccountService).selectMyAccount(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/myAccounts")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testMyAccounts_AccountServiceReturnsError() throws Exception {
        // Setup
        doReturn(ResponseData.error(null, HttpStatus.OK)).when(mockAccountService).selectMyAccount(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/myAccounts")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testTransfer() throws Exception {
        doReturn(new ResponseData<>(null)).when(mockAccountService).transfer(any(RequestTransfer.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/account/transfer")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testTransfer_AccountServiceReturnsNoItem() throws Exception {
        doReturn(ResponseData.ok()).when(mockAccountService).transfer(any(RequestTransfer.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/account/transfer")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testTransfer_AccountServiceReturnsError() throws Exception {
        doReturn(ResponseData.error(null, HttpStatus.OK)).when(mockAccountService).transfer(any(RequestTransfer.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/account/transfer")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectedCurrency() throws Exception {
        doReturn(new ResponseData<>(null)).when(mockAccountService).selectedCurrency(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/currency")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSelectedCurrency_AccountServiceReturnsNoItem() throws Exception {
        doReturn(ResponseData.ok()).when(mockAccountService).selectedCurrency(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/currency")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testSelectedCurrency_AccountServiceReturnsError() throws Exception {
        doReturn(ResponseData.error(null, HttpStatus.OK)).when(mockAccountService).selectedCurrency(
                new UsernamePasswordAuthenticationToken("principal", "credentials", List.of()));

        final MockHttpServletResponse response = mockMvc.perform(get("/uni-tech/api/v1/account/currency")
                        .with(user("username"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}

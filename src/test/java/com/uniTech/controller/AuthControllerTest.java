package com.uniTech.controller;

import com.uniTech.entities.User;
import com.uniTech.models.RequestPin;
import com.uniTech.models.ResponseData;
import com.uniTech.repos.UserRepository;
import com.uniTech.security.JwtProvider;
import com.uniTech.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider mockJwtProvider;
    @MockBean
    private AuthenticationManager mockAuthenticationManager;
    @MockBean
    private UserRepository mockUserRepository;
    @MockBean
    private UserService mockUserService;

    @Test
    void testAuthenticateUser() throws Exception {
        when(mockAuthenticationManager.authenticate(null)).thenReturn(null);
        when(mockJwtProvider.generateJwtToken(null)).thenReturn("token");

        final Optional<User> user = Optional.of(
                new User(0L, "pin", "password", LocalDateTime.of(2020, 1, 1, 0, 0, 0), false));
        when(mockUserRepository.findByPin("pin")).thenReturn(user);

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/signin")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testAuthenticateUser_AuthenticationManagerThrowsAuthenticationException() throws Exception {
        when(mockAuthenticationManager.authenticate(null)).thenThrow(AuthenticationException.class);

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/signin")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testAuthenticateUser_UserRepositoryReturnsAbsent() throws Exception {
        when(mockAuthenticationManager.authenticate(null)).thenReturn(null);
        when(mockJwtProvider.generateJwtToken(null)).thenReturn("token");
        when(mockUserRepository.findByPin("pin")).thenReturn(Optional.empty());

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/signin")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testRegister() throws Exception {
        doReturn(new ResponseData<>(null)).when(mockUserService).register(any(RequestPin.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/register")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testRegister_UserServiceReturnsNoItem() throws Exception {
        doReturn(ResponseData.ok()).when(mockUserService).register(any(RequestPin.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/register")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testRegister_UserServiceReturnsError() throws Exception {
        doReturn(ResponseData.error(null, HttpStatus.OK)).when(mockUserService).register(any(RequestPin.class));

        final MockHttpServletResponse response = mockMvc.perform(post("/uni-tech/api/v1/auth/register")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}

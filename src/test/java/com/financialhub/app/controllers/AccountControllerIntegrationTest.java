package com.financialhub.app.controllers;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.exceptions.AccountException;
import com.financialhub.app.services.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("GET /api/v1/accounts - Success")
    void testGetAllAccountsSuccess() throws Exception {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(1000.0);

        when(accountService.getAllAccounts()).thenReturn(List.of(accountResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Accounts retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].accountHolderName").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/accounts/{id} - Account Found")
    void testGetAccountByIdFound() throws Exception {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(1000.0);

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(accountResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account found successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.accountHolderName").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/accounts/{id} - Account Not Found")
    void testGetAccountByIdNotFound() throws Exception {
        when(accountService.getAccountById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account not found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/accounts - Create Account Success")
    void testCreateAccountSuccess() throws Exception {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setAccountHolderName("John Doe");
        accountRequestDto.setType("Savings");
        accountRequestDto.setBalance(1000.0);

        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(1000.0);

        when(accountService.createAccount(any(AccountRequestDto.class))).thenReturn(Optional.of(accountResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolderName\":\"John Doe\", \"type\":\"Savings\", \"balance\":1000.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.accountHolderName").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/accounts/searchBy - Accounts Found")
    void testGetAccountsByAccountHolderNameFound() throws Exception {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(1000.0);

        when(accountService.getAccountsByAccountHolderName("John Doe")).thenReturn(List.of(accountResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/searchBy")
                        .param("accountHolderName", "John Doe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Accounts found successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].accountHolderName").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/accounts/searchBy - No Accounts Found")
    void testGetAccountsByAccountHolderNameNotFound() throws Exception {
        when(accountService.getAccountsByAccountHolderName("Jane Doe")).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/searchBy")
                        .param("accountHolderName", "Jane Doe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("No accounts found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/accounts - Create Account Error")
    void testCreateAccountError() throws Exception {
        when(accountService.createAccount(any(AccountRequestDto.class)))
                .thenThrow(new AccountException("Account with the same type and name of holder already exists."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolderName\":\"John Doe\", \"type\":\"Savings\", \"balance\":1000.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Error creating account: Account with the same type and name of holder already exists."));
    }

    @Test
    @DisplayName("PUT /api/v1/accounts/{id} - Update Account Success")
    void testUpdateAccountSuccess() throws Exception {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe Updated");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(2000.0);

        when(accountService.updateAccount(anyLong(), any(AccountRequestDto.class))).thenReturn(accountResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolderName\":\"John Doe Updated\", \"type\":\"Savings\", \"balance\":2000.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.accountHolderName").value("John Doe Updated"));
    }

    @Test
    @DisplayName("PUT /api/v1/accounts/{id} - Update Account Not Found")
    void testUpdateAccountNotFound() throws Exception {
        when(accountService.updateAccount(anyLong(), any(AccountRequestDto.class)))
                .thenThrow(new AccountException("Account not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolderName\":\"John Doe Updated\", \"type\":\"Savings\", \"balance\":2000.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Error updating account: Account not found"));
    }

    @Test
    @DisplayName("DELETE /api/v1/accounts/{id} - Delete Account Success")
    void testDeleteAccountSuccess() throws Exception {
        when(accountService.getAccountById(anyLong())).thenReturn(Optional.of(new AccountResponseDto()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account deleted successfully"));
    }

    @Test
    @DisplayName("DELETE /api/v1/accounts/{id} - Delete Account Not Found")
    void testDeleteAccountNotFound() throws Exception {
        when(accountService.getAccountById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Account not found"));
    }
}
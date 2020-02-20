package fr.polytech.melusine.services;

import fr.polytech.melusine.TestData;
import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmailManager emailManager;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService(accountRepository, emailManager);
    }

    @Test
    public void test_updateAccount() {
        String clientId = "clientId";
        AccountRequest request = TestData.ACCOUNT_REQUEST_BRUCE_WAYNE;
        Account account = TestData.ACCOUNT_BRUCE_WAYNE.toBuilder()
                .password("botmon")
                .build();

        when(accountRepository.findById(eq(clientId))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmail(eq(request.getEmail()))).thenReturn(false);

        accountService.updateAccount(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertThat(captor.getValue().getEmail()).isEqualTo(request.getEmail());
        // password isn't updated yet
        assertThat(captor.getValue().getPassword()).isEqualTo(account.getPassword());
        assertThat(captor.getValue().isBarman()).isEqualTo(request.isBarman());
    }

    @Test
    public void test_updateAccount_throwException() {
        String clientId = "clientId";
        AccountRequest request = TestData.ACCOUNT_REQUEST_BRUCE_WAYNE;
        Account account = TestData.ACCOUNT_BRUCE_WAYNE;

        when(accountRepository.findById(eq(clientId))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmail(eq(request.getEmail()))).thenReturn(true);

        assertThatExceptionOfType(ConflictException.class)
                .isThrownBy(() -> accountService.updateAccount(request))
                .withMessage("An account already exists with this email: bruce.wayne@gmail.com");
    }

}
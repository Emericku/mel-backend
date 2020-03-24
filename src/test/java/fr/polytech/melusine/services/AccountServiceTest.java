package fr.polytech.melusine.services;

import fr.polytech.melusine.TestData;
import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.repositories.AccountRepository;
import fr.polytech.melusine.repositories.UserRepository;
import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.ZoneOffset;
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
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;
    @Mock
    private EmailManager emailManager;
    @Mock
    private Clock clock;

    private AccountService accountService;

    @Before
    public void setUp() {
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(clock.instant()).thenReturn(TestData.INSTANT_1.toInstant());
        accountService = new AccountService(accountRepository, userRepository, passwordService, emailManager, clock);
    }

    @Test
    public void test_updateAccount() {
        String clientId = "clientId";
        AccountRequest request = TestData.ACCOUNT_REQUEST_BRUCE_WAYNE;
        Account account = TestData.ACCOUNT_BRUCE_WAYNE.toBuilder()
                .password("botmon")
                .build();

        when(userRepository.findById(eq(clientId))).thenReturn(Optional.of(TestData.USER_BRUCE_WAYNE));
        when(accountRepository.findByUser(eq(TestData.USER_BRUCE_WAYNE))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmailAndUserIsNot(eq(request.getEmail()), eq(TestData.USER_BRUCE_WAYNE))).thenReturn(false);
        when(passwordService.encryptPassword(eq(request.getPassword().trim()))).thenReturn(request.getPassword());

        accountService.updateAccount(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertThat(captor.getValue().getEmail()).isEqualTo(request.getEmail());
        assertThat(captor.getValue().getPassword()).isEqualTo(request.getPassword());
        assertThat(captor.getValue().isBarman()).isEqualTo(request.isBarman());
    }

    @Test
    public void test_updateAccount_throwException() {
        String clientId = "clientId";
        AccountRequest request = TestData.ACCOUNT_REQUEST_BRUCE_WAYNE;
        Account account = TestData.ACCOUNT_BRUCE_WAYNE;

        when(userRepository.findById(eq(clientId))).thenReturn(Optional.of(TestData.USER_BRUCE_WAYNE));
        when(accountRepository.findByUser(eq(TestData.USER_BRUCE_WAYNE))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmailAndUserIsNot(eq(request.getEmail()), eq(TestData.USER_BRUCE_WAYNE))).thenReturn(true);

        assertThatExceptionOfType(ConflictException.class)
                .isThrownBy(() -> accountService.updateAccount(request))
                .withMessage("Le compte avec l'email : bruce.wayne@gmail.com est déjà existant");
    }

}
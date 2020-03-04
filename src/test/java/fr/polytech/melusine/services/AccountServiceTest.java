package fr.polytech.melusine.services;

import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.repositories.AccountRepository;
import fr.polytech.melusine.repositories.UserRepository;
import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;

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
        accountService = new AccountService(accountRepository, userRepository, passwordService, emailManager, clock);
    }

    @Test
    public void test_updateAccount() {
    }
    /**
     @Test public void test_updateAccount() {
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

     @Test public void test_updateAccount_throwException() {
     String clientId = "clientId";
     AccountRequest request = TestData.ACCOUNT_REQUEST_BRUCE_WAYNE;
     Account account = TestData.ACCOUNT_BRUCE_WAYNE;

     when(accountRepository.findById(eq(clientId))).thenReturn(Optional.of(account));
     when(accountRepository.existsByEmail(eq(request.getEmail()))).thenReturn(true);

     assertThatExceptionOfType(ConflictException.class)
     .isThrownBy(() -> accountService.updateAccount(request))
     .withMessage("Le compte avec l'email : bruce.wayne@gmail.com est déjà existant");
     }
     **/

}
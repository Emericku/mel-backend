package fr.polytech.melusine.services;

import fr.polytech.melusine.components.EmailManager;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.Section;
import fr.polytech.melusine.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    public static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(2020, 02, 17, 13, 06, 0, 0, ZoneOffset.UTC);

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmailManager emailManager;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService(accountRepository, emailManager);
    }

    private Account givenAccount(String accountId) {
        User user = User.builder()
                .id("userId")
                .firstName("Burce")
                .lastName("Wayne")
                .nickName("Batman")
                .section(Section.EXTERNAL)
                .credit(1000)
                .isMembership(true)
                .createdAt(OFFSET_DATE_TIME)
                .updatedAt(OFFSET_DATE_TIME)
                .build();

        return Account.builder()
                .id(accountId)
                .email("brce.wayne@gmail.com")
                .password("botmon")
                .isBarman(false)
                .user(user)
                .createdAt(OFFSET_DATE_TIME)
                .updatedAt(OFFSET_DATE_TIME)
                .build();
    }

    private AccountRequest givenAccountRequest() {
        return AccountRequest.builder()
                .email("bruce.wayne@gmail.com")
                .isBarman(true)
                .password("batman")
                .build();
    }

    @Test
    public void test_updateAccount() {
        String accountId = "accountId";
        AccountRequest request = givenAccountRequest();
        Account account = givenAccount(accountId);

        when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmail(eq(request.getEmail()))).thenReturn(false);

        accountService.updateAccount(accountId, request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertThat(captor.getValue().getEmail()).isEqualTo(request.getEmail());
        // password isn't updated yet
        assertThat(captor.getValue().getPassword()).isEqualTo(account.getPassword());
        assertThat(captor.getValue().isBarman()).isEqualTo(request.isBarman());
    }

    @Test
    public void test_updateAccount_throwException() {
        String accountId = "accountId";
        AccountRequest request = givenAccountRequest();
        Account account = givenAccount(accountId);

        when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        when(accountRepository.existsByEmail(eq(request.getEmail()))).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> accountService.updateAccount(accountId, request),
                "wtf");
    }

}
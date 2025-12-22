package com.app.warezilla.service;

import com.app.warezilla.model.User;
import com.app.warezilla.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUserEncodesPasswordSetsRoleAndSaves() {
        User user = User.builder()
                .userName("john")
                .password("plain")
                .build();

        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        userService.saveUser(user);

        verify(passwordEncoder).encode("plain");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("encoded", saved.getPassword());
        assertNotNull(saved.getRoles());
        assertEquals(1, saved.getRoles().size());
        assertEquals("USER", saved.getRoles().get(0));
    }

    @Test
    void createAdminSetsRolesAndSaves() {
        User user = User.builder()
                .userName("admin")
                .password("adminpass")
                .build();

        when(passwordEncoder.encode("adminpass")).thenReturn("enc-admin");

        userService.createAdmin(user);

        verify(passwordEncoder).encode("adminpass");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("enc-admin", saved.getPassword());
        assertThat(saved.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void getAllUsersReturnsListFromRepo() {
        User u1 = new User();
        u1.setUserName("a");

        User u2 = new User();
        u2.setUserName("b");

        List<User> list = Arrays.asList(u1, u2);
        when(userRepository.findAll()).thenReturn(list);

        List<User> result = userService.getAllUsers();

        assertEquals(list, result);
        verify(userRepository).findAll();
    }

    @Test
    void getUserByIdReturnsOptional() {
        User u = new User(); u.setUserName("x");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Optional<User> opt = userService.getUserById(1L);

        assertTrue(opt.isPresent());
        assertEquals("x", opt.get().getUserName());
    }

    @Test
    void deleteUserByIdCallsRepository() {
        userService.deleteUserById(5L);
        verify(userRepository).deleteById(5L);
    }

    @Test
    void deleteUserByUserNameCallsRepository() {
        userService.deleteUserByUserName("sam");
        verify(userRepository).deleteByUserName("sam");
    }

    @Test
    void findByUserNameReturnsUserFromRepo() {
        User u = new User(); u.setUserName("findme");
        when(userRepository.findByUserName("findme")).thenReturn(u);

        User got = userService.findByUserName("findme");

        assertEquals(u, got);
    }

    @Test
    void updateUserUpdatesFieldsAndEncodesPassword() {
        User existing = User.builder()
                .userName("old")
                .password("oldpass")
                .build();

        User incoming = User.builder()
                .userName("newname")
                .password("newpass")
                .build();

        when(userRepository.findByUserName("old")).thenReturn(existing);
        when(passwordEncoder.encode("newpass")).thenReturn("encoded-new");

        userService.updateUser("old", incoming);

        assertEquals("newname", existing.getUserName());
        assertEquals("encoded-new", existing.getPassword());
        verify(passwordEncoder).encode("newpass");
    }

    @Test
    void updateUserWithNullInputDoesNotChangeExisting() {
        User existing = new User();
        existing.setUserName("stay");
        existing.setPassword("stay-pass");

        when(userRepository.findByUserName("stay")).thenReturn(existing);

        userService.updateUser("stay", null);

        // no changes
        assertEquals("stay", existing.getUserName());
        assertEquals("stay-pass", existing.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }
}

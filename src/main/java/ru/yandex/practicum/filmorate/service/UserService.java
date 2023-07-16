package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Qualifier("DbUsersRepository")
    @NotNull
    private final UserRepository repository;
    @NotNull
    private final ValidateService validateService;

    public List<User> getAll() {
        return repository.getAll();
    }

    public void create(User user) {
        validateService.setNameAsLoginIfNameNull(user);
        repository.create(user);
    }

    public void update(User user) {
        validateService.isUserCreated(user.getId());
        repository.update(user);
    }

    public void addFriend(Integer id, Integer friendId) {
        validateService.isUserCreated(id);
        validateService.isUserCreated(friendId);
        validateService.checkNoFriendRequestFromIdToFriendId(id, friendId);
        repository.addToFriends(id, friendId);
    }

    public User getUser(Integer id) {
        User user = repository.get(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + id));
        return user;
    }

    public void deleteFriend(Integer id, Integer friendId) {
        validateService.isUserCreated(id);
        validateService.isUserCreated(friendId);
        validateService.checkThereIsFriendRequestFromIdToFriendId(id, friendId);
        repository.deleteFromFriends(id, friendId);
    }

    public List<User> getFriends(Integer id, boolean confirm) {
        validateService.isUserCreated(id);
        return confirm ? repository.getConfirmFriends(id) : repository.getFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        validateService.isUserCreated(id);
        validateService.isUserCreated(otherId);

        Set<User> commonFriends = new HashSet<>();

        commonFriends.addAll(repository.getFriends(id));
        commonFriends.retainAll(repository.getFriends(otherId));

        return List.copyOf(commonFriends);
    }

    public void deleteAllUsers() {
        repository.deleteAll();
    }

    public void deleteUser(User user) {
        validateService.isUserCreated(user.getId());
        repository.delete(user);
    }
}

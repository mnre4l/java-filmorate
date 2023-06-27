package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("InMemoryUsersRepository")
    private final UserRepository repository;
    private final ValidateService validateService;

    public Collection<User> getAll() {
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
        repository.addToFriends(id, friendId);
    }

    public User getUser(Integer id) {
        validateService.isUserCreated(id);
        return repository.get(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        validateService.isUserCreated(id);
        validateService.isUserCreated(friendId);
        validateService.areUsersFriends(id, friendId);
        repository.deleteFromFriends(id, friendId);
    }

    public Set<User> getFriends(Integer id) {
        validateService.isUserCreated(id);
        return repository.getFriends(id);
    }

    public Set<User> getCommonFriends(int id, int otherId) {
        validateService.isUserCreated(id);
        validateService.isUserCreated(otherId);

        Set<User> commonFriends = new HashSet<>();

        commonFriends.addAll(repository.getFriends(id));
        commonFriends.retainAll(repository.getFriends(otherId));

        return commonFriends;
    }

    public void deleteAllUsers() {
        repository.deleteAll();
    }
}

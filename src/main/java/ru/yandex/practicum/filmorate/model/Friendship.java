package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Friendship {

    private long user_id;
    private long friends_id;
    private FriendshipStatus friendshipStatus;
    private LocalDateTime friendTime;

}

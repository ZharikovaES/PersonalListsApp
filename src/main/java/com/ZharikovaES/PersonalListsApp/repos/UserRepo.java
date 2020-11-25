package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Query("update User u set u.dateLastActivity = :date where u.username = :username")
    void updateLastDateActive(@Param("date") Date date, @Param("username") String username);


}

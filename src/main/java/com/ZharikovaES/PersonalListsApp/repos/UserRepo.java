package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Query("update User u set u.dateLastActivity = :date where u.username = :username")
    void updateLastDateActive(@Param("date") Date date, @Param("username") String username);


}

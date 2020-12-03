package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepo extends CrudRepository<List, Long> {
//    @Modifying
//    @Query("from List l, User u where u.username = :username and u.id = l.listId")
//    List findListByUsername(@Param("username") String username);

//    @Query("select l from List l where l.listId = :id")
    java.util.List<List> findAllByUserId(Long userId);

    @Modifying
    @Query("delete from List l where l.id = :id")
    void deleteById(Long id);

    @Query("select l from List l join l.tags t where t.id = :id order by l.dateUpdate")
    java.util.List<List> findAllByTagIdByDate(Long id);

    @Query("select l from List l join l.tags t where t.id = :id order by l.dateUpdate desc")
    java.util.List<List> findAllByTagIdByDateDesc(Long id);


    java.util.List<List> findAll();
}

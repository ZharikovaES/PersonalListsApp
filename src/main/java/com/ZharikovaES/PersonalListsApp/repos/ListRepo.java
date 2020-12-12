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

    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId order by l.dateUpdate asc")
    java.util.List<List> findAllByUserIdByTagIdByDateAsc(@Param("uId") Long uId, @Param("tId") Long tId);

    @Query("select l from List l where l.userId = :uId order by l.dateUpdate desc")
    java.util.List<List> findAllByUserIdByDateDesc(@Param("uId") Long uId);

    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId order by l.dateUpdate desc")
    java.util.List<List> findAllByUserIdByTagIdByDateDesc(@Param("uId") Long uId, @Param("tId") Long tId);

    @Query("select l from List l where l.userId = :uId order by l.dateUpdate asc")
    java.util.List<List> findAllByUserIdByDateAsc(@Param("uId") Long uId);

    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId and l.title LIKE CONCAT('%', :title, '%') order by l.dateUpdate asc")
    java.util.List<List> findAllByUserIdByTitleByTagIdByDateAsc(@Param("uId") Long uId, @Param("title") String title, @Param("tId") Long tId);

    @Query("select l from List l where l.userId = :uId and l.title LIKE CONCAT('%', :title, '%') order by l.dateUpdate desc")
    java.util.List<List> findAllByUserIdByTitleByDateDesc(@Param("uId") Long uId, @Param("title") String title);

    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId and l.title LIKE CONCAT('%', :title, '%') order by l.dateUpdate desc")
    java.util.List<List> findAllByUserIdByTitleByTagIdByDateDesc(@Param("uId") Long uId, @Param("title") String title, @Param("tId") Long tId);

    @Query("select l from List l where l.userId = :uId and l.title LIKE CONCAT('%', :title, '%') order by l.dateUpdate asc")
    java.util.List<List> findAllByUserIdByTitleByDateAsc(@Param("uId") Long uId, @Param("title") String title);



    java.util.List<List> findAll();
}

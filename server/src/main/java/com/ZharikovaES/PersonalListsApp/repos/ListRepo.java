package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepo extends CrudRepository<List, Long> {
    java.util.List<List> findAllByUserId(Long userId);

    @Modifying
    @Query("delete from List l where l.id = :id")
    void deleteById(@Param("id") Long id);

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

    java.util.List<List> findByUserIdAndTitleIsLike(@Param("uId") Long uId, @Param("title") String title, Pageable pageable);
    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId and l.title LIKE CONCAT('%', :title, '%')")
    java.util.List<List> findByUserIdAndTagIdAndTitleIsLike(@Param("uId") Long uId, @Param("tId") Long tId, @Param("title") String title, Pageable pageable);
    java.util.List<List> findByUserId(@Param("uId") Long uId, Pageable pageable);
    @Query("select l from List l join l.tags t where t.id = :tId and l.userId = :uId")
    java.util.List<List> findByUserIdAndTagId(@Param("uId") Long uId, @Param("tId") Long tId, Pageable pageable);

    java.util.List<List> findAll();
}

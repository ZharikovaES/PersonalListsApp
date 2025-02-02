package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends CrudRepository<Note, Long> {
    java.util.List<Note> findAllByUserId(Long userId);

    @Modifying
    @Query("delete from Note n where n.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying
    @Query("update Note n set n.filename = :newFileName where n.filename = :filename")
    void updateImage(@Param("filename") String filename, @Param("newFileName") String newFileName);

    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId order by n.dateUpdate asc")
    java.util.List<Note> findAllByUserIdByTagIdByDateAsc(@Param("uId") Long uId, @Param("tId") Long tId);

    @Query("select n.filename from Note n where n.id = :id")
    String findFileNameById(@Param("id") Long id);

    @Query("select n from Note n where n.userId = :uId order by n.dateUpdate desc")
    java.util.List<Note> findAllByUserIdByDateDesc(@Param("uId") Long uId);

    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId order by n.dateUpdate desc")
    java.util.List<Note> findAllByUserIdByTagIdByDateDesc(@Param("uId") Long uId, @Param("tId") Long tId);

    @Query("select n from Note n where n.userId = :uId order by n.dateUpdate asc")
    java.util.List<Note> findAllByUserIdByDateAsc(@Param("uId") Long uId);

    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId and n.title LIKE CONCAT('%', :title, '%') order by n.dateUpdate asc")
    java.util.List<Note> findAllByUserIdByTitleByTagIdByDateAsc(@Param("uId") Long uId, @Param("title") String title, @Param("tId") Long tId);

    @Query("select n from Note n where n.userId = :uId and n.title LIKE CONCAT('%', :title, '%') order by n.dateUpdate desc")
    java.util.List<Note> findAllByUserIdByTitleByDateDesc(@Param("uId") Long uId, @Param("title") String title);

    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId and n.title LIKE CONCAT('%', :title, '%') order by n.dateUpdate desc")
    java.util.List<Note> findAllByUserIdByTitleByTagIdByDateDesc(@Param("uId") Long uId, @Param("title") String title, @Param("tId") Long tId);

    @Query("select n from Note n where n.userId = :uId and n.title LIKE CONCAT('%', :title, '%') order by n.dateUpdate asc")
    java.util.List<Note> findAllByUserIdByTitleByDateAsc(@Param("uId") Long uId, @Param("title") String title);

    java.util.List<Note> findByUserIdAndTitleIsLike(@Param("uId") Long uId, @Param("title") String title, Pageable pageable);
    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId and n.title LIKE CONCAT('%', :title, '%')")
    java.util.List<Note> findByUserIdAndTagIdAndTitleIsLike(@Param("uId") Long uId, @Param("tId") Long tId, @Param("title") String title, Pageable pageable);
    java.util.List<Note> findByUserId(@Param("uId") Long uId, Pageable pageable);
    @Query("select n from Note n join n.tags t where t.id = :tId and n.userId = :uId")
    java.util.List<Note> findByUserIdAndTagId(@Param("uId") Long uId, @Param("tId") Long tId, Pageable pageable);


}

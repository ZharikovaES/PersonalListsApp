package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends CrudRepository<Note, Long> {
    java.util.List<Note> findAllByUserId(Long userId);

    @Modifying
//    @Query("delete from List l where l.id = :id")
    void deleteById(Long id);

    @Query("select n from Note n join n.tags t where t.id = :id order by n.dateUpdate")
    java.util.List<Note> findAllByTagIdByDate(Long id);

    @Query("select n from Note n join n.tags t where t.id = :id order by n.dateUpdate desc")
    java.util.List<Note> findAllByTagIdByDateDesc(Long id);

}

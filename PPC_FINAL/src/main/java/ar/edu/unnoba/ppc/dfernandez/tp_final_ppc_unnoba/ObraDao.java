package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ObraDao {
    @Query("SELECT * FROM obra")
    List<Obra> getAll();

    @Query("SELECT * FROM obra WHERE oid IN (:obraIds)")
    List<Obra> loadAllByIds(int[] obraIds);

    @Query("SELECT * FROM obra WHERE domicilio LIKE :domicilio LIMIT 1")
    Obra findByName(String domicilio);

    @Insert
    void insertAll(Obra... obras);

    @Delete
    void delete(Obra obra);

    @Update
    void update(Obra... obras);
}

package cr.ac.unadeca.galeriafrank.database.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cr.ac.unadeca.galeriafrank.database.GaleriaDB;

/**
 * Created by Brian on 3/26/18.
 */
@Table(database = GaleriaDB.class)
public class Users extends BaseModel{
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String username;

    @Column
    public String password;


    @Column
    public String nombre;

    @Column
    public String roll;

}

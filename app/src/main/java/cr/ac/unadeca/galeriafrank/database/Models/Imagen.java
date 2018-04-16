package cr.ac.unadeca.galeriafrank.database.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cr.ac.unadeca.galeriafrank.database.GaleriaDB;

/**
 * Created by Brian on 4/9/18.
 */
@Table(database = GaleriaDB.class)
public class Imagen extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;
//uri es como una url pero para un archivo local
    @Column
    public String uri;

    @Column
    public String nombre;

    @Column
    public String autor;
}

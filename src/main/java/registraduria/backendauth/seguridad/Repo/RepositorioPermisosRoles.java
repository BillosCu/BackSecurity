package registraduria.backendauth.seguridad.Repo;
import org.springframework.data.mongodb.repository.Query;
import registraduria.backendauth.seguridad.Models.PermisosRol;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RepositorioPermisosRoles extends MongoRepository<PermisosRol, String>{
    @Query("{'rol.$id': ObjectId(?0), 'permiso.$id': ObjectId(?1)}")
    PermisosRol getPermisoRol(String id_rol, String id_permiso);
}

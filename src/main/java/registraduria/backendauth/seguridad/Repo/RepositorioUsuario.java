package registraduria.backendauth.seguridad.Repo;
import org.springframework.data.mongodb.repository.Query;
import registraduria.backendauth.seguridad.Models.Usuarios;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioUsuario extends MongoRepository<Usuarios, String>{
    @Query("{'mail': ?0}")
    public  Usuarios getUserByMail(String mail);
}

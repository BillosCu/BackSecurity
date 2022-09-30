package registraduria.backendauth.seguridad.Repo;
import registraduria.backendauth.seguridad.Models.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioRol extends MongoRepository<Rol, String> {
}

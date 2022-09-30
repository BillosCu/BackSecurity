package registraduria.backendauth.seguridad.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document
public class PermisosRol{
    @Id
    private String _id;
    @NotBlank(message = "No puede estar en blanco")
    @NotNull(message = "No puede ser nulo")
    @DBRef
    private Rol rol;
    @NotBlank(message = "No puede estar en blanco")
    @NotNull(message = "No puede ser nulo")
    @DBRef
    private Permiso permiso;

    public PermisosRol() {
    }

    public String get_id() {
        return _id;
    }

    public Rol getRol() {
        return rol;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
}
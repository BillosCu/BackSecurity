package registraduria.backendauth.seguridad.Models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document
public class Rol {
    @Id
    private String _id;

    @NotBlank(message = "El nombre del rol no puede estar en blanco")
    @NotNull(message = "Introduzca un nombre de rol válido")
    private String nombre;

    @JsonCreator
    public Rol(@JsonProperty("nombre") String nombre) {
        this.nombre = nombre;
    }

    public String get_id() {
        return _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
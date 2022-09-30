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
public class Permiso {

    @Id
    private String _id;
    @NotBlank(message = "URL no puede estar en blanco")
    @NotNull(message = "URL no puede ser nulo")
    private String url;
    @NotBlank(message = "Método no puede estar en blanco")
    @NotNull(message = "Método no puede ser nulo")
    private String metodo;

    @JsonCreator
    public Permiso(@JsonProperty("url") String url, @JsonProperty("metodo") String metodo) {
        this.url = url;
        this.metodo = metodo;
    }

    public String get_id() {
        return _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}

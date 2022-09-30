package registraduria.backendauth.seguridad.Models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document()
public class Usuarios {
    @JsonCreator
    public Usuarios(@JsonProperty("name") String name, @JsonProperty("last_name") String last_name, @JsonProperty("mail") String mail, @JsonProperty("password") String password) {
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.password = password;
    }
    @Id
    private String _id;
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "El apellido no puede estar en blanco")
    private String last_name;
    @NotBlank(message = "El email no puede estar en blanco")
    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "Formato de Email incorrecto")
    private String mail;
    @NotNull(message = "La contraseña no puede ser nula")
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;

    @DBRef //Indica que este campo es el que tiene la relación.
    @NotNull(message = "El rol no puede ser nula")
    @NotBlank(message = "El rol no puede estar en blanco")
    public Rol rol;

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() { return password; }

    public void setUsername(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
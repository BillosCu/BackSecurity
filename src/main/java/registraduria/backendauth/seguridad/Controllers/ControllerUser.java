package registraduria.backendauth.seguridad.Controllers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import registraduria.backendauth.seguridad.Message;
import registraduria.backendauth.seguridad.Models.Usuarios;
import registraduria.backendauth.seguridad.Models.Rol;
import registraduria.backendauth.seguridad.Repo.RepositorioRol;
import registraduria.backendauth.seguridad.Repo.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@CrossOrigin
@RestController
@RequestMapping("/users")

public class ControllerUser {

    @Autowired
    private RepositorioUsuario repoUser;

    @Autowired
    private RepositorioRol repoRol;

    @GetMapping("")
    public ResponseEntity<List<Usuarios>> index(){
        List<Usuarios> data = repoUser.findAll();
        if(data.isEmpty()){
            return new ResponseEntity(new Message("No hay usuarios que listar"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Usuarios>>(data, HttpStatus.OK); //Aquí hacemos el listado de todos los usuarios.
    }




    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Usuarios> create(@RequestBody Usuarios data, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message(bindingResult.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
        List<Usuarios> allUsers = repoUser.findAll();
        System.out.println(data.getMail());
        for(Usuarios i: allUsers){
            if(i.getMail().equalsIgnoreCase(data.getMail())){
                return new ResponseEntity(new Message("El email ya existe"), HttpStatus.BAD_REQUEST);
            }
        }

        if(data.getName() != null && data.getLast_name() != null && data.getMail() != null){
            data.setPassword(convertirSHA256(data.getPassword()));
            data.setName(capitalize(data.getName()));
            data.setLast_name(capitalize(data.getLast_name()));
            Usuarios user = this.repoUser.save(data);
            String fullName = user.getName() + " " + user.getLast_name();
            System.out.println(fullName);
            return new ResponseEntity(new Message("Se registró " + fullName), HttpStatus.CREATED);
        }
        return new ResponseEntity(new Message("Ingrese los datos correctamente."), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("{id}")
    public ResponseEntity<Usuarios> show(@PathVariable String id){
        Usuarios user = this.repoUser.findById(id).orElse(null);
        if (user != null){
            return new ResponseEntity(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new Message("Usuario no encontrado."), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<Usuarios> update(@PathVariable String id, @RequestBody Usuarios data){
        Usuarios user = this.repoUser.findById(id).orElse(null);
        if(user != null){
            if(data.getName() != null){
                user.setName(capitalize(data.getName()));
            }
            if(data.getLast_name() != null){
                user.setLast_name(capitalize(data.getLast_name()));
            }
            if(data.getMail() != null){
                user.setMail(data.getMail());
            }
            if(data.getPassword() != null){
                user.setPassword(convertirSHA256(data.getPassword()));
            }
            this.repoUser.save(user);
            String fullName = user.getName() + " " + user.getLast_name();
            return new ResponseEntity(new Message("El usuario " + fullName + " fue modificado."), HttpStatus.ACCEPTED);
        }
        else{
            return new ResponseEntity(new Message("Usuario no encontrado."), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Usuarios> delete(@PathVariable String id){
        Usuarios usuarioActual = this.repoUser.findById(id).orElse(null);
        if (usuarioActual!=null){
            String fullName = capitalize(usuarioActual.getName() + " " + capitalize(usuarioActual.getLast_name()));
            this.repoUser.delete(usuarioActual);
            return new ResponseEntity(new Message("El usuario "+ fullName +" fue eliminado."), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("El id no existe"), HttpStatus.NOT_FOUND);
    }




    /*Prueba*/
    @PutMapping("{id}/rol/{id_rol}")
    public ResponseEntity<Usuarios> asignacionDeRol(@PathVariable String id, @PathVariable String id_rol){
        Usuarios user = this.repoUser.findById(id).orElse(null);
        if(user == null){
            return new ResponseEntity(new Message("No se encontró el usuario"), HttpStatus.NOT_FOUND);
        }

        Rol rol = this.repoRol.findById(id_rol).orElse(null);
        if (rol == null) {
            return new ResponseEntity(new Message("No se encontró el rol"), HttpStatus.NOT_FOUND);
        }

        if(user != null && rol != null){
            user.setRol(rol);
            this.repoUser.save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("Error"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "/validacion", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Usuarios> validate(@RequestBody Usuarios infoUser, final HttpServletResponse response) throws IOException {
        Usuarios user = this.repoUser.getUserByMail(infoUser.getMail());
        if(user == null){
            return new ResponseEntity(new Message("Mail no existe"), HttpStatus.BAD_REQUEST);
        }
        if(!user.getPassword().equalsIgnoreCase(convertirSHA256(infoUser.getPassword()))){
            return new ResponseEntity(new Message("Contraseña incorrecta"), HttpStatus.BAD_REQUEST);
        }
        user.setPassword(""); //Se modifica el objeto localmente, no se está haciendo "save" para no afectar la base de datos.
        return new ResponseEntity(user, HttpStatus.OK);
    }









    public String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public String convertirSHA256(String password) { //
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}

package registraduria.backendauth.seguridad.Controllers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import registraduria.backendauth.seguridad.Message; //Mensaje personalizado.
import registraduria.backendauth.seguridad.Models.Rol;
import registraduria.backendauth.seguridad.Repo.RepositorioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rol")

public class ControllerRol {

    @Autowired
    private RepositorioRol repoRol; //Instancia de un Rol.


    @GetMapping
    public ResponseEntity<List<Rol>> index(){
        List<Rol> allRol = repoRol.findAll();
        if(allRol.isEmpty()){
            return new ResponseEntity(new Message("No hay roles"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Rol>>(allRol, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Rol> create(@Valid @RequestBody Rol rol, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity(new Message(bindingResult.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
        }
        List<Rol> allRols = repoRol.findAll();
        for(Rol i: allRols){
            if(i.getNombre().equalsIgnoreCase(rol.getNombre())){
                return new ResponseEntity(new Message("El rol ya existe."), HttpStatus.BAD_REQUEST);
            }
        }
        rol.setNombre(capitalize(rol.getNombre()));
        Rol uniqueRol = this.repoRol.save(rol);
        return new ResponseEntity(new Message("Se registro \"" + uniqueRol.getNombre() + "\" como rol"), HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<Rol> show(@PathVariable String id){
        Rol rol = this.repoRol.findById(id).orElse(null);
        if(rol != null){
            return new ResponseEntity(rol, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new Message("Rol no encontrado"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Rol> update(@PathVariable String id, @RequestBody Rol data){
        Rol rol = this.repoRol.findById(id).orElse(null);
        if(rol != null){
            rol.setNombre(capitalize(data.getNombre()));
            this.repoRol.save(rol);
            return new ResponseEntity(new Message("El rol \"" +rol.getNombre() + "\" fue modificado"), HttpStatus.ACCEPTED);
        }
        else{
            return new ResponseEntity(new Message("Rol no encontrado"), HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Rol> delete(@PathVariable String id){
        Rol rol = this.repoRol.findById(id).orElse(null);
        if(rol != null){
            String rolName = rol.getNombre();
            this.repoRol.delete(rol);
            return new ResponseEntity(new Message("El rol \"" + rolName + "\" fue eliminado"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("El id no existe"), HttpStatus.NOT_FOUND);
    }

    public String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}

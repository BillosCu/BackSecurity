package registraduria.backendauth.seguridad.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import registraduria.backendauth.seguridad.Message;
import registraduria.backendauth.seguridad.Models.Permiso;
import registraduria.backendauth.seguridad.Repo.RepositorioPermiso;
import java.util.List;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/permiso")
public class ControllerPermisos {
    @Autowired
    private  RepositorioPermiso repoPermiso;


    @GetMapping("")
    public ResponseEntity<List<Permiso>> index(){
        List<Permiso> allPermisos = repoPermiso.findAll();
        if(allPermisos.isEmpty()){
            return new ResponseEntity(new Message("No hay Permisos que listar"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Permiso>>(allPermisos, HttpStatus.OK);
    }


    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Permiso> create(@RequestBody Permiso permiso, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(new Message(bindingResult.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
        }
        if(!permiso.getMetodo().equalsIgnoreCase("GET") && !permiso.getMetodo().equalsIgnoreCase("POST") && !permiso.getMetodo().equalsIgnoreCase("PUT") && !permiso.getMetodo().equalsIgnoreCase("DELETE")){
            return new ResponseEntity(new Message("Digite un método correcto. (GET, POST, PUT, DELETE)"), HttpStatus.BAD_REQUEST);
        }
        List<Permiso> all = repoPermiso.findAll();
        for(Permiso i: all){
            if(i.getUrl().equalsIgnoreCase(permiso.getUrl()) && i.getMetodo().equalsIgnoreCase(permiso.getMetodo())){
                return new ResponseEntity(new Message("Ya existe el permiso"), HttpStatus.BAD_REQUEST);
            }
        }
        permiso.getMetodo().toUpperCase();
        Permiso newPermiso = this.repoPermiso.save(permiso);
        return new ResponseEntity(newPermiso, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Permiso> show(@PathVariable String id){
        Permiso permiso =this.repoPermiso.findById(id).orElse(null);
        if(permiso != null){
            return new ResponseEntity(permiso, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new Message("No se encontró"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Permiso> update(@Valid @PathVariable String id, @RequestBody Permiso permiso){
        Permiso findPermiso=this.repoPermiso.findById(id).orElse(null);
        if(findPermiso != null){
            findPermiso.setMetodo(permiso.getMetodo());
            findPermiso.setUrl(permiso.getUrl());
            this.repoPermiso.save(findPermiso);
            return new ResponseEntity(findPermiso, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(new Message("No se encontró el id."), HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Permiso> delete(@PathVariable String id){
        Permiso permiso = this.repoPermiso.findById(id).orElse(null);
        if(permiso != null){
            this.repoPermiso.delete(permiso);
            return new ResponseEntity(new Message("Se eliminó correctamente"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("No se encontró el id"), HttpStatus.NOT_FOUND);
    }

}

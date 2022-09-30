package registraduria.backendauth.seguridad.Controllers;
import org.springframework.http.ResponseEntity;
import registraduria.backendauth.seguridad.Message; //Mensaje personalizado.
import registraduria.backendauth.seguridad.Models.PermisosRol;
import registraduria.backendauth.seguridad.Models.Rol;
import registraduria.backendauth.seguridad.Models.Permiso;
import registraduria.backendauth.seguridad.Repo.RepositorioPermisosRoles;
import registraduria.backendauth.seguridad.Repo.RepositorioPermiso;
import registraduria.backendauth.seguridad.Repo.RepositorioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permisos-rol")

public class ControllerPermisoRol {

    @Autowired
    private RepositorioPermisosRoles repoPR;
    @Autowired
    private RepositorioPermiso repoPermiso;
    @Autowired
    private RepositorioRol repoRol;


    @GetMapping("")
    public ResponseEntity<List<PermisosRol>> index(){
        List<PermisosRol> allPR = this.repoPR.findAll();
        if(allPR.isEmpty()){
            return new ResponseEntity(new Message("No hay nada que listar"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<PermisosRol>>(allPR, HttpStatus.OK);

    }


    @PostMapping("rol/{id_rol}/permiso/{id_permiso}")
    public ResponseEntity<PermisosRol> create(@PathVariable String id_rol, @PathVariable String id_permiso){
        PermisosRol PR = new PermisosRol();

        Rol rol = this.repoRol.findById(id_rol).orElse(null);
        Permiso permiso = this.repoPermiso.findById(id_permiso).orElse(null);
        System.out.println(rol);
        System.out.println(permiso);

        if (rol == null){
            return new ResponseEntity(new Message("No se encontró el ID del Rol"), HttpStatus.NOT_FOUND);
        }
        if (permiso == null){
            return new ResponseEntity(new Message("No se encontró el ID del Permiso"), HttpStatus.NOT_FOUND);
        }
        List<PermisosRol> all = this.repoPR.findAll();

        for(PermisosRol i: all){
            if(i.getPermiso().get_id().equals(id_permiso) && i.getRol().get_id().equals(id_rol)){
                return new ResponseEntity(new Message("Ya existe"), HttpStatus.FOUND);
            }
        }

        PR.setPermiso(permiso);
        PR.setRol(rol);
        this.repoPR.save(PR);
        return new ResponseEntity(PR, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PermisosRol> show(@PathVariable String id){
        PermisosRol PR = this.repoPR.findById(id).orElse(null);
        if(PR != null){
            return new ResponseEntity(PR, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("No se encontró Permiso/Rol"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}/rol/{id_rol}/permiso/{id_permiso}")
    public ResponseEntity<PermisosRol> update(@PathVariable String id,@PathVariable String id_rol,@PathVariable String id_permiso){
        PermisosRol PR = this.repoPR.findById(id).orElse(null);
        Rol rol = this.repoRol.findById(id_rol).orElse(null);
        Permiso permiso = this.repoPermiso.findById(id_permiso).orElse(null);
        Permiso prueba = this.repoPermiso.findById(id_permiso).get();
        System.out.println(prueba);
        if(PR == null){
            return new ResponseEntity(new Message("No se encontró Permiso/Rol"), HttpStatus.NOT_FOUND);
        }
        if(rol == null){
            return new ResponseEntity(new Message("No se encontró Rol"), HttpStatus.NOT_FOUND);
        }
        if(permiso == null){
            return new ResponseEntity(new Message("No se encontró Permiso"), HttpStatus.NOT_FOUND);
        }
        PR.setPermiso(permiso);
        PR.setRol(rol);
        this.repoPR.save(PR);
        return new ResponseEntity(PR, HttpStatus.ACCEPTED);
    }


    @GetMapping("validar/rol/{id_rol}")
    public ResponseEntity<PermisosRol> validar(@PathVariable String id_rol, @RequestBody Permiso infoPermiso){
        Permiso permiso = this.repoPermiso.getPermiso(infoPermiso.getUrl(), infoPermiso.getMetodo().toUpperCase());
        if (permiso == null){
            return new ResponseEntity(new Message("No se encontró el permiso"), HttpStatus.NOT_FOUND);
        }
        Rol rol = this.repoRol.findById(id_rol).get();
        if(rol == null){
            return new ResponseEntity(new Message("No se encontró el rol"), HttpStatus.NOT_FOUND);
        }
        PermisosRol response = this.repoPR.getPermisoRol(id_rol, permiso.get_id());
        if (response != null ){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("No existe el permiso rol"), HttpStatus.BAD_REQUEST);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Proyectofinal.DentalDate.Controller;

import com.Proyectofinal.DentalDate.Entity.Odontologo;
import com.Proyectofinal.DentalDate.Entity.Paciente;
import com.Proyectofinal.DentalDate.Entity.Usuario;
import com.Proyectofinal.DentalDate.Service.OdontologoService;
import com.Proyectofinal.DentalDate.Service.PacienteService;
import com.Proyectofinal.DentalDate.Service.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/") //localhost:8080/odontologo
public class AdminController {

    @Autowired
    private OdontologoService odontologoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioServicio us;

//    @GetMapping("/")
//    public String dashboard(ModelMap model) {
//        model.put("usuarios", us.findAll());
//        return "admin-dashboard";
//    }
    //LISTAR
    //QUERY PARA TRAER AMBAS COLUMNAS??? 
    //INDEX DE FRANCO FALTA AQUI XD
    @GetMapping("/listar-pacientes-turnos-con-odontologos")
    public String listarPacientes(ModelMap modelo) {

        List<Odontologo> odontologo = odontologoService.listaOdontologo();
        modelo.put("listaOdontologos", odontologo);

        List<Paciente> listapacientes = pacienteService.listaPaciente();
        modelo.put("listaPacientes", listapacientes);

        return "admin.html";
    }

    @GetMapping("/registro-odontologo") //crear botton para redirecionar al formulario
    public String formulario() {

        return "form-odont";
    }

    @PostMapping("/formOdo") //formulario-ondontolog-por-creado-ADMIN-
    public String guardar(HttpSession session, ModelMap modelo, String nombre, String apellido, String email, String contraseña, String Matricula, String especialidad) {

        try {
            Usuario u = (Usuario) session.getAttribute("usuariosession");
            Odontologo o = odontologoService.Guardar(nombre, apellido, email, contraseña, Matricula, especialidad);
            //ns.registroModificacionPerro(p, u, "Bienvenido a la app de perros \n \n mail: " + u.getEmail() + " su perro fue cargado correctamente"
            //        + "\n \n Perro: " + p.getNombre() + "\n Apodo: " + p.getApodo() + "\n Raza: " + p.getRaza(), "Registro Perro");
            modelo.put("exito", "Registro exitoso");

            return "fredirect/:";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            modelo.put("error", "Falto algun dato");
            return "formOdo";
        }
    }

    /// QUE ES ESTO???? 
    @GetMapping("/role/{id}")
    public String cambiarRol(ModelMap modelo, @PathVariable String id) {
        try {
            // us.cambiarRol(id);
            modelo.put("", id);
        } catch (Exception e) {
        }
        return "redirect:/admin/";
    }

    //ELIMINAMOS ODONTOLOGO
    //ACA ELIMINO EL TURNO DIRECTAMENTE CON PACIENTE Y ODONTOGOLOGO
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        try {
            pacienteService.eliminarPaciente(id);
            odontologoService.Eliminar(id);
        } catch (Exception e) {

        }
        return "redirect:/admin/";
    }

    //modificar 
    @GetMapping("/editar-admin")
    public String editar(ModelMap model, HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuariosession");
            model.put("usuario", u);
        } catch (Exception e) {
        }

        return "editar-admin";
    }

    @PostMapping("/editar-admin")// si el usuario deseamofificar la contraseña asi lau? estas segura? 
    public String editarOdontologo(@RequestParam String email, @RequestParam String contraseña, RedirectAttributes redirectAttributes, ModelMap model) {
        try {
            Usuario u = odontologoService.modificarOdontologo(email, email, contraseña, contraseña);

            //ns.registroModificacionUsuario(u, "¡Bienvenido a la app de perros! \n \n mail: "+u.getEmail()+" \n \n Usuario modificado correctamente", "Modificacion Usuario");
            model.put("exito", "Usuario modificado con exito");
            redirectAttributes.addFlashAttribute("exito", "Usuario modificado con exito");
        } catch (Exception e) {
            model.put("error", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/editar";
    }

    //CANCELAR TURNO
}
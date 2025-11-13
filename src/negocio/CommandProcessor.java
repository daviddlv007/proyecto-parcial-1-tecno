package negocio;

import conexion.CommandParser;
import datos.*;
import java.util.List;

/**
 * Procesador de comandos COMPLETO - Lógica de negocio
 * Soporta CRUD para las 11 tablas del sistema (44 comandos + AYUDA = 45 total)
 */
public class CommandProcessor {
    
    // DAOs para tablas catálogo
    private RolDAO rolDAO = new RolDAO();
    private TipoVehiculoDAO tipoVehiculoDAO = new TipoVehiculoDAO();
    private TipoActividadDAO tipoActividadDAO = new TipoActividadDAO();
    private TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
    private MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAO();
    
    // DAOs para tablas principales
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private ActividadDAO actividadDAO = new ActividadDAO();
    private SesionDAO sesionDAO = new SesionDAO();
    private InscripcionDAO inscripcionDAO = new InscripcionDAO();
    private PagoDAO pagoDAO = new PagoDAO();
    
    /**
     * Procesa un comando y retorna respuesta HTML
     */
    public String procesarComando(CommandParser.Comando comando) {
        try {
            String cmd = comando.getNombre();
            List<String> params = comando.getParametros();
            
            if ("AYUDA".equals(cmd)) return generarAyuda();
            
            // ==================== TABLAS CATÁLOGO ====================
            
            // === ROLES ===
            if ("LISROL".equals(cmd)) return listarRoles(params.get(0));
            if ("INSROL".equals(cmd)) {
                boolean ok = rolDAO.insertar(params.get(0), params.get(1));
                return ok ? generarExito("Rol insertado correctamente") : generarError("No se pudo insertar rol");
            }
            if ("MODROL".equals(cmd)) {
                boolean ok = rolDAO.modificar(Integer.parseInt(params.get(0)), params.get(1), 
                                             params.get(2), Boolean.parseBoolean(params.get(3)));
                return ok ? generarExito("Rol modificado correctamente") : generarError("No se pudo modificar rol");
            }
            if ("DELROL".equals(cmd)) {
                boolean ok = rolDAO.eliminar(Integer.parseInt(params.get(0)));
                return ok ? generarExito("Rol desactivado correctamente") : generarError("No se pudo desactivar rol");
            }
            
            // === TIPOS DE VEHÍCULO ===
            if ("LISTVH".equals(cmd)) return listarTiposVehiculo(params.get(0));
            if ("INSTVH".equals(cmd)) {
                boolean ok = tipoVehiculoDAO.insertar(params.get(0), params.get(1));
                return ok ? generarExito("Tipo de vehículo insertado") : generarError("No se pudo insertar");
            }
            if ("MODTVH".equals(cmd)) {
                boolean ok = tipoVehiculoDAO.modificar(Integer.parseInt(params.get(0)), params.get(1), 
                                                       params.get(2), Boolean.parseBoolean(params.get(3)));
                return ok ? generarExito("Tipo de vehículo modificado") : generarError("No se pudo modificar");
            }
            if ("DELTVH".equals(cmd)) {
                boolean ok = tipoVehiculoDAO.eliminar(Integer.parseInt(params.get(0)));
                return ok ? generarExito("Tipo de vehículo desactivado") : generarError("No se pudo desactivar");
            }
            
            // === TIPOS DE ACTIVIDAD ===
            if ("LISTAC".equals(cmd)) return listarTiposActividad(params.get(0));
            if ("INSTAC".equals(cmd)) {
                boolean ok = tipoActividadDAO.insertar(params.get(0), params.get(1));
                return ok ? generarExito("Tipo de actividad insertado") : generarError("No se pudo insertar");
            }
            if ("MODTAC".equals(cmd)) {
                boolean ok = tipoActividadDAO.modificar(Integer.parseInt(params.get(0)), params.get(1), 
                                                        params.get(2), Boolean.parseBoolean(params.get(3)));
                return ok ? generarExito("Tipo de actividad modificado") : generarError("No se pudo modificar");
            }
            if ("DELTAC".equals(cmd)) {
                boolean ok = tipoActividadDAO.eliminar(Integer.parseInt(params.get(0)));
                return ok ? generarExito("Tipo de actividad desactivado") : generarError("No se pudo desactivar");
            }
            
            // === TIPOS DE PAGO ===
            if ("LISTPG".equals(cmd)) return listarTiposPago(params.get(0));
            if ("INSTPG".equals(cmd)) {
                boolean ok = tipoPagoDAO.insertar(params.get(0), params.get(1));
                return ok ? generarExito("Tipo de pago insertado") : generarError("No se pudo insertar");
            }
            if ("MODTPG".equals(cmd)) {
                boolean ok = tipoPagoDAO.modificar(Integer.parseInt(params.get(0)), params.get(1), 
                                                   params.get(2), Boolean.parseBoolean(params.get(3)));
                return ok ? generarExito("Tipo de pago modificado") : generarError("No se pudo modificar");
            }
            if ("DELTPG".equals(cmd)) {
                boolean ok = tipoPagoDAO.eliminar(Integer.parseInt(params.get(0)));
                return ok ? generarExito("Tipo de pago desactivado") : generarError("No se pudo desactivar");
            }
            
            // === MÉTODOS DE PAGO ===
            if ("LISMPG".equals(cmd)) return listarMetodosPago(params.get(0));
            if ("INSMPG".equals(cmd)) {
                boolean ok = metodoPagoDAO.insertar(params.get(0), params.get(1));
                return ok ? generarExito("Método de pago insertado") : generarError("No se pudo insertar");
            }
            if ("MODMPG".equals(cmd)) {
                boolean ok = metodoPagoDAO.modificar(Integer.parseInt(params.get(0)), params.get(1), 
                                                     params.get(2), Boolean.parseBoolean(params.get(3)));
                return ok ? generarExito("Método de pago modificado") : generarError("No se pudo modificar");
            }
            if ("DELMPG".equals(cmd)) {
                boolean ok = metodoPagoDAO.eliminar(Integer.parseInt(params.get(0)));
                return ok ? generarExito("Método de pago desactivado") : generarError("No se pudo desactivar");
            }
            
            // ==================== TABLAS PRINCIPALES ====================
            
            // === USUARIOS ===
            if ("LISUSU".equals(cmd)) return listarUsuarios(params.get(0));
            if ("INSUSU".equals(cmd)) {
                int id = usuarioDAO.insertar(params);
                return generarExito("Usuario insertado con ID: " + id);
            }
            if ("MODUSU".equals(cmd)) {
                boolean ok = usuarioDAO.modificar(params);
                return ok ? generarExito("Usuario modificado") : generarError("No se modificó");
            }
            if ("DELUSU".equals(cmd)) {
                boolean ok = usuarioDAO.eliminar(params.get(0));
                return ok ? generarExito("Usuario eliminado") : generarError("No se eliminó");
            }
            
            // === VEHÍCULOS ===
            if ("LISVEH".equals(cmd)) return listarVehiculos(params.get(0));
            if ("INSVEH".equals(cmd)) {
                int id = vehiculoDAO.insertar(params);
                return generarExito("Vehículo insertado con ID: " + id);
            }
            if ("MODVEH".equals(cmd)) {
                boolean ok = vehiculoDAO.modificar(params);
                return ok ? generarExito("Vehículo modificado") : generarError("No se modificó");
            }
            if ("DELVEH".equals(cmd)) {
                boolean ok = vehiculoDAO.eliminar(params.get(0));
                return ok ? generarExito("Vehículo eliminado") : generarError("No se eliminó");
            }
            
            // === ACTIVIDADES ===
            if ("LISACT".equals(cmd)) return listarActividades(params.get(0));
            if ("INSACT".equals(cmd)) {
                int id = actividadDAO.insertar(params);
                return generarExito("Actividad insertada con ID: " + id);
            }
            if ("MODACT".equals(cmd)) {
                boolean ok = actividadDAO.modificar(params);
                return ok ? generarExito("Actividad modificada") : generarError("No se modificó");
            }
            if ("DELACT".equals(cmd)) {
                boolean ok = actividadDAO.eliminar(params.get(0));
                return ok ? generarExito("Actividad eliminada") : generarError("No se eliminó");
            }
            
            // === SESIONES ===
            if ("LISSES".equals(cmd)) return listarSesiones(params.get(0));
            if ("INSSES".equals(cmd)) {
                int id = sesionDAO.insertar(params);
                return generarExito("Sesión insertada con ID: " + id);
            }
            if ("MODSES".equals(cmd)) {
                boolean ok = sesionDAO.modificar(params);
                return ok ? generarExito("Sesión modificada") : generarError("No se modificó");
            }
            if ("DELSES".equals(cmd)) {
                boolean ok = sesionDAO.eliminar(params.get(0));
                return ok ? generarExito("Sesión eliminada") : generarError("No se eliminó");
            }
            
            // === INSCRIPCIONES ===
            if ("LISINS".equals(cmd)) return listarInscripciones(params.get(0));
            if ("INSINS".equals(cmd)) {
                int id = inscripcionDAO.insertar(params);
                return generarExito("Inscripción insertada con ID: " + id);
            }
            if ("MODINS".equals(cmd)) {
                boolean ok = inscripcionDAO.modificar(params);
                return ok ? generarExito("Inscripción modificada") : generarError("No se modificó");
            }
            if ("DELINS".equals(cmd)) {
                boolean ok = inscripcionDAO.eliminar(params.get(0));
                return ok ? generarExito("Inscripción eliminada") : generarError("No se eliminó");
            }
            
            // === PAGOS ===
            if ("LISPAG".equals(cmd)) return listarPagos(params.get(0));
            if ("INSPAG".equals(cmd)) {
                int id = pagoDAO.insertar(params);
                return generarExito("Pago insertado con ID: " + id);
            }
            if ("MODPAG".equals(cmd)) {
                boolean ok = pagoDAO.modificar(params);
                return ok ? generarExito("Pago modificado") : generarError("No se modificó");
            }
            if ("DELPAG".equals(cmd)) {
                boolean ok = pagoDAO.eliminar(params.get(0));
                return ok ? generarExito("Pago eliminado") : generarError("No se eliminó");
            }
            
            return generarError("Comando no reconocido: " + cmd);
            
        } catch (Exception e) {
            // Imprimir stack trace completo en consola para debugging
            System.err.println("========================================");
            System.err.println("EXCEPCIÓN EN COMANDO: " + comando.getNombre());
            System.err.println("PARAMETROS: " + comando.getParametros());
            e.printStackTrace();
            System.err.println("========================================");
            
            // Generar mensaje de error informativo
            String mensaje = e.getMessage();
            if (mensaje == null || mensaje.isEmpty()) {
                mensaje = e.getClass().getSimpleName() + " - revise logs para detalles";
            }
            return generarError("Error procesando comando: " + mensaje);
        }
    }
    
    // ==================== MÉTODOS DE LISTADO ====================
    
    private String listarRoles(String patron) {
        try {
            List<Rol> roles = rolDAO.listar(patron);
            if (roles.isEmpty()) {
                return wrapHTML("<strong>No se encontraron roles</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ ROLES ═══</strong>\n\n");
            for (Rol r : roles) {
                html.append("<strong>ID:</strong> ").append(r.getId()).append("\n");
                html.append("  • Nombre: ").append(r.getNombre()).append("\n");
                html.append("  • Descripción: ").append(r.getDescripcion()).append("\n");
                html.append("  • Activo: ").append(r.isActivo() ? "Sí" : "No").append("\n\n");
            }
            html.append("<strong>Total: ").append(roles.size()).append(" roles</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando roles: " + e.getMessage());
        }
    }
    
    private String listarTiposVehiculo(String patron) {
        try {
            List<TipoVehiculo> tipos = tipoVehiculoDAO.listar(patron);
            if (tipos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron tipos de vehículo</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ TIPOS DE VEHÍCULO ═══</strong>\n\n");
            for (TipoVehiculo t : tipos) {
                html.append("<strong>ID:</strong> ").append(t.getId()).append("\n");
                html.append("  • Nombre: ").append(t.getNombre()).append("\n");
                html.append("  • Descripción: ").append(t.getDescripcion()).append("\n");
                html.append("  • Activo: ").append(t.isActivo() ? "Sí" : "No").append("\n\n");
            }
            html.append("<strong>Total: ").append(tipos.size()).append(" tipos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando tipos de vehículo: " + e.getMessage());
        }
    }
    
    private String listarTiposActividad(String patron) {
        try {
            List<TipoActividad> tipos = tipoActividadDAO.listar(patron);
            if (tipos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron tipos de actividad</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ TIPOS DE ACTIVIDAD ═══</strong>\n\n");
            for (TipoActividad t : tipos) {
                html.append("<strong>ID:</strong> ").append(t.getId()).append("\n");
                html.append("  • Nombre: ").append(t.getNombre()).append("\n");
                html.append("  • Descripción: ").append(t.getDescripcion()).append("\n");
                html.append("  • Activo: ").append(t.isActivo() ? "Sí" : "No").append("\n\n");
            }
            html.append("<strong>Total: ").append(tipos.size()).append(" tipos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando tipos de actividad: " + e.getMessage());
        }
    }
    
    private String listarTiposPago(String patron) {
        try {
            List<TipoPago> tipos = tipoPagoDAO.listar(patron);
            if (tipos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron tipos de pago</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ TIPOS DE PAGO ═══</strong>\n\n");
            for (TipoPago t : tipos) {
                html.append("<strong>ID:</strong> ").append(t.getId()).append("\n");
                html.append("  • Nombre: ").append(t.getNombre()).append("\n");
                html.append("  • Descripción: ").append(t.getDescripcion()).append("\n");
                html.append("  • Activo: ").append(t.isActivo() ? "Sí" : "No").append("\n\n");
            }
            html.append("<strong>Total: ").append(tipos.size()).append(" tipos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando tipos de pago: " + e.getMessage());
        }
    }
    
    private String listarMetodosPago(String patron) {
        try {
            List<MetodoPago> metodos = metodoPagoDAO.listar(patron);
            if (metodos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron métodos de pago</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ MÉTODOS DE PAGO ═══</strong>\n\n");
            for (MetodoPago m : metodos) {
                html.append("<strong>ID:</strong> ").append(m.getId()).append("\n");
                html.append("  • Nombre: ").append(m.getNombre()).append("\n");
                html.append("  • Descripción: ").append(m.getDescripcion()).append("\n");
                html.append("  • Activo: ").append(m.isActivo() ? "Sí" : "No").append("\n\n");
            }
            html.append("<strong>Total: ").append(metodos.size()).append(" métodos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando métodos de pago: " + e.getMessage());
        }
    }
    
    private String listarUsuarios(String patron) {
        try {
            List<Usuario> usuarios = usuarioDAO.listar(patron);
            if (usuarios.isEmpty()) {
                return wrapHTML("<strong>No se encontraron usuarios</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ USUARIOS ═══</strong>\n\n");
            for (Usuario u : usuarios) {
                html.append("<strong>ID:</strong> ").append(u.getId()).append("\n");
                html.append("  • Nombre: ").append(u.getNombre()).append(" ").append(u.getApellido()).append("\n");
                html.append("  • Email: ").append(u.getEmail()).append("\n");
                html.append("  • Teléfono: ").append(u.getTelefono()).append("\n");
                html.append("  • Rol ID: ").append(u.getRolId()).append("\n\n");
            }
            html.append("<strong>Total: ").append(usuarios.size()).append(" usuarios</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando usuarios: " + e.getMessage());
        }
    }
    
    private String listarVehiculos(String patron) {
        try {
            List<Vehiculo> vehiculos = vehiculoDAO.listar(patron);
            if (vehiculos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron vehículos</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ VEHÍCULOS ═══</strong>\n\n");
            for (Vehiculo v : vehiculos) {
                html.append("<strong>ID:</strong> ").append(v.getId()).append("\n");
                html.append("  • Placa: ").append(v.getPlaca()).append("\n");
                html.append("  • Marca/Modelo: ").append(v.getMarca()).append(" ").append(v.getModelo()).append("\n");
                html.append("  • Año: ").append(v.getAnio()).append("\n\n");
            }
            html.append("<strong>Total: ").append(vehiculos.size()).append(" vehículos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando vehículos: " + e.getMessage());
        }
    }
    
    private String listarActividades(String patron) {
        try {
            List<Actividad> actividades = actividadDAO.listar(patron);
            if (actividades.isEmpty()) {
                return wrapHTML("<strong>No se encontraron actividades</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ ACTIVIDADES ═══</strong>\n\n");
            for (Actividad a : actividades) {
                html.append("<strong>ID:</strong> ").append(a.getId()).append("\n");
                html.append("  • Nombre: ").append(a.getNombre()).append("\n");
                html.append("  • Tipo ID: ").append(a.getTipoActividadId()).append("\n");
                html.append("  • Duración: ").append(a.getDuracionHoras()).append(" horas\n\n");
            }
            html.append("<strong>Total: ").append(actividades.size()).append(" actividades</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando actividades: " + e.getMessage());
        }
    }
    
    private String listarSesiones(String patron) {
        try {
            List<Sesion> sesiones = sesionDAO.listar(patron);
            if (sesiones.isEmpty()) {
                return wrapHTML("<strong>No se encontraron sesiones</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ SESIONES ═══</strong>\n\n");
            for (Sesion s : sesiones) {
                html.append("<strong>ID:</strong> ").append(s.getId()).append("\n");
                html.append("  • Actividad ID: ").append(s.getActividadId()).append("\n");
                html.append("  • Fecha: ").append(s.getFecha()).append("\n");
                html.append("  • Horario: ").append(s.getHoraInicio()).append(" - ").append(s.getHoraFin()).append("\n");
                html.append("  • Instructor ID: ").append(s.getInstructorId()).append("\n\n");
            }
            html.append("<strong>Total: ").append(sesiones.size()).append(" sesiones</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando sesiones: " + e.getMessage());
        }
    }
    
    private String listarInscripciones(String patron) {
        try {
            List<Inscripcion> inscripciones = inscripcionDAO.listar(patron);
            if (inscripciones.isEmpty()) {
                return wrapHTML("<strong>No se encontraron inscripciones</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ INSCRIPCIONES ═══</strong>\n\n");
            for (Inscripcion i : inscripciones) {
                html.append("<strong>ID:</strong> ").append(i.getId()).append("\n");
                html.append("  • Alumno ID: ").append(i.getAlumnoId()).append("\n");
                html.append("  • Sesión ID: ").append(i.getSesionId()).append("\n");
                html.append("  • Monto: Bs. ").append(i.getMontoTotal()).append("\n");
                html.append("  • Estado: ").append(i.getEstadoInscripcion()).append("\n\n");
            }
            html.append("<strong>Total: ").append(inscripciones.size()).append(" inscripciones</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando inscripciones: " + e.getMessage());
        }
    }
    
    private String listarPagos(String patron) {
        try {
            List<Pago> pagos = pagoDAO.listar(patron);
            if (pagos.isEmpty()) {
                return wrapHTML("<strong>No se encontraron pagos</strong>");
            }
            
            StringBuilder html = new StringBuilder("<strong>═══ PAGOS ═══</strong>\n\n");
            for (Pago p : pagos) {
                html.append("<strong>ID:</strong> ").append(p.getId()).append("\n");
                html.append("  • Método Pago ID: ").append(p.getMetodoPagoId()).append("\n");
                html.append("  • Fecha: ").append(p.getFecha()).append("\n");
                html.append("  • Monto: Bs. ").append(p.getMonto()).append("\n\n");
            }
            html.append("<strong>Total: ").append(pagos.size()).append(" pagos</strong>");
            return wrapHTML(html.toString());
        } catch (Exception e) {
            return generarError("Error listando pagos: " + e.getMessage());
        }
    }
    
    // ==================== GENERADORES HTML ====================
    
    private String generarAyuda() {
        StringBuilder html = new StringBuilder();
        html.append("<h2>COMANDOS DISPONIBLES</h2>\n\n");
        
        html.append("<h3>━━━ TABLAS CATÁLOGO (20 comandos) ━━━</h3>\n\n");
        
        html.append("<strong>→ Roles</strong>\n");
        html.append("  • LISROL[\"*\"] - Listar roles\n");
        html.append("  • INSROL[\"nombre\",\"descripcion\"] - Insertar rol\n");
        html.append("  • MODROL[\"id\",\"nombre\",\"descripcion\",\"activo\"] - Modificar rol\n");
        html.append("  • DELROL[\"id\"] - Desactivar rol\n\n");
        
        html.append("<strong>→ Tipos de Vehículo</strong>\n");
        html.append("  • LISTVH[\"*\"] - Listar tipos\n");
        html.append("  • INSTVH[\"nombre\",\"descripcion\"] - Insertar tipo\n");
        html.append("  • MODTVH[\"id\",\"nombre\",\"descripcion\",\"activo\"] - Modificar tipo\n");
        html.append("  • DELTVH[\"id\"] - Desactivar tipo\n\n");
        
        html.append("<strong>→ Tipos de Actividad</strong>\n");
        html.append("  • LISTAC[\"*\"] - Listar tipos\n");
        html.append("  • INSTAC[\"nombre\",\"descripcion\"] - Insertar tipo\n");
        html.append("  • MODTAC[\"id\",\"nombre\",\"descripcion\",\"activo\"] - Modificar tipo\n");
        html.append("  • DELTAC[\"id\"] - Desactivar tipo\n\n");
        
        html.append("<strong>→ Tipos de Pago</strong>\n");
        html.append("  • LISTPG[\"*\"] - Listar tipos\n");
        html.append("  • INSTPG[\"nombre\",\"descripcion\"] - Insertar tipo\n");
        html.append("  • MODTPG[\"id\",\"nombre\",\"descripcion\",\"activo\"] - Modificar tipo\n");
        html.append("  • DELTPG[\"id\"] - Desactivar tipo\n\n");
        
        html.append("<strong>→ Métodos de Pago</strong>\n");
        html.append("  • LISMPG[\"*\"] - Listar métodos\n");
        html.append("  • INSMPG[\"nombre\",\"descripcion\"] - Insertar método\n");
        html.append("  • MODMPG[\"id\",\"nombre\",\"descripcion\",\"activo\"] - Modificar método\n");
        html.append("  • DELMPG[\"id\"] - Desactivar método\n\n");
        
        html.append("<h3>━━━ TABLAS PRINCIPALES (24 comandos) ━━━</h3>\n\n");
        
        html.append("<strong>→ Usuarios</strong>\n");
        html.append("  • LISUSU[\"*\"] - Listar usuarios\n");
        html.append("  • INSUSU[11 params] - Insertar usuario\n");
        html.append("  • MODUSU[12 params] - Modificar usuario\n");
        html.append("  • DELUSU[\"id\"] - Eliminar usuario\n\n");
        
        html.append("<strong>→ Vehículos</strong>\n");
        html.append("  • LISVEH[\"*\"] - Listar vehículos\n");
        html.append("  • INSVEH[7 params] - Insertar vehículo\n");
        html.append("  • MODVEH[8 params] - Modificar vehículo\n");
        html.append("  • DELVEH[\"id\"] - Eliminar vehículo\n\n");
        
        html.append("<strong>→ Actividades</strong>\n");
        html.append("  • LISACT[\"*\"] - Listar actividades\n");
        html.append("  • INSACT[5 params] - Insertar actividad\n");
        html.append("  • MODACT[6 params] - Modificar actividad\n");
        html.append("  • DELACT[\"id\"] - Eliminar actividad\n\n");
        
        html.append("<strong>→ Sesiones</strong>\n");
        html.append("  • LISSES[\"*\"] - Listar sesiones\n");
        html.append("  • INSSES[8 params] - Insertar sesión\n");
        html.append("  • MODSES[9 params] - Modificar sesión\n");
        html.append("  • DELSES[\"id\"] - Eliminar sesión\n\n");
        
        html.append("<strong>→ Inscripciones</strong>\n");
        html.append("  • LISINS[\"*\"] - Listar inscripciones\n");
        html.append("  • INSINS[7 params] - Insertar inscripción\n");
        html.append("  • MODINS[8 params] - Modificar inscripción\n");
        html.append("  • DELINS[\"id\"] - Eliminar inscripción\n\n");
        
        html.append("<strong>→ Pagos</strong>\n");
        html.append("  • LISPAG[\"*\"] - Listar pagos\n");
        html.append("  • INSPAG[6 params] - Insertar pago\n");
        html.append("  • MODPAG[7 params] - Modificar pago\n");
        html.append("  • DELPAG[\"id\"] - Eliminar pago\n\n");
        
        html.append("<strong>TOTAL: 45 comandos (AYUDA + 44 CRUD)</strong>\n");
        return wrapHTML(html.toString());
    }
    
    private String generarExito(String mensaje) {
        return wrapHTML("<strong>✓ Éxito</strong>\n\n" + mensaje);
    }
    
    private String generarError(String mensaje) {
        return wrapHTML("<strong>✗ Error</strong>\n\n" + mensaje);
    }
    
    private String wrapHTML(String contenido) {
        return "<html><head><meta charset='UTF-8'></head><body>" + contenido + "</body></html>";
    }
}

package negocio;

import conexion.CommandParser;
import conexion.HTMLResponseBuilder;
import datos.*;
import java.util.List;

/**
 * Procesador de comandos COMPLETO - Lógica de negocio
 * Soporta CRUD para las 11 tablas del sistema (44 comandos + AYUDA + 4 REPORTES = 49 total)
 */
public class CommandProcessor {
    
    private ReportGenerator reportGenerator = new ReportGenerator();
    
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
            
            // ==================== REPORTES Y ESTADÍSTICAS ====================
            if ("REPACT".equals(cmd)) return reportGenerator.reporteActividadesPorTipo();
            if ("REPUSU".equals(cmd)) return reportGenerator.reporteUsuariosPorRol();
            if ("REPVEH".equals(cmd)) return reportGenerator.reporteVehiculosPorTipo();
            if ("REPPAG".equals(cmd)) return reportGenerator.reportePagosPorMetodo();
            
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
            
        } catch (IndexOutOfBoundsException e) {
            // Parámetros faltantes o malformados
            return generarError("Parámetros faltantes o formato incorrecto. Use AYUDA para ver el formato correcto.");
        } catch (NumberFormatException e) {
            // Error al convertir string a número
            return generarError("Formato numérico inválido en parámetros: " + e.getMessage());
        } catch (Exception e) {
            // Error general - imprimir stack trace para debugging
            System.err.println("========================================");
            System.err.println("EXCEPCIÓN EN COMANDO: " + comando.getNombre());
            System.err.println("PARAMETROS: " + comando.getParametros());
            e.printStackTrace();
            System.err.println("========================================");
            
            // Generar mensaje de error informativo
            String mensaje = e.getMessage();
            if (mensaje == null || mensaje.isEmpty()) {
                mensaje = e.getClass().getSimpleName();
            }
            return generarError("Error procesando comando: " + mensaje);
        }
    }
    
    // ==================== MÉTODOS DE LISTADO ====================
    
    private String listarRoles(String patron) {
        try {
            List<Rol> roles = rolDAO.listar(patron);
            if (roles.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Roles", 
                    HTMLResponseBuilder.info("No se encontraron roles"));
            }
            
            String[][] rows = new String[roles.size()][4];
            for (int i = 0; i < roles.size(); i++) {
                Rol r = roles.get(i);
                rows[i][0] = String.valueOf(r.getId());
                rows[i][1] = r.getNombre();
                rows[i][2] = r.getDescripcion();
                rows[i][3] = r.isActivo() ? "✓ Activo" : "✗ Inactivo";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Descripción", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + roles.size() + " roles</p>";
            return HTMLResponseBuilder.wrapHTML("Roles", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando roles: " + e.getMessage());
        }
    }
    
    private String listarTiposVehiculo(String patron) {
        try {
            List<TipoVehiculo> tipos = tipoVehiculoDAO.listar(patron);
            if (tipos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Tipos de Vehículo", 
                    HTMLResponseBuilder.info("No se encontraron tipos de vehículo"));
            }
            
            String[][] rows = new String[tipos.size()][4];
            for (int i = 0; i < tipos.size(); i++) {
                TipoVehiculo t = tipos.get(i);
                rows[i][0] = String.valueOf(t.getId());
                rows[i][1] = t.getNombre();
                rows[i][2] = t.getDescripcion();
                rows[i][3] = t.isActivo() ? "✓ Activo" : "✗ Inactivo";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Descripción", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + tipos.size() + " tipos</p>";
            return HTMLResponseBuilder.wrapHTML("Tipos de Vehículo", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando tipos de vehículo: " + e.getMessage());
        }
    }
    
    private String listarTiposActividad(String patron) {
        try {
            List<TipoActividad> tipos = tipoActividadDAO.listar(patron);
            if (tipos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Tipos de Actividad", 
                    HTMLResponseBuilder.info("No se encontraron tipos de actividad"));
            }
            
            String[][] rows = new String[tipos.size()][4];
            for (int i = 0; i < tipos.size(); i++) {
                TipoActividad t = tipos.get(i);
                rows[i][0] = String.valueOf(t.getId());
                rows[i][1] = t.getNombre();
                rows[i][2] = t.getDescripcion();
                rows[i][3] = t.isActivo() ? "✓ Activo" : "✗ Inactivo";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Descripción", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + tipos.size() + " tipos</p>";
            return HTMLResponseBuilder.wrapHTML("Tipos de Actividad", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando tipos de actividad: " + e.getMessage());
        }
    }
    
    private String listarTiposPago(String patron) {
        try {
            List<TipoPago> tipos = tipoPagoDAO.listar(patron);
            if (tipos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Tipos de Pago", 
                    HTMLResponseBuilder.info("No se encontraron tipos de pago"));
            }
            
            String[][] rows = new String[tipos.size()][4];
            for (int i = 0; i < tipos.size(); i++) {
                TipoPago t = tipos.get(i);
                rows[i][0] = String.valueOf(t.getId());
                rows[i][1] = t.getNombre();
                rows[i][2] = t.getDescripcion();
                rows[i][3] = t.isActivo() ? "✓ Activo" : "✗ Inactivo";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Descripción", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + tipos.size() + " tipos</p>";
            return HTMLResponseBuilder.wrapHTML("Tipos de Pago", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando tipos de pago: " + e.getMessage());
        }
    }
    
    private String listarMetodosPago(String patron) {
        try {
            List<MetodoPago> metodos = metodoPagoDAO.listar(patron);
            if (metodos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Métodos de Pago", 
                    HTMLResponseBuilder.info("No se encontraron métodos de pago"));
            }
            
            String[][] rows = new String[metodos.size()][4];
            for (int i = 0; i < metodos.size(); i++) {
                MetodoPago m = metodos.get(i);
                rows[i][0] = String.valueOf(m.getId());
                rows[i][1] = m.getNombre();
                rows[i][2] = m.getDescripcion();
                rows[i][3] = m.isActivo() ? "✓ Activo" : "✗ Inactivo";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Descripción", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + metodos.size() + " métodos</p>";
            return HTMLResponseBuilder.wrapHTML("Métodos de Pago", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando métodos de pago: " + e.getMessage());
        }
    }
    
    private String listarUsuarios(String patron) {
        try {
            List<Usuario> usuarios = usuarioDAO.listar(patron);
            if (usuarios.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Usuarios", 
                    HTMLResponseBuilder.info("No se encontraron usuarios"));
            }
            
            String[][] rows = new String[usuarios.size()][5];
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                rows[i][0] = String.valueOf(u.getId());
                rows[i][1] = u.getNombre() + " " + u.getApellido();
                rows[i][2] = u.getEmail();
                rows[i][3] = u.getTelefono();
                rows[i][4] = "Rol #" + u.getRolId();
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre Completo", "Email", "Teléfono", "Rol"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + usuarios.size() + " usuarios</p>";
            return HTMLResponseBuilder.wrapHTML("Usuarios", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando usuarios: " + e.getMessage());
        }
    }
    
    private String listarVehiculos(String patron) {
        try {
            List<Vehiculo> vehiculos = vehiculoDAO.listar(patron);
            if (vehiculos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Vehículos", 
                    HTMLResponseBuilder.info("No se encontraron vehículos"));
            }
            
            String[][] rows = new String[vehiculos.size()][4];
            for (int i = 0; i < vehiculos.size(); i++) {
                Vehiculo v = vehiculos.get(i);
                rows[i][0] = String.valueOf(v.getId());
                rows[i][1] = v.getPlaca();
                rows[i][2] = v.getMarca() + " " + v.getModelo();
                rows[i][3] = String.valueOf(v.getAnio());
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Placa", "Marca/Modelo", "Año"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + vehiculos.size() + " vehículos</p>";
            return HTMLResponseBuilder.wrapHTML("Vehículos", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando vehículos: " + e.getMessage());
        }
    }
    
    private String listarActividades(String patron) {
        try {
            List<Actividad> actividades = actividadDAO.listar(patron);
            if (actividades.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Actividades", 
                    HTMLResponseBuilder.info("No se encontraron actividades"));
            }
            
            String[][] rows = new String[actividades.size()][4];
            for (int i = 0; i < actividades.size(); i++) {
                Actividad a = actividades.get(i);
                rows[i][0] = String.valueOf(a.getId());
                rows[i][1] = a.getNombre();
                rows[i][2] = "Tipo #" + a.getTipoActividadId();
                rows[i][3] = a.getDuracionHoras() + " hrs";
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Nombre", "Tipo", "Duración"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + actividades.size() + " actividades</p>";
            return HTMLResponseBuilder.wrapHTML("Actividades", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando actividades: " + e.getMessage());
        }
    }
    
    private String listarSesiones(String patron) {
        try {
            List<Sesion> sesiones = sesionDAO.listar(patron);
            if (sesiones.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Sesiones", 
                    HTMLResponseBuilder.info("No se encontraron sesiones"));
            }
            
            String[][] rows = new String[sesiones.size()][5];
            for (int i = 0; i < sesiones.size(); i++) {
                Sesion s = sesiones.get(i);
                rows[i][0] = String.valueOf(s.getId());
                rows[i][1] = "Act #" + s.getActividadId();
                rows[i][2] = String.valueOf(s.getFecha());
                rows[i][3] = s.getHoraInicio() + " - " + s.getHoraFin();
                rows[i][4] = "Inst #" + s.getInstructorId();
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Actividad", "Fecha", "Horario", "Instructor"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + sesiones.size() + " sesiones</p>";
            return HTMLResponseBuilder.wrapHTML("Sesiones", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando sesiones: " + e.getMessage());
        }
    }
    
    private String listarInscripciones(String patron) {
        try {
            List<Inscripcion> inscripciones = inscripcionDAO.listar(patron);
            if (inscripciones.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Inscripciones", 
                    HTMLResponseBuilder.info("No se encontraron inscripciones"));
            }
            
            String[][] rows = new String[inscripciones.size()][5];
            for (int i = 0; i < inscripciones.size(); i++) {
                Inscripcion ins = inscripciones.get(i);
                rows[i][0] = String.valueOf(ins.getId());
                rows[i][1] = "Alumno #" + ins.getAlumnoId();
                rows[i][2] = "Sesión #" + ins.getSesionId();
                rows[i][3] = "Bs. " + ins.getMontoTotal();
                rows[i][4] = ins.getEstadoInscripcion();
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Alumno", "Sesión", "Monto", "Estado"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + inscripciones.size() + " inscripciones</p>";
            return HTMLResponseBuilder.wrapHTML("Inscripciones", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando inscripciones: " + e.getMessage());
        }
    }
    
    private String listarPagos(String patron) {
        try {
            List<Pago> pagos = pagoDAO.listar(patron);
            if (pagos.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Pagos", 
                    HTMLResponseBuilder.info("No se encontraron pagos"));
            }
            
            String[][] rows = new String[pagos.size()][4];
            for (int i = 0; i < pagos.size(); i++) {
                Pago p = pagos.get(i);
                rows[i][0] = String.valueOf(p.getId());
                rows[i][1] = "Método #" + p.getMetodoPagoId();
                rows[i][2] = String.valueOf(p.getFecha());
                rows[i][3] = "Bs. " + p.getMonto();
            }
            
            String tabla = HTMLResponseBuilder.buildTable(
                new String[]{"ID", "Método Pago", "Fecha", "Monto"}, 
                rows
            );
            String total = "<p style='text-align:right;font-weight:600;color:#475569;margin-top:15px'>Total: " + pagos.size() + " pagos</p>";
            return HTMLResponseBuilder.wrapHTML("Pagos", tabla + total);
        } catch (Exception e) {
            return generarError("Error listando pagos: " + e.getMessage());
        }
    }
    
    // ==================== GENERADORES HTML ====================
    
    private String generarAyuda() {
        // Array con todos los comandos: {Comando, Descripción}
        String[][] comandos = {
            // Roles
            {"LISROL[\"*\"]", "Listar roles"},
            {"INSROL[\"nombre\",\"desc\"]", "Insertar rol"},
            {"MODROL[\"id\",\"nombre\",\"desc\",\"activo\"]", "Modificar rol"},
            {"DELROL[\"id\"]", "Desactivar rol"},
            // Tipos de Vehículo
            {"LISTVH[\"*\"]", "Listar tipos de vehículo"},
            {"INSTVH[\"nombre\",\"desc\"]", "Insertar tipo de vehículo"},
            {"MODTVH[\"id\",\"nombre\",\"desc\",\"activo\"]", "Modificar tipo de vehículo"},
            {"DELTVH[\"id\"]", "Desactivar tipo de vehículo"},
            // Tipos de Actividad
            {"LISTAC[\"*\"]", "Listar tipos de actividad"},
            {"INSTAC[\"nombre\",\"desc\"]", "Insertar tipo de actividad"},
            {"MODTAC[\"id\",\"nombre\",\"desc\",\"activo\"]", "Modificar tipo de actividad"},
            {"DELTAC[\"id\"]", "Desactivar tipo de actividad"},
            // Tipos de Pago
            {"LISTPG[\"*\"]", "Listar tipos de pago"},
            {"INSTPG[\"nombre\",\"desc\"]", "Insertar tipo de pago"},
            {"MODTPG[\"id\",\"nombre\",\"desc\",\"activo\"]", "Modificar tipo de pago"},
            {"DELTPG[\"id\"]", "Desactivar tipo de pago"},
            // Métodos de Pago
            {"LISMPG[\"*\"]", "Listar métodos de pago"},
            {"INSMPG[\"nombre\",\"desc\"]", "Insertar método de pago"},
            {"MODMPG[\"id\",\"nombre\",\"desc\",\"activo\"]", "Modificar método de pago"},
            {"DELMPG[\"id\"]", "Desactivar método de pago"},
            // Usuarios
            {"LISUSU[\"*\"]", "Listar usuarios"},
            {"INSUSU[11 params]", "Insertar usuario"},
            {"MODUSU[12 params]", "Modificar usuario"},
            {"DELUSU[\"id\"]", "Eliminar usuario"},
            // Vehículos
            {"LISVEH[\"*\"]", "Listar vehículos"},
            {"INSVEH[7 params]", "Insertar vehículo"},
            {"MODVEH[8 params]", "Modificar vehículo"},
            {"DELVEH[\"id\"]", "Eliminar vehículo"},
            // Actividades
            {"LISACT[\"*\"]", "Listar actividades"},
            {"INSACT[5 params]", "Insertar actividad"},
            {"MODACT[6 params]", "Modificar actividad"},
            {"DELACT[\"id\"]", "Eliminar actividad"},
            // Sesiones
            {"LISSES[\"*\"]", "Listar sesiones"},
            {"INSSES[8 params]", "Insertar sesión"},
            {"MODSES[9 params]", "Modificar sesión"},
            {"DELSES[\"id\"]", "Eliminar sesión"},
            // Inscripciones
            {"LISINS[\"*\"]", "Listar inscripciones"},
            {"INSINS[7 params]", "Insertar inscripción"},
            {"MODINS[8 params]", "Modificar inscripción"},
            {"DELINS[\"id\"]", "Eliminar inscripción"},
            // Pagos
            {"LISPAG[\"*\"]", "Listar pagos"},
            {"INSPAG[6 params]", "Insertar pago"},
            {"MODPAG[7 params]", "Modificar pago"},
            {"DELPAG[\"id\"]", "Eliminar pago"},
            // Reportes
            {"REPACT", "Reporte de actividades por tipo"},
            {"REPUSU", "Reporte de usuarios por rol"},
            {"REPVEH", "Reporte de vehículos por tipo"},
            {"REPPAG", "Reporte de pagos por método"}
        };
        
        String tabla = HTMLResponseBuilder.buildTable(
            new String[]{"Comando", "Descripción"}, 
            comandos
        );
        
        String footer = "<p style='text-align:center;font-weight:600;color:#475569;margin-top:20px;font-size:16px'>" +
                       "📊 TOTAL: 49 comandos (1 AYUDA + 44 CRUD + 4 REPORTES)</p>" +
                       "<p style='text-align:center;color:#64748b;margin-top:10px'>Usa el patrón \"*\" para listar todos los registros</p>";
        
        return HTMLResponseBuilder.wrapHTML("Comandos Disponibles", tabla + footer);
    }
    
    private String generarExito(String mensaje) {
        return HTMLResponseBuilder.wrapHTML("Operación Exitosa", 
            HTMLResponseBuilder.success(mensaje));
    }
    
    private String generarError(String mensaje) {
        return HTMLResponseBuilder.wrapHTML("Error", 
            HTMLResponseBuilder.error(mensaje));
    }
    
    private String wrapHTML(String contenido) {
        return "<html><head><meta charset='UTF-8'></head><body>" + contenido + "</body></html>";
    }
}

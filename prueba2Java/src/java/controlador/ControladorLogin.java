package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Empleado;


public class ControladorLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String opcion = "";
        HttpSession sesion = request.getSession();  // Crear o recuperar la sesión
        String usuario, clave;

        if (request.getParameter("opcion") != null) {
            opcion = request.getParameter("opcion");
        }

        switch (opcion) {
            case "Ingresar":
                usuario = request.getParameter("usuario");
                clave = request.getParameter("clave");
                
                // Verificar los datos de usuario y clave
                boolean estado = new EmpleadoDAO().verificarDatos(usuario, clave);
                
                if (estado) {  // Si las credenciales son correctas
                    // Recuperamos los datos del empleado desde la base de datos
                    Empleado emp = new EmpleadoDAO().buscarDatos(usuario);
                    String rutEmpleado = emp.getRut() + "-" + emp.getDv();
                    String nombreCompleto = emp.getPrimerNombre() + " " + emp.getSegundoNombre() + " " + emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();

                    // Guardamos los datos del usuario en la sesión
                    sesion.setAttribute("usuario", usuario);
                    sesion.setAttribute("rutEmpleado", rutEmpleado);
                    sesion.setAttribute("nombreCompletoE", nombreCompleto);

                    // Redirigimos a la página principal (menú)
                    request.getRequestDispatcher("menuPrincipal.jsp").forward(request, response);
                } else {  // Si las credenciales no son correctas
                    response.sendRedirect("errorLogin.jsp");
                }
                break;
            default:
                // Si la opción no es "Ingresar", redirigir a la página de login por defecto
                response.sendRedirect("login.jsp");
                break;
        }
    }

    // HttpServlet methods
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet encargado del control de login";
    }
}

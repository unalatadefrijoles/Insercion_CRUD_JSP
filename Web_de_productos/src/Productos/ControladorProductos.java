package Productos;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class ControladorProductos
 */
@WebServlet("/ControladorProductos")
public class ControladorProductos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ModeloProductos modeloProductos;
	
	@Resource(name="jdbc/Productos")
	private DataSource miPool;
	
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		
		try {
			modeloProductos = new ModeloProductos(miPool);
		}catch (Exception e) {
			throw new ServletException(e);
		}
		
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//Leer el parametro que llega desde el formulario
		String comando = request.getParameter("instruccion");
		
		//Si no se envia el parametro, listar productos
		if(comando==null) comando="listar"; 
			
		
		//Redirigir el flujo de datos adecuados
		
		switch(comando) {
			
		case "listar": obtenerProductos(request, response);break;
		case "insertarBD": agregarProducto(request, response);break;
		default: obtenerProductos(request, response);break;
		}
		
		
		
		
		
		
	}



	private void agregarProducto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		//Leer la informacion del producto
		String CodArticulo= request.getParameter("CArt");
		String Seccion= request.getParameter("seccion");
		String NombreArticulo= request.getParameter("NArt");
		
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-mm-dd");
		Date Fecha = null;
		
		try {
			Fecha= formatoFecha.parse(request.getParameter("fecha"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double Precio= Double.parseDouble(request.getParameter("precio"));
		String Importado= request.getParameter("importado");
		String PaisOrigen= request.getParameter("POrig");
		
		
		
		//Crear un objt de tipo producto
		
		Productos NuevoProducto = new Productos(CodArticulo, Seccion, NombreArticulo, Precio, Fecha, Importado, PaisOrigen);
		
		//Enviar el objeto al modelo, despues insertar en la BD
		
		modeloProductos.agregarElNuevoProducto(NuevoProducto);
		
		//Volver al listado de productos
		obtenerProductos(request, response);
	}



	private void obtenerProductos(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		//Obtener la lista
				List<Productos> productos;
				try {
					productos = modeloProductos.getProductos();
				
				
				//Agregar la lista
					
					request.setAttribute("LISTAPRODUCTOS", productos);
				
				//Enviar la lista
					
					RequestDispatcher miDispatcher = request.getRequestDispatcher("/ListaProductos.jsp");
					miDispatcher.forward(request, response);
					
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}
	}

}

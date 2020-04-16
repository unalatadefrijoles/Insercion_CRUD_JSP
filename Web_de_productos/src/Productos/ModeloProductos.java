package Productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.sql.DataSource;

public class ModeloProductos {

	private DataSource origenDatos;
	
	public ModeloProductos(DataSource origenDatos){
			
		this.origenDatos = origenDatos;
	}
	
	public List<Productos> getProductos() throws Exception{
		
		List<Productos> productos = new ArrayList<>();
		Connection miConexion = null;
		Statement miStatement = null;
		ResultSet miResulset = null;
		
		//Establecer conexion
		miConexion= origenDatos.getConnection();
		
		//Sentencia SLQ
		String miSql = "SELECT * FROM PRODS";
		miStatement = miConexion.createStatement();
		
		//Ejecutar SQL
		miResulset = miStatement.executeQuery(miSql);
		
		while(miResulset.next()) {
			String c_art=miResulset.getString("CODIGOARTICULO");
			String seccion=miResulset.getString("SECCION");
			String n_art=miResulset.getString("NOMBREARTICULO");
			double precio=miResulset.getDouble("PRECIO");
			Date fecha=miResulset.getDate("FECHA");
			String importado=miResulset.getString("IMPORTADO");
			String p_orig=miResulset.getString("PAISDEORIGEN");
			
			Productos temProd = new Productos(c_art,seccion,n_art,precio,fecha,importado,p_orig);
			
			productos.add(temProd);
			
		}
		
		return productos;
		
	}

	public void agregarElNuevoProducto(Productos nuevoProducto) {
		// TODO Auto-generated method stub
		
		//Obtener la conexion con la BD
		Connection miConexion = null;
		PreparedStatement miStatement = null;
		
		try {
			miConexion = origenDatos.getConnection();
		
		
		//Crear la instruccion SQL que inserte el producto
		
		String sql = "INSERT INTO PRODS (CODIGOARTICULO, SECCION, NOMBREARTICULO, PRECIO, FECHA, IMPORTADO, PAISDEORIGEN)"+
		"VALUES(?,?,?,?,?,?,?)";
		miStatement = miConexion.prepareStatement(sql);
		
		
		//Establecer los parametros para el producto
		
		miStatement.setString(1,nuevoProducto.getcArt());
		miStatement.setString(2,nuevoProducto.getSeccion());
		miStatement.setString(3,nuevoProducto.getnArt());
		miStatement.setDouble(4,nuevoProducto.getPrecio());
		
		java.util.Date utilDate = nuevoProducto.getFecha();
		java.sql.Date fechaConvertida = new java.sql.Date(utilDate.getTime());
		miStatement.setDate(5, fechaConvertida);
		
		
		miStatement.setString(6,nuevoProducto.getImportado());
		miStatement.setString(7,nuevoProducto.getpOrig());
		
		//Ejecutar el SQL
		
		miStatement.execute();
		
		}catch(Exception e) {
			
		}
	}
	
}

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * La clase Productos proporciona métodos para interactuar con una base de datos de productos retro.
 * Permite la inserción, búsqueda y eliminación de productos de diferentes tipos como chaquetas, camisetas y botas.
 */
public class Productos {

    /**
     * Establece una conexión con la base de datos utilizando las credenciales proporcionadas en un archivo de configuración.
     *
     * @return Una conexión a la base de datos.
     */
    public Connection obtenerConexion() {
        Connection conexion = null;
        Properties propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream("./Files/database.ini")) {
            propiedades.load(fis);

            String url = propiedades.getProperty("database.url");
            String user = propiedades.getProperty("database.user");
            String paswd = propiedades.getProperty("database.paswd");

            conexion = DriverManager.getConnection(url, user, paswd);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }


    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param tipo   El tipo de producto (chaqueta, camiseta, botas).
     * @param nombre El nombre del producto.
     * @param marca  La marca del producto.
     * @param precio El precio del producto.
     */
    public void insertarProducto(String tipo, String nombre, String marca, double precio) {
        String tabla = obtenerTablaPorTipo(tipo);
        if (tabla == null) return;

        String sql = "INSERT INTO " + tabla + " (nombre, marca, precio) VALUES (?, ?, ?)";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, marca);
            pstmt.setDouble(3, precio);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca productos en la base de datos que coincidan con un criterio dado.
     *
     * @param tipo     El tipo de producto (chaqueta, camiseta, botas).
     * @param criterio El criterio de búsqueda.
     * @return Una lista de arrays de strings que representan los productos encontrados.
     */
    public List<String[]> buscarProductos(String tipo, String criterio) {
        List<String[]> resultados = new ArrayList<>();
        String tabla = obtenerTablaPorTipo(tipo);
        if (tabla == null) return resultados;

        String sql = "SELECT * FROM " + tabla + " WHERE nombre LIKE ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + criterio + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] fila = {rs.getString("nombre"), rs.getString("marca"), String.valueOf(rs.getDouble("precio"))};
                resultados.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }
/**
        * Elimina un producto de la base de datos.
            *
            * @param tipo   El tipo de producto (chaqueta, camiseta, botas).
            * @param nombre El nombre del producto a eliminar.
     */
    public void eliminarProducto(String tipo, String nombre) {
        String tabla = obtenerTablaPorTipo(tipo);
        if (tabla == null) return;

        String sql = "DELETE FROM " + tabla + " WHERE nombre = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para obtener el nombre de la tabla correspondiente al tipo de producto.
     *
     * @param tipo El tipo de producto (chaqueta, camiseta, botas).
     * @return El nombre de la tabla en la base de datos que contiene productos del tipo especificado.
     */
    private String obtenerTablaPorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "chaqueta":
                return "chaquetas_retro";
            case "camiseta":
                return "camisetas_retro";
            case "botas":
                return "botas_retro";
            default:
                System.out.println("Tipo de producto no válido");
                return null;
        }
    }
}

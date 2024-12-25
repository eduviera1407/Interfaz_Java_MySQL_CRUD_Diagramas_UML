import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * La clase InterfazGraficaProductos proporciona una interfaz gráfica para la gestión de productos retro.
 * Permite la inserción, búsqueda y eliminación de productos, así como la inserción de datos desde un archivo.
 */
public class InterfazGraficaProductos extends JFrame {

    private JTextField campoNombre;
    private JTextField campoMarca;
    private JTextField campoPrecio;
    private JComboBox<String> comboTipo;
    private JButton botonInsertarDesdeArchivo;
    private JButton botonBuscar;
    private JButton botonEliminar;
    private JButton botonInsertar;
    private JTable tablaResultados;
    private Productos productos;
    private JPanel panelInsercion;
    private JPanel panelBusqueda;

    /**
     * Constructor de la clase InterfazGraficaProductos.
     * Inicializa la interfaz de usuario, los manejadores de eventos y la apariencia.
     */
    public InterfazGraficaProductos() {
        productos = new Productos();
        initUI();
        initEventHandlers();
        apariencia();
    }

    private void initUI() {
        // Configuración básica del JFrame
        setTitle("Gestión de Productos Retro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creación de paneles
        panelInsercion = new JPanel();
        panelBusqueda = new JPanel();

        // Configuración de layouts para los paneles
        panelInsercion.setLayout(new GridLayout(2, 1));
        panelBusqueda.setLayout(new GridLayout(6, 2));

        // Componentes de inserción
        botonInsertarDesdeArchivo = new JButton("Insertar desde archivo");
        panelInsercion.add(botonInsertarDesdeArchivo);

        // Componentes de búsqueda/eliminación/inserción
        botonInsertar = new JButton("Insertar");
        botonBuscar = new JButton("Buscar");
        botonEliminar = new JButton("Eliminar");

        panelBusqueda.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panelBusqueda.add(campoNombre);

        panelBusqueda.add(new JLabel("Marca:"));
        campoMarca = new JTextField();
        panelBusqueda.add(campoMarca);

        panelBusqueda.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        panelBusqueda.add(campoPrecio);

        panelBusqueda.add(new JLabel("Tipo de Producto:"));
        String[] tipos = {"Chaqueta", "Camiseta", "Botas"};
        comboTipo = new JComboBox<>(tipos);
        panelBusqueda.add(comboTipo);

        panelBusqueda.add(botonBuscar);
        panelBusqueda.add(botonEliminar);
        panelBusqueda.add(botonInsertar);

        // Configuración de la tabla de resultados
        tablaResultados = new JTable(new DefaultTableModel(new Object[]{"Nombre", "Marca", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaResultados);

        // Configuración de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Insertar datos desde archivo", panelInsercion);
        tabbedPane.addTab("Búsqueda/Eliminación/Inserción", panelBusqueda);

        // Agregar componentes al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.NORTH);
    }

    /**
     * Inicializa los manejadores de eventos para los botones.
     */
    private void initEventHandlers() {
        // Manejador de eventos para el botón Insertar
        botonInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String marca = campoMarca.getText();
                double precio = Double.parseDouble(campoPrecio.getText());
                String tipo = (String) comboTipo.getSelectedItem();
                productos.insertarProducto(tipo, nombre, marca, precio);
                JOptionPane.showMessageDialog(InterfazGraficaProductos.this, "Producto insertado correctamente");
            }
        });

        // Manejador de eventos para el botón Buscar
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String criterio = campoNombre.getText();
                String tipo = (String) comboTipo.getSelectedItem();
                List<String[]> resultados = productos.buscarProductos(tipo, criterio);
                mostrarResultados(resultados);
            }
        });

        // Manejador de eventos para el botón Eliminar
        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String tipo = (String) comboTipo.getSelectedItem();
                productos.eliminarProducto(tipo, nombre);
                JOptionPane.showMessageDialog(InterfazGraficaProductos.this, "Producto eliminado correctamente");
            }
        });

        // Manejador de eventos para el botón Insertar desde archivo
        botonInsertarDesdeArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarDesdeArchivo();
            }
        });
    }

    /**
     * Muestra los resultados de la búsqueda en la tabla de resultados.
     *
     * @param resultados La lista de resultados de la búsqueda.
     */
    private void mostrarResultados(List<String[]> resultados) {
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);
        for (String[] resultado : resultados) {
            model.addRow(resultado);
        }
    }

    /**
     * Inserta productos desde un archivo de texto.
     */
    private void insertarDesdeArchivo() {
        // Ruta del archivo de texto
        String filePath = "./Files/InsertProductos.txt";
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             Connection connection = obtenerConexionDesdePropiedades()) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                String nombre = data[0].trim();
                String marca = data[1].trim();
                double precio = Double.parseDouble(data[2].trim());

                String tipo;
                if (nombre.toLowerCase().contains("chaqueta")) {
                    tipo = "chaqueta";
                } else if (nombre.toLowerCase().contains("botas")) {
                    tipo = "botas";
                } else if (nombre.toLowerCase().contains("camiseta")) {
                    tipo = "camiseta";
                } else {
                    continue;
                }

                productos.insertarProducto(tipo, nombre, marca, precio);
            }

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM chaquetas_retro " +
                         "UNION ALL " +
                         "SELECT * FROM botas_retro " +
                         "UNION ALL " +
                         "SELECT * FROM camisetas_retro")) {
                DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
                model.setRowCount(0);
                while (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    String marca = resultSet.getString("marca");
                    double precio = resultSet.getDouble("precio");
                    model.addRow(new Object[]{nombre, marca, precio});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener los datos de la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Productos insertados correctamente desde el archivo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar datos desde el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene una conexión a la base de datos utilizando las propiedades especificadas en un archivo de configuración.
     *
     * @return Una conexión a la base de datos.
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     * @throws IOException  Si ocurre un error al leer el archivo de configuración.
     */
       private Connection obtenerConexionDesdePropiedades() throws SQLException, IOException {
        Properties propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream("./Files/database.ini")) {
            propiedades.load(fis);
        }

        String url = propiedades.getProperty("database.url");
        String user = propiedades.getProperty("database.user");
        String paswd = propiedades.getProperty("database.paswd");
        return DriverManager.getConnection(url, user, paswd);
    }

    /**
     * Aplica estilos visuales a los componentes de la interfaz gráfica.
     */
    private void apariencia() {
        EstiloInterfaz.aplicarEstilo(botonInsertarDesdeArchivo);
        EstiloInterfaz.aplicarEstilo(botonInsertar);
        EstiloInterfaz.aplicarEstilo(botonBuscar);
        EstiloInterfaz.aplicarEstilo(botonEliminar);

        EstiloInterfaz.aplicarEstilo(panelInsercion);
        EstiloInterfaz.aplicarEstilo(panelBusqueda);

        panelInsercion.add(botonInsertarDesdeArchivo, BorderLayout.CENTER);

        tablaResultados.setBackground(Color.decode("#F0F0F0"));
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Los argumentos de la línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InterfazGraficaProductos frame = new InterfazGraficaProductos();
                frame.setVisible(true);
            }
        });
    }
}


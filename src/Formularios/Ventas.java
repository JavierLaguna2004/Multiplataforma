/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Formularios;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;
import java.time.LocalDate;

/**
 *
 * @author joser
 */
public class Ventas extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;
    private Timer timer;
    
    private int IDEmp;
    private String rol;
    
    private ControlCajaMAN ControlCajaMAN;
    private Connection connection;
    
    MantenimientoVentas MV = new MantenimientoVentas();
    MantenimientoDetalleVentas MDV = new MantenimientoDetalleVentas();
    ConexionSQL cone = new ConexionSQL();
    
    private double subtotal = 0.0;
    private double isv = 0.0;
    private double total = 0.0;
    private int stockTemporal;
    
    int fila;
    int codigo;
    String identidad1 = "";
    
    
    public Ventas(int IDEmp, String rol) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.pintarImagen(this.lblLogo,"src/Formularios/logoSiglas.jpg");
        
        this.connection = connection;
        this.ControlCajaMAN = new ControlCajaMAN(connection);
        
        this.IDEmp = IDEmp;
        this.rol = rol;
        
        //Timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Date ahora = new Date();
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String fechaHora = formato.format(ahora);
                
                
                jLabel14.setText("Fecha y Hora: "+fechaHora);
            }
        });
        timer.start();
        
        llenarpacientesproc();
        llenarsucursalesproc();
        llenarproductosproc();
    }
    
    private Ventas(){}
    
    
    private void Limpiar(){
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtSubtotal.setText("0.00");
        txtDescuento.setText("0.00");
        txtISV.setText("0.00");
        txtTotal.setText("0.00");

        DefaultTableModel model = (DefaultTableModel) jVentas.getModel();
        model.setRowCount(0);

        subtotal = 0.0;
        isv = 0.0;
        total = 0.0;
        stockTemporal = 0;


        cmbProducto.setSelectedIndex(0); 
    }
    
    private void llenarpacientesproc(){
        cmbCliente.setModel(MV.llenarpacientes());
    }
    
    private void llenarsucursalesproc(){
        cmbSucursal.setModel(MV.llenarsucursal());
    }
    
    private void llenarproductosproc(){
        cmbProducto.setModel(MV.llenarproductos());
    }
    
     private void verificarYActualizarControlCaja(double montoVenta, int idEmpleado) {
        LocalDate fechaActual = LocalDate.now();
        
        try {
            ControlCaja controlCaja = ControlCajaMAN.obtenerControlCajaPorFecha(fechaActual);
            
            if (controlCaja == null) {
                ControlCajaMAN.crearControlCaja(fechaActual, 0.0, montoVenta, idEmpleado);
            } else {
                double nuevoMontoFinal = controlCaja.getMontoFinal() + montoVenta;
                ControlCajaMAN.actualizarControlCaja(controlCaja.getId(), nuevoMontoFinal);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void cerrarCaja() {
        LocalDate fechaActual = LocalDate.now();
        
        try {
            ControlCaja controlCaja = ControlCajaMAN.obtenerControlCajaPorFecha(fechaActual);
            
            if (controlCaja != null) {
                // Actualizar el MontoInicial con el MontoFinal
                double montoFinal = controlCaja.getMontoFinal();
                ControlCajaMAN.actualizarMontoInicial(controlCaja.getId(), montoFinal);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }
    
    public void cerrar() {
        ControlCajaMAN.cerrarConexion(); // Cierra la conexión al finalizar
    }
    
    private void guardarVenta(String metodoPago) {
    try {
        Connection con = cone.establecerConexion(); 
        String sql = "INSERT INTO Ventas (Fecha, MontoTotal, MetodoPago, IdPaciente, IdEmpleado) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        stmt.setDouble(2, total);
        stmt.setString(3, metodoPago);
        stmt.setInt(4, cmbCliente.getSelectedIndex() == 0 ? 0 : ((ComboPaciente) cmbCliente.getSelectedItem()).getIdPaciente());
        stmt.setInt(5, cmbSucursal.getSelectedIndex() == 0 ? 0 : ((ComboSucursal) cmbSucursal.getSelectedItem()).getIdSucursal());

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int idVenta = rs.getInt(1);

            guardardetalleventa(idVenta);

            generarFactura(metodoPago);
            
            DefaultTableModel model = (DefaultTableModel) jVentas.getModel();
            model.setRowCount(0);
            subtotal = 0.0;
            isv = 0.0;
            total = 0.0;
            txtSubtotal.setText("0.00");
            txtDescuento.setText("0.00");
            txtISV.setText("0.00");
            txtTotal.setText("0.00");

            JOptionPane.showMessageDialog(this, "La venta se guardó correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void guardardetalleventa(int idVenta) {
    try {
        Connection con = cone.establecerConexion();
        CallableStatement stmt = con.prepareCall("{CALL sp_guardardetalleventa(?,?,?,?)}");

        DefaultTableModel model = (DefaultTableModel) jVentas.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            stmt.setInt(1, idVenta);
            stmt.setInt(2, (int) model.getValueAt(i, 0));
            stmt.setInt(3, (int) model.getValueAt(i, 2));
            stmt.setDouble(4, (double) model.getValueAt(i, 3));

            stmt.executeUpdate();

            int idProducto = (int) model.getValueAt(i, 0);
            int cantidadVendida = (int) model.getValueAt(i, 2);
            CallableStatement stmtStock = con.prepareCall("{CALL sp_actualizarstock(?,?)}");
            stmtStock.setInt(1, idProducto);
            stmtStock.setInt(2, cantidadVendida);
            stmtStock.executeUpdate();
        }

        stmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar los detalles de la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void generarFactura(String metodoPago) {
        ComboSucursal sucursalSeleccionado = (ComboSucursal) cmbSucursal.getSelectedItem();
        String sucursal = sucursalSeleccionado.getnombreSucursal();

        ComboPaciente pacienteSeleccionado = (ComboPaciente) cmbCliente.getSelectedItem();
        String nombrePaciente = pacienteSeleccionado.getnombrePaciente();
        int idPaciente = pacienteSeleccionado.getIdPaciente();

        int numeroFactura = new Random().nextInt(100000);

        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        DefaultTableModel model = (DefaultTableModel) jVentas.getModel();
        StringBuilder detalleServicios = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            int idProducto = (int) model.getValueAt(i, 0);
            String nombreProducto = (String) model.getValueAt(i, 1);
            int cantidad = (int) model.getValueAt(i, 2);
            double precio = (double) model.getValueAt(i, 3);
            detalleServicios.append(String.format("ID: %d | Producto: %s | Cantidad: %d | Precio: %.2f\n", idProducto, nombreProducto, cantidad, precio));
        }

        StringBuilder factura = new StringBuilder();
        factura.append("LABORATORIO MEDICO POLANCO\n\n");
        factura.append("Sucursal: ").append(sucursal).append("\n");
        factura.append("Teléfono: 8545-0025\n");
        factura.append("Correo: info@laboratoriomedicopolanco.com\n\n");
        factura.append("FACTURA N°: ").append(numeroFactura).append("\n");
        factura.append("FECHA: ").append(fecha).append("\n");
        factura.append("PACIENTE: ").append(nombrePaciente).append("\n");
        factura.append("ID PACIENTE: ").append(idPaciente).append("\n");
        factura.append("ID EMPLEADO: ").append(IDEmp).append("\n\n");
        factura.append("DETALLE DE SERVICIOS:\n");
        factura.append(detalleServicios.toString()).append("\n");
        factura.append("SUBTOTAL: ").append(txtSubtotal.getText()).append("\n");
        factura.append("DESCUENTO: ").append(txtDescuento.getText()).append("\n");
        factura.append("ISV (15%): ").append(txtISV.getText()).append("\n");
        factura.append("TOTAL A PAGAR: ").append(txtTotal.getText()).append("\n\n");

        if (metodoPago != null) {
            factura.append("MÉTODO DE PAGO: ").append(metodoPago).append("\n");
        } else {
            factura.append("MÉTODO DE PAGO: No seleccionado\n");
        }

        factura.append("Gracias por confiar en nuestros servicios.\n\n");
        factura.append("NOTA:\n");
        factura.append("Esta factura es válida como comprobante de pago.\n Conserve este documento para futuras referencias.");

        JOptionPane.showMessageDialog(this, factura.toString(), "Factura", JOptionPane.INFORMATION_MESSAGE);
        verificarYActualizarControlCaja(Double.parseDouble(txtTotal.getText()), IDEmp);
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        cmbProducto = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbSucursal = new javax.swing.JComboBox<>();
        cmbCliente = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jVentas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JLabel();
        txtISV = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnCerrarSesion = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        btnPacientes = new javax.swing.JButton();
        btnGuardarVenta = new javax.swing.JButton();
        btnFactura = new javax.swing.JButton();
        btnLimpieza = new javax.swing.JButton();
        btnCancelarVenta = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        cmbFiltroBuscar = new javax.swing.JComboBox<>();
        txtBuscar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1420, 850));

        jPanel6.setBackground(new java.awt.Color(242, 90, 56));

        lblLogo.setText("lblLogo");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 46)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("VENTAS");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1042, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(14, 14, 14))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        cmbProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProductoActionPerformed(evt);
            }
        });

        jLabel15.setText("PRODUCTO:");

        jLabel16.setText("CANTIDAD:");

        jLabel17.setText("PRECIO:");

        txtPrecio.setEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jLabel1.setText("SUCURSAL:");

        jLabel18.setText("PACIENTE:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 53, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18)
                            .addComponent(jLabel1)
                            .addComponent(cmbSucursal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Producto", "Nombre", "Cantidad", "Precio"
            }
        ));
        jScrollPane1.setViewportView(jVentas);

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel4.setText("DESCUENTO:");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 48)); // NOI18N
        jLabel6.setText("TOTAL:");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel8.setText("SUBTOTAL:");

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel9.setText("ISV:");

        txtSubtotal.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        txtSubtotal.setText("00.00");

        txtDescuento.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        txtDescuento.setText("00.00");

        txtISV.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        txtISV.setText("00.00");

        txtTotal.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        txtTotal.setText("00.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubtotal)
                    .addComponent(txtDescuento)
                    .addComponent(txtISV)
                    .addComponent(txtTotal))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSubtotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDescuento))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtISV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCerrarSesion.setText("CERRAR SESION");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        btnSalir.setText("SALIR");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnRegresar.setText("CERRAR CAJA");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        btnPacientes.setText("NUEVO CLIENTE");
        btnPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPacientesActionPerformed(evt);
            }
        });

        btnGuardarVenta.setText("GENERAR VENTA");
        btnGuardarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarVentaActionPerformed(evt);
            }
        });

        btnFactura.setText("IMPRIMIR FACTURA");
        btnFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturaActionPerformed(evt);
            }
        });

        btnLimpieza.setText("NUEVA VENTA");
        btnLimpieza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiezaActionPerformed(evt);
            }
        });

        btnCancelarVenta.setText("CANCELAR VENTA");
        btnCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVentaActionPerformed(evt);
            }
        });

        btnEliminarProducto.setText("ELIMINAR PRODUCTO");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnAgregarProducto.setText("AÑADIR PRODUCTO");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel14.setText("CARGANDO FECHA Y HORA...");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(btnLimpieza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel14)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPacientes, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEliminarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpieza, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnGuardarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(76, 76, 76))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnCancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)))
                        .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        cmbFiltroBuscar.setBackground(new java.awt.Color(255, 255, 255));
        cmbFiltroBuscar.setForeground(new java.awt.Color(39, 65, 140));

        txtBuscar.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(39, 65, 140));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(39, 65, 140));
        jLabel7.setText("Buscar Por:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(39, 65, 140));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Search.png"))); // NOI18N
        jLabel2.setText("BUSCAR:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(cmbFiltroBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(312, 312, 312))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(34, 34, 34))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(cmbFiltroBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(78, 78, 78))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        cerrarCaja();
        if(rol.equals("GER")){
            Menu frmM = new Menu(IDEmp,rol);
            frmM.setVisible(true);
            this.setVisible(false);
        }else if(rol.equals("AAC")){
            MenuAAC frmMAAC = new MenuAAC(IDEmp,rol);
            frmMAAC.setVisible(true);
            this.setVisible(false);
        }else if(rol.equals("ENF")||rol.equals("MIC")){
            MenuENFMIC frmENFMIC = new MenuENFMIC();
            frmENFMIC.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        if(JOptionPane.showConfirmDialog(null, "¿Desea cerrar la sesión actual?", "¡Atencion!", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            Login frmL = new Login();
            frmL.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        if(JOptionPane.showConfirmDialog(null, "¿Desea salir de la sesión actual?", "¡Atencion!", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed

        DefaultTableModel model = (DefaultTableModel) jVentas.getModel();

        ComboProducto productoSeleccionado = (ComboProducto) cmbProducto.getSelectedItem();

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidad.getText();

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad > stockTemporal) {
                JOptionPane.showMessageDialog(this, "La cantidad no puede superar el stock disponible (" + stockTemporal + ").", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String precioStr = txtPrecio.getText();

        if (cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la cantidad y el precio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio;

        try {
            cantidad = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad y el precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idProducto = productoSeleccionado.getIdProducto(); 
        String nombreProducto = productoSeleccionado.getnombreProducto(); 

        model.addRow(new Object[]{idProducto, nombreProducto, cantidad, precio});

        subtotal += precio * cantidad;

        isv = subtotal * 0.18;

        total = subtotal + isv; 

        txtSubtotal.setText(String.format("%.2f", subtotal)); 
        txtDescuento.setText("0.00");
        txtISV.setText(String.format("%.2f", isv));
        txtTotal.setText(String.format("%.2f", total));

        stockTemporal -= cantidad;
        
        txtCantidad.setText("");
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed

    DefaultTableModel model = (DefaultTableModel) jVentas.getModel();

    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No hay productos para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int selectedRow = jVentas.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int cantidad = (int) model.getValueAt(selectedRow, 2);
    double precio = (double) model.getValueAt(selectedRow, 3);

    subtotal -= precio * cantidad;

    isv = subtotal * 0.18;
    total = subtotal + isv;

    txtSubtotal.setText(String.format("%.2f", subtotal));
    txtDescuento.setText("0.00");
    txtISV.setText(String.format("%.2f", isv));
    txtTotal.setText(String.format("%.2f", total));

    model.removeRow(selectedRow);

    if (model.getRowCount() == 0) {
        subtotal = 0.0;
        isv = 0.0;
        total = 0.0;

        txtSubtotal.setText("00.00");
        txtDescuento.setText("00.00");
        txtISV.setText("00.00");
        txtTotal.setText("00.00");
    }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPacientesActionPerformed
        Clientes frmClientes = new Clientes(IDEmp,rol);
        frmClientes.setVisible(true);
    }//GEN-LAST:event_btnPacientesActionPerformed

    private void btnLimpiezaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiezaActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiezaActionPerformed

    private void btnGuardarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVentaActionPerformed
       MetodoPago dialog = new MetodoPago(this);
       dialog.setVisible(true);

    
        String metodoPago = dialog.getMetodoPago();

        if (metodoPago != null) {
            guardarVenta(metodoPago);
        } else {
            JOptionPane.showMessageDialog(this, "No se selecciono ningun metodo de pago.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarVentaActionPerformed

    private void cmbProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProductoActionPerformed
        if(cmbProducto.getSelectedIndex() == 0){
            return;
        }
        
        ComboProducto productoSeleccionado = (ComboProducto) cmbProducto.getSelectedItem();
        
        if (productoSeleccionado != null) {
            int idProducto = productoSeleccionado.getIdProducto();
            
            try {
                Connection con = cone.establecerConexion();
                CallableStatement stmt = con.prepareCall("{CALL sp_productosventas(?)}");
                stmt.setInt(1, idProducto);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double precio = rs.getDouble("Precio");
                    int stock = rs.getInt("Stock");
                    
                    txtPrecio.setText(String.format("%.2f", precio));
                    
                    stockTemporal = stock;
                }
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al obtener el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cmbProductoActionPerformed

    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVentaActionPerformed

    if (JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cancelar la venta?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        Limpiar();
    }
    }//GEN-LAST:event_btnCancelarVentaActionPerformed

    private void btnFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturaActionPerformed
        generarFactura("FACUTRA NO VALIDA, SOLO PARA QUE CLIENTE REVISE DETALLES");
    }//GEN-LAST:event_btnFacturaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }
    
    private void pintarImagen(JLabel lbl, String ruta){
        this.imagen = new ImageIcon(ruta);
        this.icono = new ImageIcon(
                this.imagen.getImage().getScaledInstance(
                        lbl.getWidth(),
                        lbl.getHeight(),
                        Image.SCALE_DEFAULT
                )
        );
        lbl.setIcon(this.icono);
        this.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnFactura;
    private javax.swing.JButton btnGuardarVenta;
    private javax.swing.JButton btnLimpieza;
    private javax.swing.JButton btnPacientes;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbFiltroBuscar;
    private javax.swing.JComboBox<String> cmbProducto;
    private javax.swing.JComboBox<String> cmbSucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jVentas;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JLabel txtDescuento;
    private javax.swing.JLabel txtISV;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JLabel txtSubtotal;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}

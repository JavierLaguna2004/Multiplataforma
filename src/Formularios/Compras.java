/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Formularios;
import javax.swing.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class Compras extends javax.swing.JFrame {

    
    private String rol;
    private int IDEmp;
    ConexionSQL cone = new ConexionSQL();
    MantenimientoCompras MC = new MantenimientoCompras();
    
     private double total = 0.0;
     private int stockTemporal;
    
    public Compras(int IDEmp, String rol) {
        initComponents();
        this.IDEmp = IDEmp;
        this.rol = rol;
        
        this.setLocationRelativeTo(null);
        
        llenarproveedoresproc();
        llenarproductosproc();
    }
    
    private Compras(){}
   
    private void llenarproveedoresproc(){
        cmbProveedor.setModel(MC.llenarproveedores());
    }
    
    private void llenarproductosproc(){
        cmbProducto.setModel(MC.llenarproductos());
    }
    
    private void Limpiar() {
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtTotal.setText("0.00");

        DefaultTableModel model = (DefaultTableModel) jCompras.getModel();
        model.setRowCount(0);
    }
    
    private void generarRecibo() {

        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        int numeroFactura = new Random().nextInt(100000);
        
        DefaultTableModel model = (DefaultTableModel) jCompras.getModel();
        StringBuilder detalleServicios = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            int idProducto = (int) model.getValueAt(i, 0);
            String nombreProducto = (String) model.getValueAt(i, 1);
            int cantidad = (int) model.getValueAt(i, 2);
            double precio = (double) model.getValueAt(i, 3);
            detalleServicios.append(String.format("ID: %d | Producto: %s | Cantidad: %d | Precio: %.2f\n", idProducto, nombreProducto, cantidad, precio));
        }

        StringBuilder compras = new StringBuilder();
        compras.append("LABORATORIO MEDICO POLANCO\n\n");
        compras.append("Teléfono: 8545-0025\n");
        compras.append("Correo: info@laboratoriomedicopolanco.com\n\n");
        compras.append("FACTURA N°: ").append(numeroFactura).append("\n");
        compras.append("FECHA: ").append(fecha).append("\n");
        compras.append("ID EMPLEADO: ").append(IDEmp).append("\n\n");
        compras.append("PROVEEDOR: ").append(cmbProveedor.getSelectedItem()).append("\n");
        compras.append("COMPRAS DE PRODUCTOS:\n");
        compras.append(detalleServicios.toString()).append("\n");
        compras.append("TOTAL A PAGAR: ").append(txtTotal.getText()).append("\n\n");

        compras.append("Gracias por confiar en nuestros servicios.\n\n");
        compras.append("NOTA:\n");
        compras.append("Esta factura es válida como comprobante de pago.\n Conserve este documento para futuras referencias.");

        JOptionPane.showMessageDialog(this, compras.toString(), "Compras", JOptionPane.INFORMATION_MESSAGE);
}
    
    private void guardarCompras() {
    try {
        Connection con = cone.establecerConexion(); 
        String sql = "INSERT INTO Compras (Fecha, MontoTotal, IdProveedor) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        stmt.setDouble(2, total);
        stmt.setInt(3, cmbProveedor.getSelectedIndex() == 0 ? 0 : ((ComboProveedor) cmbProveedor.getSelectedItem()).getIdProveedor());

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int idCompra = rs.getInt(1);

            guardardetallecompra(idCompra);

            generarRecibo();
            
            DefaultTableModel model = (DefaultTableModel) jCompras.getModel();
            model.setRowCount(0);
            total = 0.0;
            txtTotal.setText("0.00");

            JOptionPane.showMessageDialog(this, "La compra se guardó correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar la compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void guardardetallecompra(int idCompra) {
    try {
        Connection con = cone.establecerConexion();
        CallableStatement stmt = con.prepareCall("{CALL sp_guardardetallecompra(?,?,?,?)}");

        DefaultTableModel model = (DefaultTableModel) jCompras.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            stmt.setInt(1, idCompra);
            stmt.setInt(2, (int) model.getValueAt(i, 0));
            stmt.setInt(3, (int) model.getValueAt(i, 2));
            stmt.setDouble(4, (double) model.getValueAt(i, 3));

            stmt.executeUpdate();

            int idProducto = (int) model.getValueAt(i, 0);
            int cantidadComprada = (int) model.getValueAt(i, 2);
            CallableStatement stmtStock = con.prepareCall("{CALL sp_actualizarstocksumar(?,?)}");
            stmtStock.setInt(1, idProducto);
            stmtStock.setInt(2, cantidadComprada);
            stmtStock.executeUpdate();
        }

        stmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar los detalles de la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jCompras = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbBuscar = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnRecibo = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        cmbProducto = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        cmbProveedor = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(237, 235, 236));

        jPanel6.setBackground(new java.awt.Color(242, 90, 56));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 46)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("COMPRAS");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(529, 529, 529))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(237, 235, 236));

        jPanel9.setBackground(new java.awt.Color(237, 235, 236));

        jCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Producto", "Nombre", "Cantidad", "Precio Unitario"
            }
        ));
        jCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jCompras);

        txtBuscar.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(39, 65, 140));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(39, 65, 140));
        jLabel7.setText("Buscar Por:");

        cmbBuscar.setBackground(new java.awt.Color(255, 255, 255));
        cmbBuscar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbBuscar.setForeground(new java.awt.Color(39, 65, 140));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(39, 65, 140));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Search.png"))); // NOI18N
        jLabel2.setText("BUSCAR:");

        jPanel8.setBackground(new java.awt.Color(237, 235, 236));

        btnLimpiar.setBackground(new java.awt.Color(39, 65, 140));
        btnLimpiar.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Delete.png"))); // NOI18N
        btnLimpiar.setText("LIMPIAR");

        btnGuardar.setBackground(new java.awt.Color(39, 65, 140));
        btnGuardar.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Verified.png"))); // NOI18N
        btnGuardar.setText("COMPRAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnRegresar.setBackground(new java.awt.Color(39, 65, 140));
        btnRegresar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Log-in.png"))); // NOI18N
        btnRegresar.setText("REGRESAR");
        btnRegresar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnRegresar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(39, 65, 140));
        btnModificar.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Backspace.png"))); // NOI18N
        btnModificar.setText("CANCELAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnRecibo.setBackground(new java.awt.Color(39, 65, 140));
        btnRecibo.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnRecibo.setForeground(new java.awt.Color(255, 255, 255));
        btnRecibo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Key.png"))); // NOI18N
        btnRecibo.setText("RECIBO");
        btnRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReciboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRegresar)
                .addGap(290, 290, 290)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12), new java.awt.Color(39, 65, 140))); // NOI18N

        cmbProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProductoActionPerformed(evt);
            }
        });

        jLabel15.setForeground(new java.awt.Color(39, 65, 140));
        jLabel15.setText("PRODUCTO:");

        jLabel16.setForeground(new java.awt.Color(39, 65, 140));
        jLabel16.setText("CANTIDAD:");

        jLabel17.setForeground(new java.awt.Color(39, 65, 140));
        jLabel17.setText("PRECIO:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        btnAgregar.setBackground(new java.awt.Color(39, 65, 140));
        btnAgregar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Add-circle.png"))); // NOI18N
        btnAgregar.setText("AGREGAR");
        btnAgregar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(39, 65, 140));
        btnEliminar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Delete.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        cmbProveedor.setBackground(new java.awt.Color(255, 255, 255));
        cmbProveedor.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbProveedor.setForeground(new java.awt.Color(39, 65, 140));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(39, 65, 140));
        jLabel9.setText("PROVEEDOR:");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 48)); // NOI18N
        jLabel6.setText("TOTAL:");

        txtTotal.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(39, 65, 140));
        txtTotal.setText("00.00");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel9Layout.createSequentialGroup()
                                    .addComponent(btnAgregar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEliminar)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotal)))
                        .addGap(109, 109, 109)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cmbBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTotal))
                        .addGap(18, 18, 18)))
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComprasMouseClicked

    }//GEN-LAST:event_jComprasMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarCompras();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        Productos frmP = new Productos(IDEmp, rol);
        frmP.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReciboActionPerformed

    }//GEN-LAST:event_btnReciboActionPerformed

    private void cmbProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProductoActionPerformed

    }//GEN-LAST:event_cmbProductoActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        DefaultTableModel model = (DefaultTableModel) jCompras.getModel();

        ComboProducto productoSeleccionado = (ComboProducto) cmbProducto.getSelectedItem();

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidad.getText();

        int cantidad;
        
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

        total += precio * cantidad;

        txtTotal.setText(String.format("%.2f", total));

        stockTemporal += cantidad;
        
        txtCantidad.setText("");
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        DefaultTableModel model = (DefaultTableModel) jCompras.getModel();

    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No hay productos para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int selectedRow = jCompras.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int cantidad = (int) model.getValueAt(selectedRow, 2);
    double precio = (double) model.getValueAt(selectedRow, 3);

    total -= precio * cantidad;
    txtTotal.setText(String.format("%.2f", total));

    model.removeRow(selectedRow);

    if (model.getRowCount() == 0) {
        total = 0.0;
        txtTotal.setText("00.00");
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRecibo;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbBuscar;
    private javax.swing.JComboBox<String> cmbProducto;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JTable jCompras;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}

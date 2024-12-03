/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author joser
 */
public class MantenimientoSucursales {
    ConexionSQL cone = new ConexionSQL();
    
    public void mantenimientossucursales(int codigo, String sucursal,String direccion,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoSucursales(?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2,sucursal);
             cmd.setString(3,direccion);
             cmd.setString(4,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    public void cargartablaSucursales(JTable tabla, int codigo, String sucursal,String direccion,String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoSucursales(?,?,?,?)}");

             cmd.setInt(1,codigo);
             cmd.setString(2,sucursal);
             cmd.setString(3,direccion);
             cmd.setString(4,accion);

            rs = cmd.executeQuery();
            rsmd = rs.getMetaData();
            columnas = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] filas = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelotabla.addRow(filas);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
}

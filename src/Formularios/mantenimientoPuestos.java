/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;


/**
 *
 * @author PC-Victoria
 */

public class mantenimientoPuestos {
     private ConexionSQL c;

    public mantenimientoPuestos() {
        this.c = new ConexionSQL();
    }


    public void agregarPuesto(String nombre, String descripcion) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo 'Nombre' es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = c.establecerConexion()) {
            String sql = "{CALL Sp_InsertarPuestos(?, ?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setString(1, nombre); // Parámetro @Nombre
            stmt.setString(2, descripcion); // Parámetro @Descripcion

            int filasInsertadas = stmt.executeUpdate();

            if (filasInsertadas > 0) {
                JOptionPane.showMessageDialog(null, "Puesto guardado correctamente.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el puesto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificarPuesto(int idPuesto, String nombre, String descripcion) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo 'Nombre' es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = c.establecerConexion()) {
            String sql = "{CALL Sp_ModificarPuestos(?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, idPuesto); // Parámetro @IdPuesto
            stmt.setString(2, nombre); // Parámetro @Nombre
            stmt.setString(3, descripcion); // Parámetro @Descripcion

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(null, "Puesto modificado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el puesto con el ID proporcionado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el puesto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
  public void cargarTabla(JTable tabla) {
    DefaultTableModel modeloTabla = (DefaultTableModel) tabla.getModel();
    modeloTabla.setRowCount(0); 

    try (Connection con = c.establecerConexion()) {
        String sql = "{CALL sp_mostrarpuestoporid}";
        CallableStatement stmt = con.prepareCall(sql);

        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnas = rsmd.getColumnCount();

        while (rs.next()) {
            Object[] fila = new Object[columnas];
            for (int i = 0; i < columnas; i++) {
                fila[i] = rs.getObject(i + 1); 
            }
            modeloTabla.addRow(fila); 
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void eliminarPuesto(String nombrePuesto, JTable tabla, int idPuesto) {
   
    if (nombrePuesto == null || nombrePuesto.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Debes proporcionar un nombre de puesto.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    
    ConexionSQL conexion = new ConexionSQL();

    try (Connection conn = conexion.establecerConexion()) {
     
        String sql = "{CALL sp_EliminarPuesto(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.setInt(1, idPuesto);

      
        int filasEliminadas = stmt.executeUpdate();

       
        if (filasEliminadas > 0) {
            JOptionPane.showMessageDialog(null, "Puesto eliminado correctamente.");
            
            
            cargarTabla(tabla); 
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el puesto con el nombre especificado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
     
        JOptionPane.showMessageDialog(null, "Error al eliminar el puesto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}


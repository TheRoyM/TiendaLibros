package TheRoy.tienda_libros.Presentacion;

import TheRoy.tienda_libros.modelo.Libro;
import TheRoy.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTexto, idTexto;
    private JLabel Libro;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;

    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e-> modificarLibro());
        eliminarButton.addActionListener(e-> eliminarLibro());
    }
    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if (renglon != -1){
           String idLibro =  tablaLibros.getModel().getValueAt(renglon,0).toString();
           var libro = new Libro();
           libro.setIdLibro(Integer.parseInt(idLibro));
           libroServicio.eliminarLibro(libro);
           mostrarMensaje("Su libro " +idLibro+ " ha sido correctamente eliminado...");
           limpiarFormulario();
           listarLibros();
        }else {
            mostrarMensaje("Debes seleccionar un libro para eliminar...");
        }
    }
    private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debes seleccionar un registro...");
        }else{
            //verificar que le nombre del libro no sea null

            if (libroTexto.getText().equals("")){
                mostrarMensaje("Escriba el nombre del libro...");
                libroTexto.requestFocusInWindow();
                return;
            }
            //llenamos el objeto del libro a actualizar
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var exixtencias = Integer.parseInt(existenciasTexto.getText());
            var libro = new Libro(idLibro, nombreLibro, autor, precio, exixtencias);

            libroServicio.guardarLibro(libro);
            mostrarMensaje("su libro se ha modificado exitosamente");
            limpiarFormulario();
            listarLibros();
        }
    }
    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth()/2);
        int y = (tamanioPantalla.height = getHeight()/2);
        setLocation(x,y);
    }

    private void agregarLibro(){
        //leer los valores del formulario
        if (libroTexto.getText().equals("")){
            mostrarMensaje("Proporciona el nombre del libro");
            libroTexto.requestFocusInWindow();
            return;
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var exixtencias = Integer.parseInt(existenciasTexto.getText());

        //crear el objeto Libro
        var libro = new Libro(null, nombreLibro,autor,precio,exixtencias);
        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se acabao de agregar el libro...");
        limpiarFormulario();
        listarLibros();
    }

    private void cargarLibroSeleccionado(){
        //Los indices de las columnas inician en 0
        var renglon  = tablaLibros.getSelectedRow();
        if (renglon != -1){//regresa -1 si no se seleeciono ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String precio = tablaLibros.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(precio);
            String existencias = tablaLibros.getModel().getValueAt(renglon, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Creamos el elemento idText oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5){
            @Override
            public boolean isCellEditable(int row, int colum){
                return false;
            }
        };
        String[] cabecera = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        tablaModeloLibros.setColumnIdentifiers(cabecera);
        //Instanciar el objeto JTable
        this.tablaLibros = new JTable(tablaModeloLibros);
        //evitar que se seleccionen varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();

    }

    private void listarLibros(){
        //Limpiar tabla
        tablaModeloLibros.setRowCount(0);
        //Obtener los libros
        var libros = libroServicio.listarLibros();
        libros.forEach((libro -> {
            Object[] renglonLibro = {
                   libro.getIdLibro(),
                   libro.getNombreLibro(), libro.getAutor(),
                   libro.getPrecio(), libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        }));
    }
}

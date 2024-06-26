package com.example;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;

import com.example.models.Cliente;
import com.example.models.Producto;
import com.example.models.Tabla;
import com.example.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormularioControllers implements Initializable
{
    private String direccion = Paths.get("").toAbsolutePath().toString();

    private Cliente cliente;
    private List<Producto> productos;
    private List<Object> productos_escogidos = new ArrayList<>();
    private double[] costoTotal = {0.0};
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @FXML
    private TextField entradaCedula;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoApellido;
    @FXML
    private TextField campoCelular;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoFechaNacimiento;
    @FXML
    private TextField campoEstado;
    @FXML
    private TextField campoCedulaRuc;
    @FXML
    private ChoiceBox<String> choice_box;

    String[] opciones = {"Validar Por Cedula", "Validar Por Ruc"};


    /* Definiremos a continuacion unos botones el cual estan destinados para la parte
     * de productos.
     */
    @FXML
    private Button botonInsertar;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonEditar;
    /* Los campos se pondran para calcular de manera automatica a los productos */
    @FXML
    private ChoiceBox campoProducto;
    @FXML
    private TextField campoCantidad;


    /* Nuestra siguiente variable es nuestra tabla */
    @FXML 
    private TableView tabla;

    @FXML 
    private TableColumn codigo;
    
    @FXML 
    private TableColumn producto;
    
    @FXML 
    private TableColumn cantidad;

    @FXML 
    private TableColumn valorUnitario;

    private ObservableList<Tabla> registros;

    @FXML
    private Text campoCostoTotal;


    /*
    * Los campos de texto a continuacion pertenecen para editar el producto seleccionado de la tabla
    *  */
    TextField FieldCodigo = new TextField();
    TextField FieldProducto = new TextField();
    TextField FieldCantidad = new TextField();
    TextField FieldValorUnitario = new TextField();

    //el texto para los campos de arriba
    Label titulo = new Label("EDITAR PRODUCTO");
    Label LabelCodigo = new Label("Código: ");
    Label LabelProducto = new Label("Producto: ");
    Label LabelCantidad = new Label("Cantidad: ");
    Label LabelValorUnitario = new Label("Valor Unitario: ");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) 
    {
        //permitir la seleccion multiple
        this.tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //encargado de la parte de la tabla.
        this.registros = FXCollections.observableArrayList();

        this.codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.producto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        this.cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        this.valorUnitario.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));

        this.AccesoCampos(true, "usuario");
        this.AccesoCampos(true, "productos");
        
        this.validandoCampoOpciones();
    }

    public void validandoCampoOpciones()
    {
        this.choice_box.setOnAction(evento -> {
            String entrada = this.campoCedulaRuc.getText().toString();

            switch( this.choice_box.getValue().toString() )
            {
                case "Validar Por Cedula":
            
                    if ( verificaCedula(entrada) )
                    {
                        this.resetearCampoOpciones();
                        this.AccesoCampos(false, "productos");
                        this.mensaje(false, "Cédula", "Validando Cédula", "Número de cédula válido");
                    }
                    else 
                    {
                        this.resetearCampoOpciones();
                        this.AccesoCampos(true, "productos");
                        this.mensaje(true, "Cédula", "Validando Cédula Identidad", "campo vacio o cedula incorrecta");
                    }
    
                    break;
    
                case "Validar Por Ruc":
                    
                    if( verificarRUC(entrada) )
                    {
                        this.resetearCampoOpciones();
                        this.AccesoCampos(false, "productos");
                        this.mensaje(false, "RUC", "Validando RUC", "Número de RUC válido");
                    }
                    else 
                    {
                        this.resetearCampoOpciones();
                        this.AccesoCampos(true, "productos");
                        this.mensaje(true, "RUC", "Validando RUC", "campo vacio o numero RUC inválido");
                    }
    
                    break;
                default:
                    this.mensaje(true, "Error", "Verificar Campo Opciones", "No hay coincidencia de opciones");
                    break;
            }
        });

    }

    //este metodo de aqui escuchara para cuando la persona teclee en el campo... los botones de (eliminar) (registrar) (insertar)
    //se bloqueen... para que nuevamente se escoga la opcion de validar por...
    public void escuchandoCampoCedulaRuc(){ this.AccesoCampos(true, "productos"); }

    public void resetearCampoOpciones()
    {
        this.choice_box.getItems().clear();
        this.choice_box.getItems().addAll(opciones);
    }

    public void AccesoCampos(boolean acceso, String tipo)
    {
        if( tipo.equals("usuario") )
        {
            //Algunos campos quedan desactivados...
            this.campoNombre.setDisable(acceso);
            this.campoApellido.setDisable(acceso);
            this.campoCelular.setDisable(acceso);
            this.campoEmail.setDisable(acceso);
            this.campoFechaNacimiento.setDisable(acceso);
            this.campoEstado.setDisable(acceso);
        }
        else if( tipo.equals("productos") )
        {
            this.botonEliminar.setDisable(acceso);
            this.botonInsertar.setDisable(acceso);
            this.botonRegistrar.setDisable(acceso);
            this.botonEditar.setDisable(acceso);
        }

    }

    @FXML
    public String eventoTeclado()
    {
        return entradaCedula.getText().toString();
    }

    /*
    * Funcion esta encargado de rellenar los campos sobre los datos del cliente.
    * */
    public void eventoBoton()
    {
        String cedula = this.eventoTeclado();
      
        if(cedula.length() == 10 && this.verificaCedula(cedula))
        {
            if( ( this.cliente = ClienteRepository.VerificarCiudadano(cedula) ) != null ) // verificamos si la persona sigue viguente o no con dicha cedula.
            {
                this.resetearCampoOpciones();

                campoNombre.setText(this.cliente.getCiudadano_nombre());
                campoApellido.setText(this.cliente.getCiudadano_apellido());
                campoCelular.setText(this.cliente.getCiudadano_celular());
                campoEmail.setText(this.cliente.getCiudadano_email());
                campoFechaNacimiento.setText(this.cliente.getCiudadano_fechaNacimiento());
                campoCedulaRuc.setText(this.cliente.getCiudadano_cedula());
                campoEstado.setText("vivo");

                //vamos a llenar nuestros productos
                LlenarProductos();

                if(this.campoProducto.getItems().size() != 0)
                    this.campoProducto.getItems().clear();

                this.productos.stream().forEach(p -> {
                    this.campoProducto.getItems().add(p.getProductoNombre());
                });

            }
            else 
            {
                mensaje(true, "VERIFICAR PERSONA", "ATENCION", "Hemos encontrado a una persona no vigente.");
            }
        }
        else 
        {
            mensaje(true, "VERIFICAR CEDULA", "ERROR", "Numero de cedula incorrecta o campo vacio");
        }
    }

    public void insertarOAgregarCantidadProducto(int identificadorProducto, int cantidad, boolean tipo)
    {
        this.productos = this.productos.stream()
                .map(producto -> {
                    if(producto.getProductoId() == identificadorProducto)
                    {
                        if(tipo)
                            producto.setProductoCantidad(producto.getProductoCantidad() - cantidad);
                        else
                            producto.setProductoCantidad(producto.getProductoCantidad() + cantidad);
                    }
                    return producto;
                })
                .collect(Collectors.toList());
    }

    /*
     * Dentro de esta funcion, esta nos permitira introducirlo dentro de 
     * nuestra tabla. 
     */
    @FXML
    public void EventoBotonInsertarProducto()
    {
        String producto = this.campoProducto.getValue().toString();
        int cantidad = Integer.parseInt(this.campoCantidad.getText());

        int indice = this.productos.indexOf(new Producto(0, producto, cantidad, 0.0));

        if( indice != -1 )
        {
            Producto registroProducto = this.productos.get(indice);

            if(registroProducto.getProductoCantidad() >= cantidad && cantidad <= registroProducto.getProductoCantidad())
            {
                indice = this.tabla.getItems().indexOf(new Tabla(0, "", producto, cantidad, registroProducto.getProductoValorUnitario()));

                if( indice != -1 )
                {
                    ( (Tabla) this.tabla.getItems().get(indice) ).setCantidad(
                            ( (Tabla) this.tabla.getItems().get(indice) ).getCantidad() + cantidad
                    );
                    this.tabla.refresh();

                    //actualizamos la parte de -- productoEscogido --
                    indice = this.productos_escogidos.indexOf(new Tabla(0, "", producto, cantidad, registroProducto.getProductoValorUnitario()));

                    if(indice != -1)
                    {
                        ( (Tabla) this.productos_escogidos.get(indice) ).setCantidad(
                                ( (Tabla) this.productos_escogidos.get(indice) ).getCantidad() + cantidad
                        );
                    }
                }
                else
                {
                    String codigoAleatorio = this.codigoAleatorio();
                    //definimos un random... para generar nuestro codigo unico...
                    Tabla p = new Tabla(codigoAleatorio, producto, cantidad, registroProducto.getProductoValorUnitario());

                    this.registros.add(p);
                    this.tabla.getItems().clear();
                    this.tabla.getItems().addAll(this.registros);

                    this.productos_escogidos.add(new Tabla(
                            registroProducto.getProductoId(),
                            codigoAleatorio,
                            producto,
                            cantidad,
                            registroProducto.getProductoValorUnitario()
                    ));
                }

                //actualizar productos
                this.insertarOAgregarCantidadProducto(registroProducto.getProductoId(), cantidad, true);

                //tenemos que calcular nuestro costo para que se vaya actualizando constantemente.
                this.CalcularCostoTotal();
            }
            else
            {
                mensaje(true, "VERIFICANDO EN BODEGA", "ATENCION", "No existe dicha cantidad.");
            }
        }
        else
        {
            mensaje(true, "PRODUCTOS", "BUSQUEDA DE PRODUCTOS", "No existe el producto en bodega.");
        }
    }
    

    @FXML
    public void EventoBotonEliminarProducto()
    {
        try
        {
            //crearemos una copia modificable de los valores de la tabla... (pero solamente de los que fueron seleccionados)
            List<Tabla> registrosSeleccionados = new ArrayList<>(List.copyOf( this.tabla.getSelectionModel().getSelectedItems() ));

            if(registrosSeleccionados.size() > 0)
            {
                registrosSeleccionados.forEach(productoSeleccionado -> {

                    int indice = this.productos.indexOf(new Producto(
                            productoSeleccionado.getId(),
                            productoSeleccionado.getProducto(),
                            productoSeleccionado.getCantidad(),
                            productoSeleccionado.getValorUnitario()
                    ));

                    Producto producto = this.productos.get(indice);

                    //volver a sumar la cantidad de productos sacados nuevamente con la cantidad de los productos actuales.
                    insertarOAgregarCantidadProducto(producto.getProductoId(), productoSeleccionado.getCantidad() , false);

                    //eliminar de mi lista de productos escogidos del cliente, el cual estan almacenados en : this.productos_escogidos
                    indice = this.productos_escogidos.indexOf( new Tabla(
                            productoSeleccionado.getCodigo(),
                            productoSeleccionado.getProducto(),
                            productoSeleccionado.getCantidad(),
                            productoSeleccionado.getValorUnitario()
                    ) );

                    if(indice != -1)
                        this.productos_escogidos.remove(this.productos_escogidos.get(indice));

                    
                    //eliminar de mi tabla de producto...
                    if ( this.tabla.getItems().contains(productoSeleccionado) )
                    {
                        this.tabla.getItems().remove(productoSeleccionado);
                    }

                    //eliminarlo de registros...
                    this.registros.remove(productoSeleccionado);
                });


                //tenemos que calcular nuestro costo para que se vaya actualizando constantemente.
                this.CalcularCostoTotal();
          
                this.tabla.refresh();

                return;
            }

            mensaje(true, "ELIMINACION DE PRODUCTO", "ERROR", "Escoge un producto a eliminar");
        }
        catch (Exception e)
        {
            mensaje(true, "PROBLEMA DE CONEXION", "ERROR", "Vuelve a intentarlo mas tarde");
        }
    }


    @FXML
    public void EventoBotonActualizarProducto()
    {
        try
        {
            List<Tabla> productoSeleccionado = this.tabla.getSelectionModel().getSelectedItems();

            if ( productoSeleccionado.size() > 0 && productoSeleccionado.size() == 1)
            {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setResult(ButtonType.CLOSE);

                DialogPane panelDialogo = new DialogPane();
                panelDialogo.setPrefWidth(450.0d);
                dialog.setDialogPane(panelDialogo);

                //damos un texto a nuestro tituloo del Dialogo Panel
                titulo.getStyleClass().add("titulo-editar");
                //titulo para la ventana Dialog
                dialog.setTitle("Editar Producto");

                HBox hbox = new HBox(titulo);
                hbox.getStyleClass().add("hbox-titulo");
                hbox.setPrefWidth(panelDialogo.getPrefWidth());
                hbox.setAlignment(Pos.CENTER);

                FieldCodigo.getStyleClass().add("field-estilo");
                FieldCodigo.setText(productoSeleccionado.get(0).getCodigo());
                FieldCodigo.setDisable(true);
                LabelCodigo.getStyleClass().add("label-estilo");

                FieldProducto.getStyleClass().add("field-estilo");
                FieldProducto.setText(productoSeleccionado.get(0).getProducto());
                FieldProducto.setDisable(true);
                LabelProducto.getStyleClass().add("label-estilo");

                FieldCantidad.getStyleClass().add("field-estilo");
                FieldCantidad.setText( Integer.toString( productoSeleccionado.get(0).getCantidad() ) );
                LabelCantidad.getStyleClass().add("label-estilo");

                FieldValorUnitario.getStyleClass().add("field-estilo");
                FieldValorUnitario.setText( Double.toString( productoSeleccionado.get(0).getValorUnitario() ) );
                LabelValorUnitario.getStyleClass().add("label-estilo");

                Button btnGuardar = new Button("Guardar");
                btnGuardar.getStyleClass().add("boton-guardar");
                btnGuardar.setOnAction(e -> {
                    Tabla productoEditar = new Tabla(
                        FieldCodigo.getText(),
                        FieldProducto.getText(),
                        Integer.parseInt( FieldCantidad.getText() ),
                        Double.parseDouble( FieldValorUnitario.getText() )
                    );
                    
                    //esta funcion ira a la tabla y actualizar el producto con el codigo asignado.
                    if( ActualizarRegistroProductoEnTabla(productoEditar, productoSeleccionado.get(0).getCantidad()) )
                        dialog.close();
                      
                });

                Button btnCancelar = new Button("Cancelar");
                btnCancelar.getStyleClass().add("boton-cancelar");
                btnCancelar.setOnAction(e -> {

                    dialog.close();
                });

                HBox hbox2 = new HBox(
                        btnGuardar,
                        btnCancelar
                );
                hbox2.setPadding(new Insets(10.0d, 0.0d, 0.0d, 0.0d));
                hbox2.getStyleClass().add("hbox-botones");

                // configuraciones adicionales para nuestro panel de dialogo.
                panelDialogo.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
                panelDialogo.setContent(new VBox(
                        hbox,
                        LabelCodigo, FieldCodigo,
                        LabelProducto, FieldProducto,
                        LabelCantidad, FieldCantidad,
                        LabelValorUnitario, FieldValorUnitario,
                        hbox2
                ));

                dialog.showAndWait();
            }
            else
            {
                mensaje(true, "ACTUALIZACION DE PRODUCTO", "ERROR", "Escoge solo un producto para actualizar");
            }
        }
        catch (Exception e)
        {
            mensaje(true, "PROBLEMA DE CONEXION", "ERROR", "Vuelve a intentarlo mas tarde");
        }
    }

    public boolean ActualizarRegistroProductoEnTabla(Tabla productoEditar, int cantidadAntigua)
    {
        //actualizamos nuestra lista de productos de la base de datos.
        int indice = this.productos.indexOf(new Producto(0, productoEditar.getProducto(), 0, 0.0));

        if( productoEditar.getCantidad() > (cantidadAntigua + this.productos.get(indice).getProductoCantidad() ) )
        {
            mensaje(true, "ACTUALIZACION DE PRODUCTO", "ERROR", "No existe dicha cantidad en bodega");
        }
        else if ( productoEditar.getCantidad() <= (cantidadAntigua + this.productos.get(indice).getProductoCantidad() ) )
        {
            indice = this.tabla.getItems().indexOf(productoEditar);
        
            if(indice != -1)
            {
                //actualizamos el registro de nuestra tabla
                ( (Tabla) this.tabla.getItems().get(indice) ).setCantidad(productoEditar.getCantidad());
                ( (Tabla) this.tabla.getItems().get(indice) ).setValorUnitario(productoEditar.getValorUnitario());
                
                //actualizamos nuestros productos escogidos.
                indice = this.productos_escogidos.indexOf(new Tabla(
                    productoEditar.getCodigo(),
                    productoEditar.getProducto(),
                    productoEditar.getCantidad(),
                    productoEditar.getValorUnitario()
                ));

                if( indice != -1 )
                {
                   ( (Tabla) this.productos_escogidos.get(indice) ).setCantidad( productoEditar.getCantidad() );
                   ( (Tabla) this.productos_escogidos.get(indice) ).setValorUnitario( productoEditar.getValorUnitario() );
                }

                //actualizamos la cantidad del producto que es de la base de datos.
                indice = this.productos.indexOf(new Producto(0, productoEditar.getProducto(), 0, 0.0));

                if(indice != -1)
                {
                    if( productoEditar.getCantidad() < cantidadAntigua )
                    {
                        cantidadAntigua -= productoEditar.getCantidad();
                        insertarOAgregarCantidadProducto(
                            this.productos.get(indice).getProductoId(), 
                            cantidadAntigua, 
                            false); //poner
                    }
                    else if( productoEditar.getCantidad() > cantidadAntigua )
                    {
                        productoEditar.setCantidad(productoEditar.getCantidad() - cantidadAntigua);
                        insertarOAgregarCantidadProducto(
                            this.productos.get(indice).getProductoId(), 
                            productoEditar.getCantidad(), 
                            true); //quitar
                    }
                }

                //actualizamos el costo total...
                CalcularCostoTotal();
                this.tabla.refresh();

                return true;
            }
            
        }

        return false;
    }

    public String EnviarFacturaFinalGuardar(int idCliente)
    {
        try {
            OkHttpClient c = new OkHttpClient();
            String url = "http://localhost:5019/InsertarFacturaFinal";

            String codigoAleatorio = this.codigoAleatorio();

            DateTime fechaActual = DateTime.now();

            System.out.println(codigoAleatorio);

            HashMap<String, Object> json_registro = new HashMap<>();
            json_registro.put("facturaId", "0");
            json_registro.put("facturaCodigo", codigoAleatorio);
            json_registro.put("facturaFecha", fechaActual.toString());
            json_registro.put("facturaIdCliente", Integer.toString(idCliente));
            json_registro.put("facturaTotal", "0"); //en la base de datos este campo se calcula solo...

            List<HashMap<String, String>> listado_registros = new ArrayList<>();

            ( (List<Object>) this.productos_escogidos).stream().forEach( e -> {
                Tabla t = (Tabla) e;
                HashMap<String, String> json_registro2 = new HashMap<>();
                double total = ( t.getCantidad() * t.getValorUnitario());
                json_registro2.put("detalleFacturaId", "0");
                json_registro2.put("detalleFacturaCodigoFactura", codigoAleatorio);
                json_registro2.put("detalleFacturaProductoId", Integer.toString( t.getId() ) );
                json_registro2.put("detalleFacturaCantidad", Integer.toString( t.getCantidad() ) );
                json_registro2.put("detalleFacturaValorUnitario", Double.toString( t.getValorUnitario() ));
                json_registro2.put("detalleFacturaValorTotal", Double.toString( total ));

                listado_registros.add(json_registro2);
            });

            json_registro.put("detalleFacturas", listado_registros);

            ObjectMapper objMapper = new ObjectMapper();

            String obj = objMapper.writeValueAsString(json_registro);

            RequestBody body = RequestBody.create(obj, JSON);

            Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

            Response response = c.newCall(request).execute();

            return "ok";
        } 
        catch (Exception e) { }
        
        return "sin_guardar";
    }

    public String EnviarClienteAGuardar()
    {
        try {
            OkHttpClient c = new OkHttpClient();
            String url = "http://localhost:5019/agregarCliente";

            HashMap<String, String> json_registro = new HashMap<>();
            json_registro.put("clienteId", "0");
            json_registro.put("clienteNombre", this.cliente.getCiudadano_nombre().toString());
            json_registro.put("clienteCedula", this.cliente.getCiudadano_cedula().toString());

            Gson gson = new Gson();
            String json = gson.toJson(json_registro);

            RequestBody body = RequestBody.create(json, JSON);

            Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

            Response response = c.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            // TODO: handle exception
            mensaje(true, "PROBLEMA DE CONEXION", "ERROR", "Vuelve a intentarlo mas tarde");
        }

        return "0";
    }


    public void EnviarAGuardar()
    {
        try {
            String resultado_almacenar_usuarios = EnviarClienteAGuardar();
            
            if( resultado_almacenar_usuarios != "0"  && !(resultado_almacenar_usuarios.isEmpty()) )
            {
                String resultado_almacenar_facturaFinal = EnviarFacturaFinalGuardar(Integer.parseInt( resultado_almacenar_usuarios ));
            
                if(resultado_almacenar_facturaFinal != "sin_guardar")
                {
                    mensaje(false, "REGISTRANDO COMPRA FINAL", "REGISTRO", "Su compra ha sido procesado correctamente");

                    this.registros.clear();

                    return;
                }
                mensaje(true, "REGISTRANDO COMPRA FINAL", "ERROR", "Su compra no ha podido ser registrada, intentalo otra vez.");
            }
        } catch (Exception e) {
            // TODO: handle exception
            mensaje(true, "PROBLEMA DE CONEXION", "ERROR", "Vuelve a intentarlo mas tarde");
        }
    }

    public void LlenarProductos()
    {
        try {
            OkHttpClient c = new OkHttpClient();
            String _url = "http://localhost:5019/obtenerProductos";

            Request request = new Request.Builder()
                    .url(_url)
                    .build();

            Response response = c.newCall(request).execute();
            
            JSONArray registros = new JSONArray(response.body().string());

            List<Producto> productosFinales = ( (ArrayList<Object>) registros.toList() ).stream()
                    .map(producto -> {
                        HashMap r = (HashMap) producto;
                        return new Producto(
                            (int) r.get("productoId"),
                            (String) r.get("productoNombre"),
                            (int) r.get("productoCantidad"),
                            ((BigDecimal) r.get("productoPrecioUnitario")).doubleValue()
                        );
                    })
                    .collect(Collectors.toList());

            this.productos = productosFinales;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            // TODO: handle exception
            mensaje(true, "PROBLEMA DE CONEXION", "ERROR", "Vuelve a intentarlo mas tarde");
        }

    }

    
    /*
    * Este es para que se muestre en la interfaz, el calculo
    * */
    public void CalcularCostoTotal()
    {
        this.costoTotal[0] = 0;
        this.tabla.getItems().stream()
            .forEach(e ->  this.costoTotal[0] +=  ( (Tabla) e ).getCantidad() * ( (Tabla) e ).getValorUnitario()   );  
            
        
        //mostramos dentro de la parte de la interfaz el costo total...
        this.campoCostoTotal.setText( String.format("%.2f", this.costoTotal[0] ) );
    }


    public String codigoAleatorio()
    {
        Random aleatorio = new Random();
        String codigo = "";

        String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] numeros = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        
        for(int i = 0; i < 5; i++)
        {
            int numero = aleatorio.nextInt(25);
            int numero2 = aleatorio.nextInt(10);

            codigo += letras[numero];
            codigo += numeros[numero2];
        }

        return codigo;
    }

    public void mensaje(boolean tipo, String cabecera, String titulo, String contenido)
    {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);

        if( tipo )
        {
            mensaje = new Alert(Alert.AlertType.ERROR);
        }


        mensaje.setHeaderText(cabecera);
        mensaje.setTitle(titulo);
        mensaje.setContentText(contenido);
        mensaje.showAndWait();
    }


    public boolean verificarRUC(String numero)
    {
        if(numero.length() >= 13 && numero.length() <= 14)
        {
            //validar si es una cedula valida....
            if( !NumberUtils.isDigits(numero) )
            {
                return false;
            }
            String expresion = "";

            //sacaremos el tercero numero verificador.
            int tercerDigito = Integer.parseInt( numero.split(expresion)[2] );

            if( tercerDigito >= 0 && tercerDigito < 6 )
            {
                boolean[] resultado = {false};
                String[] numeroCedula = {""};
                Arrays.stream(Arrays.copyOfRange(numero.split(expresion), 0, 10)).forEach(n -> {
                    numeroCedula[0] += n;
                    if(numeroCedula[0].length() == 10)
                        resultado[0] = this.verificaCedula(numeroCedula[0]);
                });

                if( !resultado[0] )
                    return resultado[0];
            }

            return modulo11(tercerDigito, numero.split(expresion));
        }

        return false;
    }

    public boolean modulo11(int tipoDigito, String[] ruc)
    {
        int[] coeficientes;
        int[] resultadoFinal = {0};
        int decimoDigito = 0;

        if( tipoDigito == 9 )
        {
            coeficientes = new int[]{4, 3, 2, 7, 6, 5, 4, 3, 2};

            decimoDigito = Integer.parseInt( ruc[9] );
            IntStream.range(0, 9).forEach(indice -> {
                int resultado = coeficientes[indice] * Integer.parseInt(ruc[indice]);
                resultadoFinal[0] += resultado;
            });

        }
        else if ( tipoDigito == 6 )
        {
            coeficientes = new int[]{3, 2, 7, 6, 5, 4, 3, 2};

            decimoDigito = Integer.parseInt( ruc[8] );
            IntStream.range(0, 8).forEach(indice -> {
                int resultado = coeficientes[indice] * Integer.parseInt(ruc[indice]);
                resultadoFinal[0] += resultado;
            });
        }

        resultadoFinal[0] = resultadoFinal[0]%11;

        if( resultadoFinal[0] == 0 && resultadoFinal[0] == decimoDigito )
        {
            return  true;
        }

        //le resta con el modulo 11
        resultadoFinal[0] = 11-resultadoFinal[0];

        if ( resultadoFinal[0] != decimoDigito )
        {
            return false;
        }

        return true;
    }

    /**
     * PROPIEDAD SACADAD DE GITHUB
     * >>> https://github.com/RandyMejiaArias/ValidacionCedulaEcuatoriana/blob/master/ValidaCedula.java
     */
    public boolean verificaCedula(String numeroCedula)
    {
        int suma=0;
        String digitosProvincia = numeroCedula.substring(0,2);
        if(numeroCedula.length()!= 10 || Integer.parseInt(digitosProvincia) > 24 || Integer.parseInt(digitosProvincia) < 0 || Integer.parseInt(String.valueOf(numeroCedula.charAt(2))) > 6)
            return false;
        else{
            int a[]=new int [numeroCedula.length()/2];
            int b[]=new int [(numeroCedula.length()/2)];
            int c=0;
            int d=1;
            for (int i = 0; i < numeroCedula.length()/2; i++) 
            {
                a[i]=Integer.parseInt(String.valueOf(numeroCedula.charAt(c)));
                c=c+2;
                if (i < (numeroCedula.length()/2)-1) {
                  b[i]=Integer.parseInt(String.valueOf(numeroCedula.charAt(d)));
                  d=d+2;
                }
            }

            for (int i = 0; i < a.length; i++) 
            {
                a[i]=a[i]*2;
                if (a[i] >9)
                  a[i]=a[i]-9;
                suma=suma+a[i]+b[i];
            } 
            int aux=suma/10;
            int dec=(aux+1)*10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(numeroCedula.charAt(numeroCedula.length()-1))))
                return true;
            else
                if(suma%10==0 && numeroCedula.charAt(numeroCedula.length()-1)=='0')
                  return true;
                else
                  return false;
        }
    }
}

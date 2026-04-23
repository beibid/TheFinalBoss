package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ResourceBundle;


public class PracticanteControlGUI implements Initializable{
    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidoPaterno;

    @FXML
    private TextField txtApellidoMaterno;

    @FXML
    private RadioButton rdiobtnFemenino;

    @FXML
    private RadioButton rdiobtnMasculino;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelarRegistro;

    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void botonRegresar(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void radioBoton(){
        String genero = "";
        if (rdiobtnFemenino.isSelected()) {
            genero = "Femenino";
        } else if (rdiobtnMasculino.isSelected()) {
            genero = "Masculino";
        }
    }

}

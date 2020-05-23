package org.Presentation;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.Logic.GearException;
import org.Logic.Settings;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;


public class SettingsController {
    private Settings settings;
    private DashboardController dashboardController;
    @FXML
    private Slider SLdashboardLightIntensity;
    @FXML
    private ToggleSwitch TSautoLowBeam;
    @FXML
    private ToggleSwitch TSshuffleMode;
    @FXML
    private RadioButton RDengineTypePetrol;
    @FXML
    private RadioButton RDengineTypeDiesel;
    @FXML
    private RadioButton RDnumberOfGears5;
    @FXML
    private RadioButton RDnumberOfGears6;
    @FXML
    private Button saveButton;
    @FXML
    private TextField TFplaylistFolderPath;
    @FXML
    private TextFieldLimited TFLmaxSpeed;

    public void loadSettings(Settings settings, DashboardController dashboardController) {
        this.settings = settings;
        this.dashboardController = dashboardController;
        TFLmaxSpeed.setText(Short.toString(this.settings.getMaxSpeed()));
        SLdashboardLightIntensity.setValue(this.settings.getDashboardLightIntesity());
        TSautoLowBeam.setSelected(this.settings.isAutoLowBeam());
        if (this.settings.getEngineType() == 'P') {
            RDengineTypePetrol.setSelected(true);
            RDengineTypeDiesel.setSelected(false);
        } else {
            RDengineTypePetrol.setSelected(false);
            RDengineTypeDiesel.setSelected(true);
        }
        if (this.settings.getNumberOfGears() == 5) {
            RDnumberOfGears5.setSelected(true);
            RDnumberOfGears6.setSelected(false);
        } else {
            RDnumberOfGears5.setSelected(false);
            RDnumberOfGears6.setSelected(true);
        }
        TSshuffleMode.setSelected(this.settings.isShuffleMode());
        TFplaylistFolderPath.setText(this.settings.getPlaylistDirectoryPath());
    }

    @FXML
    private void save() throws GearException {
        settings.setMaxSpeed(Short.parseShort(TFLmaxSpeed.getText()));
        settings.setDashboardLightIntesity((short) SLdashboardLightIntensity.getValue());
        settings.setAutoLowBeam(TSautoLowBeam.isSelected());
        settings.setEngineType(RDengineTypePetrol.isSelected() ? 'P' : 'D');
        //settings.setMaxRevs((short) (RDengineTypePetrol.isSelected() ? 8000 : 6000));
        settings.setNumberOfGears((byte) (RDnumberOfGears5.isSelected() ? 5 : 6));
        settings.setShuffleMode(TSshuffleMode.isSelected());
        settings.setPlaylistDirectoryPath(TFplaylistFolderPath.getText());
        if (dashboardController.getDashboard().getCurrentGear() > settings.getNumberOfGears())
            dashboardController.getDashboard().setCurrentGear((short) 5, true);
        dashboardController.reloadAfterSettings();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void lockSettings(boolean lock) {
        TFLmaxSpeed.setDisable(lock);
        RDengineTypePetrol.setDisable(lock);
        RDengineTypeDiesel.setDisable(lock);
        RDnumberOfGears5.setDisable(lock);
        RDnumberOfGears6.setDisable(lock);
    }

    @FXML
    private void chooseDirectory(MouseEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node) e.getSource()).getScene().getWindow());
        if (selectedDirectory != null)
            TFplaylistFolderPath.setText(selectedDirectory.getAbsolutePath());
    }

}

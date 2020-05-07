package org.Presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.Logic.Settings;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;

public class SettingsController {
    private Settings settings;
    DashboardController dashboardController;
    public Slider SLdashboardLightIntensity;
    public ToggleSwitch TSautoLowBeam;
    public RadioButton RDengineTypePetrol;
    public RadioButton RDengineTypeDiesel;
    public RadioButton RDnumberOfGears5;
    public RadioButton RDnumberOfGears6;
    public ToggleSwitch TSshuffleMode;
    public Button saveButton;
    public TextFieldLimited TFLmaxSpeed;

    public void loadSettings(Settings settings, DashboardController dashboardController) {
        this.settings = settings;
        this.dashboardController = dashboardController;
        TFLmaxSpeed.setText(Short.toString(this.settings.maxSpeed));
        SLdashboardLightIntensity.setValue(this.settings.dashboardLightIntesity);
        TSautoLowBeam.setSelected(this.settings.autoLowBeam);
        if(this.settings.engineType == 'P') {
            RDengineTypePetrol.setSelected(true);
            RDengineTypeDiesel.setSelected(false);
        }
        else {
            RDengineTypePetrol.setSelected(false);
            RDengineTypeDiesel.setSelected(true);
        }
        if(this.settings.numberOfGears == 5) {
            RDnumberOfGears5.setSelected(true);
            RDnumberOfGears6.setSelected(false);
        }
        else {
            RDnumberOfGears5.setSelected(false);
            RDnumberOfGears6.setSelected(true);
        }
        TSshuffleMode.setSelected(this.settings.shuffleMode);
    }

    @FXML
    private void save() {
        settings.maxSpeed = Short.parseShort(TFLmaxSpeed.getText());
        settings.dashboardLightIntesity = (short)SLdashboardLightIntensity.getValue();
        settings.autoLowBeam = TSautoLowBeam.isSelected();
        settings.engineType = RDengineTypePetrol.isSelected() ? 'P' : 'D';
        settings.maxRevs = (short) (RDengineTypePetrol.isSelected() ? 8000 : 6000);
        settings.numberOfGears = (byte)(RDnumberOfGears5.isSelected() ? 5 : 6);
        settings.shuffleMode = TSshuffleMode.isSelected();

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

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}

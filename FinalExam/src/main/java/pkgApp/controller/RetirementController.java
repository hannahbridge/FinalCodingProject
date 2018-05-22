package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");
		
		hmTextFieldRegEx.put(txtYearsRetired, "\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtRequiredIncome, "\\d*?");
		hmTextFieldRegEx.put(txtMonthlySSI, "\\d*?" );

		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}
	}

	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			txtField.clear();
			txtField.setDisable(false);
		}
	}

	@FXML
	public void btnCalculate() {

		String yTW = txtYearsToWork.getText();
		int yearsToWork = Integer.parseInt(yTW);
		
		String aRT = txtAnnualReturnWorking.getText();
		double annualReturnWorking = Double.parseDouble(aRT);
		
		String yR = txtYearsRetired.getText();
		int yearsRetired = Integer.parseInt(yR);
		
		String aRR = txtAnnualReturnRetired.getText();
		double annualReturnRetired = Double.parseDouble(aRR);
		
		String rI = txtRequiredIncome.getText();
		int requiredIncome = Integer.parseInt(rI);
		
		String mSSI = txtMonthlySSI.getText();
		int monthlySSI = Integer.parseInt(mSSI);
		
		Retirement retirement1 = new Retirement(yearsToWork, annualReturnWorking, yearsRetired, annualReturnRetired, requiredIncome, monthlySSI);
		
		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		
		txtWhatYouNeedToSave.setText(" " + retirement1.TotalAmountToSave());
		txtSaveEachMonth.setText(" " + retirement1.MonthlySavings());
		
		
		

		
	}
}

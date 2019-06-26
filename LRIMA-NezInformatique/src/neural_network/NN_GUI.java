package neural_network;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JCheckBox;
import java.awt.Choice;
import javax.swing.JMenuItem;

public class NN_GUI {

	private JFrame frmNeuralNetwork;
	private JLabel lblDataset;
	private JTextField textField_InputNb;
	private JLabel lblNumberOfInputs;
	private JButton btnAddHiddenLayer;
	private JList list_HiddenLayers;
	private JLabel lblHiddenLayers;
	private JTextField lbl_hiddenNeuronCount;
	private JLabel lblNumberOfNeurons;
	private JLabel lblInput;
	private JLabel lblOutput;
	private JLabel lblNumberOfOutputs;
	private JTextField textField_Outputs;
	private JList list_Dataset;
	private JTextField txtData;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JButton btnLoadDataset;
	private JButton btnTrain;
	private JButton btnTest;
	private JPanel panelTrain;
	private JLabel lblTrain;
	private JLabel lblEpochs;
	private JLabel lblLearningRate;
	private JLabel lblWeights;
	private JLabel lblDatasetPercent;
	private JFormattedTextField formattedTextField_Epochs;
	private JFormattedTextField formattedTextField_LR;
	private JFormattedTextField frmtdtxtfldMin;
	private JFormattedTextField frmtdtxtfldMax;
	private JLabel label;
	private JFormattedTextField formattedTextField_Percent;
	private JLabel label_percent;
	private JLabel lblAccuracyTitle;
	private JLabel label_accuracy;
	private JLabel lblPastAccuracies;
	private JList list_pastAccuracies;
	private JCheckBox chckbxTrain;
	private JCheckBox chckbxTest;
	private JPanel panel_Test;
	private JLabel lblTest;
	private JTextField textField;
	private JLabel lblGoal;
	private JLabel label_1;
	private JCheckBox chckbxVariation;
	private Choice choice_Variation;
	private JTextField txtStart;
	private JTextField txtEnd;
	private JTextField txtIncrement;
	private JLabel lblVariation;
	private JMenuItem mntmPastAccuracies;
	private JButton btnUpdateNetwork;
	private JButton btnChangeActivationFunction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NN_GUI window = new NN_GUI();
					window.frmNeuralNetwork.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NN_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNeuralNetwork = new JFrame();
		frmNeuralNetwork.setTitle("Neural Network");
		frmNeuralNetwork.setBounds(100, 100, 661, 486);
		frmNeuralNetwork.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNeuralNetwork.getContentPane().setLayout(null);
		
		panelTrain = new JPanel();
		panelTrain.setBounds(207, 259, 248, 177);
		frmNeuralNetwork.getContentPane().add(panelTrain);
		panelTrain.setLayout(null);
		
		lblTrain = new JLabel("Train");
		lblTrain.setBounds(102, 5, 43, 20);
		lblTrain.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTrain.add(lblTrain);
		
		lblEpochs = new JLabel("Epochs");
		lblEpochs.setBounds(19, 42, 46, 14);
		panelTrain.add(lblEpochs);
		
		lblLearningRate = new JLabel("Learning rate");
		lblLearningRate.setBounds(10, 98, 99, 14);
		panelTrain.add(lblLearningRate);
		
		lblWeights = new JLabel("Weights starting at");
		lblWeights.setBounds(129, 36, 109, 14);
		panelTrain.add(lblWeights);
		
		lblDatasetPercent = new JLabel("% of dataset to use");
		lblDatasetPercent.setBounds(139, 98, 109, 14);
		panelTrain.add(lblDatasetPercent);
		
		formattedTextField_Epochs = new JFormattedTextField();
		formattedTextField_Epochs.setHorizontalAlignment(SwingConstants.CENTER);
		formattedTextField_Epochs.setText("5");
		formattedTextField_Epochs.setBounds(19, 67, 36, 20);
		panelTrain.add(formattedTextField_Epochs);
		
		formattedTextField_LR = new JFormattedTextField();
		formattedTextField_LR.setText("0.05");
		formattedTextField_LR.setHorizontalAlignment(SwingConstants.CENTER);
		formattedTextField_LR.setBounds(20, 123, 36, 20);
		panelTrain.add(formattedTextField_LR);
		
		frmtdtxtfldMin = new JFormattedTextField();
		frmtdtxtfldMin.setToolTipText("Minimum");
		frmtdtxtfldMin.setText("Min");
		frmtdtxtfldMin.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldMin.setBounds(139, 67, 36, 20);
		panelTrain.add(frmtdtxtfldMin);
		
		frmtdtxtfldMax = new JFormattedTextField();
		frmtdtxtfldMax.setToolTipText("Maximum");
		frmtdtxtfldMax.setText("Max");
		frmtdtxtfldMax.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldMax.setBounds(185, 67, 36, 20);
		panelTrain.add(frmtdtxtfldMax);
		
		label = new JLabel("-");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(156, 70, 46, 14);
		panelTrain.add(label);
		
		formattedTextField_Percent = new JFormattedTextField();
		formattedTextField_Percent.setHorizontalAlignment(SwingConstants.CENTER);
		formattedTextField_Percent.setToolTipText("Number in percentage");
		formattedTextField_Percent.setText("100");
		formattedTextField_Percent.setBounds(169, 123, 36, 20);
		panelTrain.add(formattedTextField_Percent);
		
		label_percent = new JLabel("%");
		label_percent.setBounds(208, 126, 18, 14);
		panelTrain.add(label_percent);
		
		btnChangeActivationFunction = new JButton("Change Activation Function");
		btnChangeActivationFunction.setBounds(50, 154, 164, 23);
		panelTrain.add(btnChangeActivationFunction);
		
		lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDataset.setBounds(43, 39, 53, 17);
		frmNeuralNetwork.getContentPane().add(lblDataset);
		
		textField_InputNb = new JTextField();
		textField_InputNb.setHorizontalAlignment(SwingConstants.CENTER);
		textField_InputNb.setText("1");
		textField_InputNb.setBounds(152, 93, 86, 20);
		frmNeuralNetwork.getContentPane().add(textField_InputNb);
		textField_InputNb.setColumns(10);
		
		lblNumberOfInputs = new JLabel("Number of inputs");
		lblNumberOfInputs.setBounds(152, 66, 110, 14);
		frmNeuralNetwork.getContentPane().add(lblNumberOfInputs);
		
		btnAddHiddenLayer = new JButton("Add Hidden Layer");
		btnAddHiddenLayer.setBounds(260, 109, 150, 23);
		frmNeuralNetwork.getContentPane().add(btnAddHiddenLayer);
		
		list_HiddenLayers = new JList();
		list_HiddenLayers.setBounds(260, 143, 150, 65);
		frmNeuralNetwork.getContentPane().add(list_HiddenLayers);
		
		lblHiddenLayers = new JLabel("Hidden Layers");
		lblHiddenLayers.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHiddenLayers.setHorizontalAlignment(SwingConstants.CENTER);
		lblHiddenLayers.setBounds(294, 11, 86, 14);
		frmNeuralNetwork.getContentPane().add(lblHiddenLayers);
		
		lbl_hiddenNeuronCount = new JTextField();
		lbl_hiddenNeuronCount.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hiddenNeuronCount.setText("1");
		lbl_hiddenNeuronCount.setBounds(294, 63, 86, 20);
		frmNeuralNetwork.getContentPane().add(lbl_hiddenNeuronCount);
		lbl_hiddenNeuronCount.setColumns(10);
		
		lblNumberOfNeurons = new JLabel("Number of Neurons in Layer");
		lblNumberOfNeurons.setBounds(276, 38, 179, 23);
		frmNeuralNetwork.getContentPane().add(lblNumberOfNeurons);
		
		lblInput = new JLabel("Input");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInput.setBounds(171, 41, 46, 14);
		frmNeuralNetwork.getContentPane().add(lblInput);
		
		lblOutput = new JLabel("Output");
		lblOutput.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOutput.setBounds(489, 41, 46, 14);
		frmNeuralNetwork.getContentPane().add(lblOutput);
		
		lblNumberOfOutputs = new JLabel("Number of outputs");
		lblNumberOfOutputs.setBounds(465, 66, 115, 14);
		frmNeuralNetwork.getContentPane().add(lblNumberOfOutputs);
		
		textField_Outputs = new JTextField();
		textField_Outputs.setText("1");
		textField_Outputs.setHorizontalAlignment(SwingConstants.CENTER);
		textField_Outputs.setColumns(10);
		textField_Outputs.setBounds(465, 93, 86, 20);
		frmNeuralNetwork.getContentPane().add(textField_Outputs);
		
		list_Dataset = new JList();
		list_Dataset.setBounds(21, 65, 110, 77);
		frmNeuralNetwork.getContentPane().add(list_Dataset);
		
		txtData = new JTextField();
		txtData.setText("Data/");
		txtData.setBounds(31, 159, 86, 20);
		frmNeuralNetwork.getContentPane().add(txtData);
		txtData.setColumns(10);
		
		separator = new JSeparator();
		separator.setBounds(23, 246, 599, 2);
		frmNeuralNetwork.getContentPane().add(separator);
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(195, 246, 7, 190);
		frmNeuralNetwork.getContentPane().add(separator_1);
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(465, 246, 1, 190);
		frmNeuralNetwork.getContentPane().add(separator_2);
		
		btnLoadDataset = new JButton("Load Dataset");
		btnLoadDataset.setBounds(21, 190, 110, 23);
		frmNeuralNetwork.getContentPane().add(btnLoadDataset);
		
		btnTrain = new JButton("Train");
		btnTrain.setBounds(52, 294, 89, 23);
		frmNeuralNetwork.getContentPane().add(btnTrain);
		
		btnTest = new JButton("Test");
		btnTest.setBounds(52, 377, 89, 23);
		frmNeuralNetwork.getContentPane().add(btnTest);
		
		lblAccuracyTitle = new JLabel("Accuracy");
		lblAccuracyTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAccuracyTitle.setBounds(519, 266, 61, 17);
		frmNeuralNetwork.getContentPane().add(lblAccuracyTitle);
		
		label_accuracy = new JLabel("0%");
		label_accuracy.setHorizontalAlignment(SwingConstants.CENTER);
		label_accuracy.setBounds(519, 298, 46, 14);
		frmNeuralNetwork.getContentPane().add(label_accuracy);
		
		lblPastAccuracies = new JLabel("Past Accuracies");
		lblPastAccuracies.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPastAccuracies.setBounds(502, 323, 103, 17);
		frmNeuralNetwork.getContentPane().add(lblPastAccuracies);
		
		list_pastAccuracies = new JList();
		list_pastAccuracies.setBounds(489, 348, 128, 77);
		frmNeuralNetwork.getContentPane().add(list_pastAccuracies);
		
		chckbxTrain = new JCheckBox("Train");
		chckbxTrain.setSelected(true);
		chckbxTrain.setBounds(141, 294, 61, 23);
		frmNeuralNetwork.getContentPane().add(chckbxTrain);
		
		chckbxTest = new JCheckBox("Test");
		chckbxTest.setBounds(141, 377, 61, 23);
		frmNeuralNetwork.getContentPane().add(chckbxTest);
		
		panel_Test = new JPanel();
		panel_Test.setBounds(207, 259, 248, 177);
		frmNeuralNetwork.getContentPane().add(panel_Test);
		panel_Test.setLayout(null);
		
		lblTest = new JLabel("Test");
		lblTest.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTest.setBounds(101, 11, 43, 20);
		panel_Test.add(lblTest);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("95");
		textField.setBounds(31, 46, 32, 20);
		panel_Test.add(textField);
		textField.setColumns(10);
		
		lblGoal = new JLabel("Goal");
		lblGoal.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGoal.setHorizontalAlignment(SwingConstants.CENTER);
		lblGoal.setBounds(21, 27, 46, 14);
		panel_Test.add(lblGoal);
		
		label_1 = new JLabel("%");
		label_1.setBounds(65, 49, 19, 14);
		panel_Test.add(label_1);
		
		chckbxVariation = new JCheckBox("Variate");
		chckbxVariation.setBounds(21, 101, 97, 23);
		panel_Test.add(chckbxVariation);
		
		choice_Variation = new Choice();
		choice_Variation.setEnabled(false);
		choice_Variation.setBounds(123, 46, 103, 20);
		panel_Test.add(choice_Variation);
		
		txtStart = new JTextField();
		txtStart.setToolTipText("Start number");
		txtStart.setHorizontalAlignment(SwingConstants.CENTER);
		txtStart.setText("Start");
		txtStart.setBounds(124, 102, 37, 20);
		panel_Test.add(txtStart);
		txtStart.setColumns(10);
		
		txtEnd = new JTextField();
		txtEnd.setToolTipText("End at number");
		txtEnd.setHorizontalAlignment(SwingConstants.CENTER);
		txtEnd.setText("End");
		txtEnd.setColumns(10);
		txtEnd.setBounds(189, 102, 37, 20);
		panel_Test.add(txtEnd);
		
		txtIncrement = new JTextField();
		txtIncrement.setHorizontalAlignment(SwingConstants.CENTER);
		txtIncrement.setToolTipText("Increment");
		txtIncrement.setText("Increment");
		txtIncrement.setBounds(148, 133, 59, 20);
		panel_Test.add(txtIncrement);
		txtIncrement.setColumns(10);
		
		lblVariation = new JLabel("Variation");
		lblVariation.setHorizontalAlignment(SwingConstants.CENTER);
		lblVariation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVariation.setBounds(133, 77, 76, 14);
		panel_Test.add(lblVariation);
		
		mntmPastAccuracies = new JMenuItem("Past Accuracies");
		mntmPastAccuracies.setBounds(489, 348, 129, 22);
		frmNeuralNetwork.getContentPane().add(mntmPastAccuracies);
		
		btnUpdateNetwork = new JButton("Update Network");
		btnUpdateNetwork.setBounds(465, 174, 129, 34);
		frmNeuralNetwork.getContentPane().add(btnUpdateNetwork);
	}
}

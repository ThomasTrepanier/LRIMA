package deeplearning4j;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.tuple.Pair;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import pictureUtils.PictureReader;

import javax.swing.SpringLayout;
import javax.swing.JMenuBar;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class IFMAP extends JFrame {
	
	private static final File DEFAULT_IMAGECHOOSER_PATH = new File("Data");
	private static MultiLayerNetwork recognitionModel;
	private static MultiLayerNetwork evaluationModel;
	private final JFileChooser fileChooser;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JButton btnChoosePicture;
	private JLabel lblFileChosen;
	private JButton btnEvaluateAge;
	private JLabel lblResult;

	private BufferedImage imageToEvaluate = null;
	private ImageViewer imageViewer;
	private JButton btnRecognizeFruit;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IFMAP frame = new IFMAP();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		recognitionModel = ModelUtils.loadModel(ModelJob.Regonize, "", true);
		System.out.println(recognitionModel);
	}

	/**
	 * Create the frame.
	 */
	public IFMAP() {
		
		fileChooser = new JFileChooser(DEFAULT_IMAGECHOOSER_PATH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnChoosePicture = new JButton("Choose picture");
		btnChoosePicture.setBounds(30, 54, 132, 29);
		btnChoosePicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageToEvaluate = choosePicture();
			}
		});
		contentPane.setLayout(null);
		btnChoosePicture.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(btnChoosePicture);
		
		lblFileChosen = new JLabel("file chosen");
		lblFileChosen.setBounds(20, 20, 267, 15);
		lblFileChosen.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(lblFileChosen);
		
		btnEvaluateAge = new JButton("Evaluate age");
		btnEvaluateAge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEvaluateAge.setBounds(266, 141, 144, 37);
		btnEvaluateAge.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(btnEvaluateAge);
		
		lblResult = new JLabel("Result:");
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setBounds(29, 208, 380, 20);
		lblResult.setFont(new Font("Verdana", Font.BOLD, 15));
		contentPane.add(lblResult);
		
		imageViewer = new ImageViewer();
		imageViewer.setBounds(310, 15, 100, 100);
		imageViewer.initialize(null, imageViewer.getBounds());
		contentPane.add(imageViewer);
		
		btnRecognizeFruit = new JButton("Recognize fruit");
		btnRecognizeFruit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recognizeFruit(imageToEvaluate);
			}
		});
		btnRecognizeFruit.setBounds(30, 142, 144, 37);
		contentPane.add(btnRecognizeFruit);
	}
	
	private BufferedImage choosePicture() {
		BufferedImage img = null;
		int choice = fileChooser.showOpenDialog(null);
		System.out.println(choice);
		
		if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
            if(file == null)
            	return null;
            
            System.out.println("Opening: " + file.getName() + ".");
            try {
				img = ImageIO.read(file);
				imageViewer.setImage(img);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            lblFileChosen.setText(file.getPath());
        } else {
            System.out.println("Open command cancelled by user.");
        }
		return img;
	}
	
	private String recognizeFruit(BufferedImage img) {
		if(img == null || recognitionModel == null)
			return "";
		
		INDArray output = passImage(img, recognitionModel);
		System.out.println("Output: " + output);
		String fruit = convertToFruit(output, recognitionModel);
		
		System.out.println(fruit);
		lblResult.setText(fruit);
		return fruit;
	}
	
	private INDArray passImage(BufferedImage img, MultiLayerNetwork model) {
		INDArray output = null;
		 // Main background thread, this will load the model and test the input image
	    // The dimensions of the images are set here
            int height = img.getHeight();
            int width = img.getWidth();
            int channels = 3;

            //Now we load the model from the raw folder with a try / catch block
            try {
                //Use the nativeImageLoader to convert to numerical matrix
                NativeImageLoader loader = new NativeImageLoader(height, width, channels);

                //put image into INDArray
                INDArray image = loader.asMatrix(img);

                //values need to be scaled
                DataNormalization scalar = new ImagePreProcessingScaler(0, 1);

                //then call that scalar on the image dataset
                scalar.transform(image);

                //pass through neural net and store it in output array
                output = model.output(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
	}

	private String convertToFruit(INDArray output, MultiLayerNetwork model) {
		Pair<Integer, Double> result = getResultId(output.getRow(0));
		int resultId = result.getLeft();
		double resultAccuracy = result.getRight();
		
		String fruitName = getFruitInFilesFromId(resultId);
		return fruitName + " with " + round(resultAccuracy * 100) + "% certainty";
	}
	
	private Pair<Integer, Double> getResultId(INDArray outputs) {
		Pair<Integer, Double> result;
		int id = 0;
		double value = outputs.getDouble(id);
		
		for(int i = 0; i < outputs.length(); i++) {
			if(outputs.getDouble(i) > value) {
				id = i;
				value = outputs.getDouble(i);
			}
		}
		result = Pair.of(id, value);
		return result;
	}
	
	//TODO: Read from file in zip
	private String getFruitInFilesFromId(int id) {
		File labelsFile = new File("Data\\Fruits\\Fruit_Training");
		return labelsFile.listFiles()[id].getName();
	}
	
	private double round(double d) {
		return Math.round(d * 100) / 100;
	}
}

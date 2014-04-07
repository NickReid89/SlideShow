
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

public final class SlideShow extends JFrame implements ActionListener {

    //Keeping track of which slide the program is on.
    private int slideIndex = 0;
    // Store path files.
    File files[];
    //Timer for delay
    Timer timer;
    //Images being held in this panel
    private final JPanel imagePanel = new JPanel();
    // all other buttons.
    private final JPanel buttonPanel = new JPanel();
    //This is the picture.
    private JLabel lblPicture = new JLabel();

    private final JButton btnAdd = new JButton("Add Picture(s)");
    private final JButton btnStartShow = new JButton("Start Slideshow");
    private final JButton btnClear = new JButton("Clear");
    private final JLabel lblDelay = new JLabel("Delay");
    private final JButton btnStop = new JButton("Pause");
    // Formatted text field to only accept ints, explained below.
    private JFormattedTextField txtDelay = new JFormattedTextField(1);

    SlideShow() {
        setTitle("SlideShow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        SetUpPanels();
        setResizable(false);
        btnAdd.addActionListener(this);
        btnStartShow.addActionListener(this);
        btnStop.addActionListener(this);
        btnClear.addActionListener(this);
        pack();
        setVisible(true);
    }

    public void SetUpTextField() {
        // So this sets up the formatting for the formatted text field. Rather than call exceptions. I felt it'd be better to not allow
        // the user to enter letters or doubles. So I used the number format class to customize my formatted text field to only allow ints.
        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0l);

        txtDelay = new JFormattedTextField(numberFormatter);
    }

    public void SetUpPanels() {
        imagePanel.add(lblPicture);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnStartShow);
        buttonPanel.add(lblDelay);
        buttonPanel.add(txtDelay);
        buttonPanel.add(btnStop);
        buttonPanel.add(btnClear);
        add(imagePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAdd) {
            //clear slide
            lblPicture.setIcon(null);
            // Create a file chooser.
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);

            // Create a filter. Might be nice for a user so they won't have to search through files in a folder.
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
            // set filter.
            fc.setFileFilter(filter);
            //Rather than create an integer and compare in an if statement I decided it would be better to call the open dialog and 
            // based on what it returns to compare that to JFileChoosers approve option.
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                // initalize the array to the files.
                files = fc.getSelectedFiles();
            }// If the start slide show button is pressed.
        } else if (e.getSource() == btnStartShow) {
            // set the slide to the start.
            if (files == null) {
                slideIndex = 0;
            } else if(slideIndex -1 == -1) {
                slideIndex = files.length;
            }

            // Call the timer to cause the delay desired.
            timer = new Timer((Integer.parseInt(txtDelay.getText()) * 1000), null);
            timer.addActionListener(this);
            timer.start();
            // if the timer is activated.
        } else if (e.getSource() == timer) {
            // If there are files
            if (files == null) {

            } else if (slideIndex - 1 == -1) {
                slideIndex = files.length;
            } else {
                ImageIcon ii = new ImageIcon(files[slideIndex - 1].getAbsolutePath());
                Image img = ii.getImage();
                Image newimg = img.getScaledInstance(400, 500, java.awt.Image.SCALE_SMOOTH);
                ii = new ImageIcon(newimg);
                lblPicture.setIcon(ii);

                slideIndex--;
                pack();
            }

            // pause the image.
        } else if (e.getSource() == btnStop) {
            timer.stop();
        } else if (e.getSource() == btnClear) {
            lblPicture.setIcon(null);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SlideShow slideShow = new SlideShow();
    }
}

package reconocimiento;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.text.Highlighter;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;


class DetectarRostro{
    CascadeClassifier faceDetector = new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
    MatOfRect faceDetections = new MatOfRect();
    VideoCapture cap = new VideoCapture();
    Mat imagen = new Mat();
    
    public void run(){
        cap.open(0);
        System.out.println("\nDeteccion de Rostros con OpenCV y Webcam en java");
        //formMain ventana = new formMain();
        //Ventana ventana = new Ventana();
        
        if(cap.isOpened()){
            while(true){
                try{
                    //Thread.sleep(100);
                    cap.read(imagen);
                    if(!imagen.empty()){
                        faceDetector.detectMultiScale(imagen, faceDetections);
                        for(Rect rect : faceDetections.toArray()){
                            Imgproc.rectangle(imagen, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0, 255, 0));
                            Mat region = new Mat(imagen,rect);
                            Imgcodecs.imwrite("Captura1.jpg", region);
                        }
                        //ventana.setImage(convertir(imagen));
                        //ventana.setVisible(true);
                    }
                }catch(Exception ex){
                    Logger.getLogger(DetectarRostro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private Image convertir(Mat imagen){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", imagen, matOfByte);
        
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        
        try{
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        }catch(Exception e){
            e.printStackTrace();
        }
        return (Image) bufImage;
    }
}

public class Main {
    
    //static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);};
    
    public static void main(String[] args){
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try{            
            ReconocimientoCaras reconocer = ReconocimientoCaras.getInstance();
            
            //Entrenamiento
            IplImage[] trainImages = new IplImage[10];
            for(int i=1; i<=5;i++){
                trainImages[i-1]=cvLoadImage("Elvin_"+i+".jpg");
                CvSeq faces = reconocer.detectFace(trainImages[i-1]);
                CvRect r = new CvRect(cvGetSeqElem(faces, 0));
                trainImages[i-1] = reconocer.preprocessImage(trainImages[i-1], r);
            }
            reconocer.learnNewFace("Elvin", trainImages);
        }catch(Exception ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //new DetectarRostro().run();
        
        //formMain ventana = new formMain();
        //ventana.setVisible(true);
        
        //Mat m = Mat.eye(3, 3, CvType.CV_8UC1);
        //System.out.println("m= \n"+ m.dump());        
    }
}
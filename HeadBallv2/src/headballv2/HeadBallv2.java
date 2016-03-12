package headballv2;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Tiax
 */
public class HeadBallv2 extends JFrame{

    RenderComponent Renderer;
    private String VERSION = "_alpha 1.0";
    private String TITLE = "Heads";
    
    public HeadBallv2(){
        initGUI();
    }
    
    public void initGUI(){
        Renderer = new RenderComponent();
        add(Renderer);
        addKeyListener(Renderer.getMotionComponent());
        setFocusable(true);
        setResizable(true);
        pack();
        
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle(TITLE + VERSION);    
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
    }
    
    public static void main(String[] args) {
        System.out.println("INIT SCR_DIMENSION: ");
        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame ex = new HeadBallv2();
                ex.setVisible(true);                
            }
        });
    }
    
    

}

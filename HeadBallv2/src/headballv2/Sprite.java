
package headballv2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 @author Tiax
*/
public class Sprite{
    
    private final int DEF_SPRITE_HEIGHT = 100;
    private final int DEF_SPRITE_WIDTH = 94;
    private String SOURCE;

    BufferedImage Sprite;
    BufferedImage Tile;
    
    public Sprite(String Source) {
        this.SOURCE = Source;
        try {
            this.Sprite = ImageIO.read(new File("res/" + this.SOURCE));
        } catch (IOException ex) {
            System.out.println("Errore nel caricamento res: " + this.SOURCE);
        }
    }
    
    public Sprite(String Source, int Index){
        this.SOURCE = Source;
         try {
            this.Tile = ImageIO.read(new File("res/" + this.SOURCE));
            this.Sprite = this.Tile.getSubimage(DEF_SPRITE_WIDTH * Index, 0, DEF_SPRITE_WIDTH, DEF_SPRITE_HEIGHT);
        } catch (IOException ex) {
            System.out.println("Errore nel caricamento res: " + this.SOURCE);
        }
    }
    
    public BufferedImage getSprite(){
        return this.Sprite;
    }
    
    public void setSprite(int i){
        this.Sprite = this.Tile.getSubimage(DEF_SPRITE_WIDTH*i, 0, DEF_SPRITE_WIDTH, DEF_SPRITE_HEIGHT);
    }

    public String getSource() {
        return SOURCE;
    }
}

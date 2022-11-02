import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class Button extends Actor {
    interface OnClick {
        void onClick();
    }

    public String text;
    public Color fontColor;
    public Color backgroundColor;
    public OnClick onClick;
    private boolean mouseOver;

    public Button(String text, Color fontColor, Color backgroundColor, OnClick onClick) {
        this.text = text;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.onClick = onClick;
        init();
    }

    private void init() {
        GreenfootImage image = new GreenfootImage(text, 30, fontColor, backgroundColor);
        setImage(image);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            onClick.onClick();
        }
        if (Greenfoot.mouseMoved(this) && !mouseOver) {
            mouseOver = true;
            GreenfootImage image = new GreenfootImage(text, 30, backgroundColor, fontColor);
            setImage(image);
        } else if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this) && mouseOver){
            mouseOver = false;
            GreenfootImage image = new GreenfootImage(text, 30, fontColor, backgroundColor);
            setImage(image);
        }
    }
}

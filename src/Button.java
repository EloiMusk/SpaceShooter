import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * A button that can be clicked on.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Button extends Actor {
    /**
     * The interface for the click callback.
     */
    interface OnClick {
        void onClick();
    }

    /**
     * The text of the button.
     */
    public String text;
    /**
     * The color of the text.
     */
    public Color fontColor;
    /**
     * The color of the button.
     */
    public Color backgroundColor;
    /**
     * The callback for when the button is clicked.
     */
    public OnClick onClick;
    /**
     * Weather or not the mouse is hovering over the button.
     */
    private boolean mouseOver;

    /**
     * Constructor for the Button class. Sets the values of the button.
     *
     * @param text            The text of the button.
     * @param fontColor       The color of the text.
     * @param backgroundColor The color of the button.
     * @param onClick         The callback for when the button is clicked.
     */
    public Button(String text, Color fontColor, Color backgroundColor, OnClick onClick) {
        this.text = text;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.onClick = onClick;
        init();
    }

    /**
     * Initializes the button and sets the image and text.
     */
    private void init() {
        GreenfootImage image = new GreenfootImage(text, 30, fontColor, backgroundColor);
        setImage(image);
    }

    public void act() {
//        Trigger the callback when the button is clicked.
        if (Greenfoot.mouseClicked(this)) {
            onClick.onClick();
        }
//        Check if the mouse is hovering over the button and set the image accordingly.
        if (Greenfoot.mouseMoved(this) && !mouseOver) {
            mouseOver = true;
            GreenfootImage image = new GreenfootImage(text, 30, backgroundColor, fontColor);
            setImage(image);
        } else if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this) && mouseOver) {
            mouseOver = false;
            GreenfootImage image = new GreenfootImage(text, 30, fontColor, backgroundColor);
            setImage(image);
        }
    }
}

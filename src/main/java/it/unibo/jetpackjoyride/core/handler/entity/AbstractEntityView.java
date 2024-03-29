package it.unibo.jetpackjoyride.core.handler.entity;

import it.unibo.jetpackjoyride.core.entities.entity.api.Entity;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The {@link AbstractEntityView} class implements all the methods of
 * {@link EntityView} and provides a standard code implementation for
 * the scaling of an image based on the screen sizes.
 * 
 * @author gabriel.stira@studio.unibo.it
 */
public abstract class AbstractEntityView implements EntityView {
    /**
     * Defines the node used for painting the image loaded.
     */
    private final ImageView imageView;
    /**
     * Defines the list of all images.
     */
    private final List<Image> images;
    /**
     * Defines the current image loaded in the imageView.
     */
    private int animationFrame;
    /**
     * Defines the widht of the image.
     */
    private Double width;
    /**
     * Defines the height of the image.
     */
    private Double height;


    /**
     * Constructs an AbstractEntityView with the provided images.
     * By default, width and height of the imageView are set to 0.
     *
     * @param images The type of the entity.
     */
    public AbstractEntityView(final List<Image> images) {
        this.images = new ArrayList<>(images);
        this.imageView = new ImageView();
        this.animationFrame = 0;
        this.width = 0.0;
        this.height = 0.0;
    }

    /**
     * Defines a method used by all entity views to carry out an operation of scaling
     * based on the current screen size. The animateFrames method is used first to 
     * find the image corresponding to the entity and then the imageView is modified
     * using scaled position, dimensions and rotation.
     * 
     * @param entity The entity whose imageView is associated.
     */
    @Override
    public void updateView(final Entity entity) {
        this.animateFrames(entity);

        final GameInfo infoResolution = GameInfo.getInstance();

        final double scaleX = infoResolution.getScreenWidth() / infoResolution.getDefaultWidth();
        final double scaleY = infoResolution.getScreenHeight() / infoResolution.getDefaultHeight();

        imageView.setX((entity.getEntityMovement().getPosition().get1() - width / 2) * scaleX);
        imageView.setY((entity.getEntityMovement().getPosition().get2() - height / 2) * scaleY);
        imageView.setRotate(entity.getEntityMovement().getRotation().get1());

        final double width = this.width * scaleX;
        final double height = this.height * scaleY;

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        imageView.setImage(this.images.get(this.animationFrame));
    }

    /**
     * Defines the method used to find the correct animation frame of the entity.
     * @param entity The entity whose ImageView has to be computed.
     */
    protected abstract void animateFrames(Entity entity);

    @Override
    public final ImageView getImageView() {
        return Collections.nCopies(1, this.imageView).get(0);
    }

    /**
     * Sets a new animation frame.
     * @param newAnimationFrame The new animation frame.
     */
    public final void setAnimationFrame(final Integer newAnimationFrame) {
        this.animationFrame = newAnimationFrame;
    }

    /**
     * Gets the current animation frame.
     * @return The current animation frame.
     */
    public final Integer getAnimationFrame() {
        return this.animationFrame;
    }

    /**
     * Set the new width of the imageView.
     * @param newWidth The new width.
     */
    public final void setWidht(final Double newWidth) {
        this.width = newWidth;
    }

    /**
     * Set the new height of the imageView.
     * @param newHeight The new height.
     */
    public final void setHeight(final Double newHeight) {
        this.height = newHeight;
    }

    /**
     * Gets the width of the imageView.
     * @return The width of the imageView.
     */
    public final Double getWidht() {
        return this.width;
    }

    /**
     * Gets the height of the imageView.
     * @return The height of the imageView.
     */
    public final Double getHeight() {
        return this.height;
    }

}

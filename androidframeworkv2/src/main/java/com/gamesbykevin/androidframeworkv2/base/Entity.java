package com.gamesbykevin.androidframeworkv2.base;

import com.gamesbykevin.androidframeworkv2.common.ICommon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/31/2017.
 */

public abstract class Entity extends Cell implements ICommon {

    //each entity will have an (x,y) coordinate
    private float x, y;

    //each entity will have a width and height
    private float w, h;

    //each entity will have a velocity
    private double dx, dy;

    //complete transparency
    private static final float TRANSPARENCY_FULL = 1.0f;

    //the level of transparency when we render
    private float transparency = TRANSPARENCY_FULL;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

    private float[] vertices = {
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

    private float[] textures = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    //texture id so we know what texture to render
    private int textureId;

    //current facing angle of the entity
    private float angle = 0.0f;


    /**
     * Default constructor
     */
    public Entity() {

        //call default parent constructor
        super();

        //create our vertices buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //create our texture buffer
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        textureBuffer.put(textures);
        textureBuffer.position(0);
    }

    /**
     * Set the transparency of this entity
     * @param transparency 1.0 fully opaque, 0.0 invisible
     */
    public void setTransparency(final float transparency) {
        this.transparency = transparency;
    }

    public float getTransparency() {
        return this.transparency;
    }

    /**
     * Assign the facing angle
     * @param angle The facing angle in degrees
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     * Get the facing angle
     * @return The facing angle in degrees
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     * Assign the texture id
     * @param textureId The unique id of the texture we want to render
     */
    public void setTextureId(final int textureId) {
        this.textureId = textureId;
    }

    /**
     * Get the texture id
     * @return The unique id of the texture we want to render
     */
    public int getTextureId() {
        return this.textureId;
    }

    /**
     * Get the distance
     * @param entity The entity we want to compare
     * @return The distance between the current and specified entities
     */
    public double getDistance(final Entity entity)
    {
        return getDistance(entity.getX(), entity.getY());
    }

    /**
     * Get the distance
     * @param x x-coordinate
     * @param y y-coordinate
     * @return The distance between the entity and specified (x,y)
     */
    public double getDistance(final double x, final double y)
    {
        return getDistance(x, y, getX(), getY());
    }

    /**
     * Get the distance
     * @param x1 x-coordinate
     * @param y1 y-coordinate
     * @param x2 x-coordinate
     * @param y2 y-coordinate
     * @return The distance between the 2 specified (x,y) coordinates
     */
    public static double getDistance(final double x1, final double y1, final double x2, final double y2)
    {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Get the x-velocity
     * @return the assigned x-velocity
     */
    public double getDX()
    {
        return this.dx;
    }

    /**
     * Get the y-velocity
     * @return the assigned y-velocity
     */
    public double getDY()
    {
        return this.dy;
    }

    /**
     * Assign the x-velocity
     * @param dx The desired x-velocity
     */
    public void setDX(final double dx)
    {
        this.dx = dx;
    }

    /**
     * Assign the y-velocity
     * @param dy The desired y-velocity
     */
    public void setDY(final double dy)
    {
        this.dy = dy;
    }

    /**
     * Assign the x-coordinate
     * @param entity The entity containing the desired x-coordinate
     */
    public void setX(final Entity entity)
    {
        setX(entity.getX());
    }

    /**
     * Assign the x-coordinate
     * @param x the desired x-coordinate
     */
    public void setX(final float x)
    {
        this.x = x;
    }

    /**
     * Assign the y-coordinate
     * @param entity The entity containing the desired y-coordinate
     */
    public void setY(final Entity entity)
    {
        setY(entity.getY());
    }

    /**
     * Assign the y-coordinate
     * @param y the desired y-coordinate
     */
    public void setY(final float y)
    {
        this.y = y;
    }

    /**
     * Get the x-coordinate
     * @return the x-coordinate
     */
    public float getX()
    {
        return this.x;
    }

    /**
     * Get the y-coordinate
     * @return the y-coordinate
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Get the width
     * @return get the width
     */
    public float getWidth()
    {
        return this.w;
    }

    /**
     * Get the height
     * @return get the height
     */
    public float getHeight()
    {
        return this.h;
    }

    /**
     * Assign the width
     * @param entity The object containing the width
     */
    public void setWidth(final Entity entity)
    {
        setWidth(entity.getWidth());
    }

    /**
     * Assign the width
     * @param w The desired width
     */
    public void setWidth(final float w)
    {
        this.w = w;
    }

    /**
     * Assign the height
     * @param entity The object containing the height
     */
    public void setHeight(final Entity entity)
    {
        setHeight(entity.getHeight());
    }

    /**
     * Assign the height
     * @param h The desired height
     */
    public void setHeight(final float h)
    {
        this.h = h;
    }

    /**
     * Render the object
     * @param openGL Our openGL context used for rendering
     */
    public void render(GL10 openGL) {

        //use for quick transformations so it will only apply to this object
        openGL.glPushMatrix();

        //assign the correct transparency if it isn't 100%
        if (getTransparency() != TRANSPARENCY_FULL)
            openGL.glColor4f(1.0f, 1.0f, 1.0f, getTransparency());

        //if there is an angle we rotate
        if (getAngle() != 0.0f) {

            //3. now move it back to complete the operation
            openGL.glTranslatef(getX() + (getWidth() / 2), getY() + (getHeight() / 2), 0.0f);

            //2. now rotate the angle
            openGL.glRotatef(getAngle(), 0.0f, 0.0f, 1.0f);

            //1. reset to origin as this open gl operation is done first (open gl operations done in reverse)
            openGL.glTranslatef( -(getWidth() / 2), -(getHeight() / 2), 0.0f);

        } else {

            //assign render coordinates
            openGL.glTranslatef(getX(), getY(), 0.0f);
        }

        //assign dimensions
        openGL.glScalef(getWidth(), getHeight(), 0.0f);

        //enable texture rendering
        openGL.glEnable(GL10.GL_TEXTURE_2D);

        //assign texture we want to use
        openGL.glBindTexture(GL10.GL_TEXTURE_2D, getTextureId());

        //enable client state for our render
        openGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //provide our array of vertex coordinates
        openGL.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        //coordinates on texture we want to render
        openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        //render our texture based on the texture and vertex coordinates
        openGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //restore the transparency back if it isn't already 100%
        if (getTransparency() != TRANSPARENCY_FULL)
            openGL.glColor4f(1.0f, 1.0f, 1.0f, TRANSPARENCY_FULL);

        //after rendering remove the transformation since we only needed it for this object
        openGL.glPopMatrix();
    }
}
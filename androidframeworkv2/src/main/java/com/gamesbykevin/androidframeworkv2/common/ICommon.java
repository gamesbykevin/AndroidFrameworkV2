package com.gamesbykevin.androidframeworkv2.common;

import com.gamesbykevin.androidframeworkv2.base.Disposable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/31/2017.
 */

public interface ICommon extends Disposable {

    /**
     * We need logic to render
     * @param openGL OpenGL context used for rendering
     */
    public void render(GL10 openGL);
}

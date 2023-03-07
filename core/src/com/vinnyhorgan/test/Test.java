package com.vinnyhorgan.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

public class Test extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private Shader shader;
    private Model model;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private ModelBatch modelBatch;

    @Override
    public void create () {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 8f, 8f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        for (int x = -5; x <= 5; x += 2) {
            for (int z = -5; z <= 5; z += 2) {
                ModelInstance instance = new ModelInstance(model);
                instance.transform.setToTranslation(x, 0, z);

                ColorAttribute attribute = ColorAttribute.createDiffuse((x + 5f) / 10f, (z + 5f) / 10f, 0, 1);
                instance.materials.get(0).set(attribute);

                instances.add(instance);
            }
        }

        shader = new CustomShader();
        shader.init();

        modelBatch = new ModelBatch();
    }

    @Override
    public void render () {
        cameraController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);

        for (ModelInstance instance : instances)
            modelBatch.render(instance, shader);

        modelBatch.end();
    }

    @Override
    public void dispose () {
        shader.dispose();
        model.dispose();
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}

package pro.dimmy.ball;


import android.util.Log;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static java.lang.Math.round;

/**
 * Created by ddemidovskiy on 11.10.2016.
 */

public class Physics {

    private static final int FPS = 30;
    private static final int SPEED_MULTIPLIER = 2;
    public static float GRAVITY_SCALE = 100.0f;

    // private final BodyDef boxDef;
    private World world;
    private Vec2 grav;
    private Body bBall;
    private Body bBorders;
    private Physics.Callbacks context;

    private BodyDef bdStatic;
    private BodyDef bdKinem;
    private BodyDef bdDyn;

    private FixtureDef fdBall;
    private FixtureDef fdWalls;



    public interface Callbacks
    {
        void onBallPositionChange(float x, float y);
    }



    public Physics(Physics.Callbacks context)
    {
        this.context = context;

        setBodyDefs();
        setFixtureDefs();


        // сотворение мира
        grav = new Vec2(0, 0);
        world = new World(grav);

        // да будет шар!
        bdDyn.position.set(10, 10);
        bBall = world.createBody(bdDyn);
        bBall.createFixture(fdBall);

        // да будут границы
        createWall(0, 0, 100, 1); // top
        createWall(100, 0, 1, 100); // right
        createWall(0, 100, 100, 1); // bottom
        createWall(0, 0, 1, 100); // left

    }



    private void createWall(float x, float y, float w, float h)
    {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(w, h);
        FixtureDef fd = fdWalls;
        fd.shape = ps;
        BodyDef bd = bdStatic;
        bd.position.set(x, y);
        Body b = world.createBody(bd);
        b.createFixture(fd);
    }




/*        figureParams.shape = new b2PolygonShape;
        figureParams.shape.SetAsBox( 145/SCALE, 3 / SCALE);//Длина и ширина
        bodyParams.position.Set(160/SCALE, 284 / SCALE);//Нижняя граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);//Свяжем физическую часть и геометрическую и добавим в мир

        bodyParams.position.Set(160 / SCALE , 8 /SCALE );//верхняя граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);
        figureParams.shape.SetAsBox(3 / SCALE, 141 / SCALE);//изменить габариты прямоугольника

        bodyParams.position.Set(12/ SCALE, 146 / SCALE);//левая граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);
        bodyParams.position.Set(308 / SCALE, 146/SCALE);//правая граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);*/






        /*boxDef = new BodyDef();
        boxDef.type = BodyType.DYNAMIC;
        boxDef.allowSleep = false;
        boxDef.position.set(0.0f, 4.0f);
        box = world.createBody(boxDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(1.0f, 1.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        box.createFixture(fixtureDef);*/







    private void setFixtureDefs()
    {
        // параметры фигур стенок
        fdWalls = new FixtureDef();
        fdWalls.density = 1.0f; // Плотность тела
        fdWalls.friction = 0.5f; // Что то вроде скольжения
        fdWalls.restitution = 0.5f; // Упругость
        fdWalls.shape = new PolygonShape();

        // параметры фигуры шарика
        fdBall = new FixtureDef();
        fdBall.density = 1.0f; // Плотность тела
        fdBall.friction = 0.5f; // Что то вроде скольжения
        fdBall.restitution = 0.5f; // Упругость
        fdBall.shape = new CircleShape();//sphereRadius / SCALE);
        fdBall.shape.setRadius(1);
    }




    private void setBodyDefs()
    {
        // параметры статичных объектов
        bdStatic = new BodyDef();
        bdStatic.type = BodyType.STATIC;

        // параметры движущихся объектов
        bdKinem = new BodyDef();
        bdKinem.type = BodyType.KINEMATIC;

        // параметры динамических объектов (шарика)
        bdDyn = new BodyDef();
        bdDyn.type = BodyType.DYNAMIC;
    }


    public void setGravity(float x, float y)
    {
        grav = new Vec2(GRAVITY_SCALE*x, GRAVITY_SCALE*y);
        // Log.d("getGravity", world.getGravity().x + " " + world.getGravity().y);
    }






    public void start()
    {
        // box.applyLinearImpulse(new Vec2(10, 10), boxDef.position);

        loop();
    }






    private void loop()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                world.setGravity(grav);
                world.step(1.0f / FPS, 10, 10);
                context.onBallPositionChange(bBall.getPosition().x, bBall.getPosition().y);
                try
                {
                    Thread.sleep(round(1.0f/SPEED_MULTIPLIER * 1000 / FPS));
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                world.clearForces();
                loop();
            }
        }).start();
    }





    /* стенки по краям уровня (всегда одинаковые) */
/*    private void createBorders(figureParams, bodyParams)
    {


        figureParams.shape = new b2PolygonShape;
        figureParams.shape.SetAsBox( 145/SCALE, 3 / SCALE);//Длина и ширина
        bodyParams.position.Set(160/SCALE, 284 / SCALE);//Нижняя граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);//Свяжем физическую часть и геометрическую и добавим в мир

        bodyParams.position.Set(160 / SCALE , 8 /SCALE );//верхняя граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);
        figureParams.shape.SetAsBox(3 / SCALE, 141 / SCALE);//изменить габариты прямоугольника

        bodyParams.position.Set(12/ SCALE, 146 / SCALE);//левая граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);
        bodyParams.position.Set(308 / SCALE, 146/SCALE);//правая граница
        world.CreateBody(bodyParams).CreateFixture(figureParams);
    };*/




}



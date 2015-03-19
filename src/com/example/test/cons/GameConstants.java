package com.example.test.cons;

public interface GameConstants {
	public static float D_CAMERA_HEIGHT = 480;
	public static float D_CAMERA_WIDTH = 800;
	/* The categories. */
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_PLAYER = 2;
	public static final short CATEGORYBIT_BULLET = 4;
	public static final short CATEGORYBIT_ENEMY = 8;
	public static final short CATEGORYBIT_ENEMYWALL = 16;
	public static final short CATEGORYBIT_PLAYERBODY = 32;
	public static final short CATEGORYBIT_PLAYERBULLET = 64;
	public static final short CATEGORYBIT_ENEMYBODY = 128;
	
	/* And what should collide with what. */
	public static final short MASKBITS_WALL = -1;
	public static final short MASKBITS_PLAYER = CATEGORYBIT_WALL;
	public static final short MASKBITS_BULLET = CATEGORYBIT_WALL + CATEGORYBIT_PLAYERBODY;
	public static final short MASKBITS_ENEMY = CATEGORYBIT_WALL + CATEGORYBIT_ENEMYWALL;
	public static final short MASKBITS_ENEMYWALL = CATEGORYBIT_ENEMY + CATEGORYBIT_WALL + CATEGORYBIT_ENEMYBODY;
	public static final short MASKBITS_PLAYERBODY = CATEGORYBIT_BULLET;
	public static final short MASKBITS_PLAYERBULLET = CATEGORYBIT_WALL + CATEGORYBIT_ENEMYBODY;
	public static final short MASKBITS_ENEMYBODY = CATEGORYBIT_WALL + CATEGORYBIT_ENEMYWALL + CATEGORYBIT_PLAYERBULLET;

}

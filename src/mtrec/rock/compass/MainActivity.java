package mtrec.rock.compass;

import mtrec.rock.campass.R;
import mtrec.rock.object.CompassView;
import android.R.bool;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private CompassView cam1;
	private CompassView cam2;
	private CompassView cam3;
	private CompassView cam4;
	private SensorEventListener mSensorListener;
	private SensorManager mSensorManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.cam1 = (CompassView) findViewById(R.id.CompassView01);
	    this.cam1.setCompassName("Noise-Reduced");
	    this.cam2 = ((CompassView)findViewById(R.id.CompassView02));
	    this.cam2.setCompassName("Magnetic Field");
	    this.cam3 = ((CompassView)findViewById(R.id.CompassView03));
	    this.cam3.setCompassName("Gyroscope");
	    this.cam4 = ((CompassView)findViewById(R.id.CompassView04));
	    this.cam4.setCompassName("Noisy");
	    
	    this.mSensorManager = ((SensorManager)getSystemService("sensor"));
	    this.mSensorListener = new SensorEventListener() {
	    	
	    	private int ARRAY_LENGTH = 100;
	        private float[] gyroDegreeArray = new float[ARRAY_LENGTH]; // For gyroscope
	        private float gyroDegree = 0; // For gyroscope
	        private int gyroCounter = 0;
	        private int gyroNegaNum = 0;
	        private long gyroTime = 0;

	        private float[] oriDegreeArray = new float[ARRAY_LENGTH]; // For ori
	        private float oriDegree = 0; // For ori
	        private int oriCounter = 0;
	        private int oriNegaNum = 0;
	        
	        private float minusAvg = 0; // For gyroscope

	        // custom
	        private float[] accelVals = new float[3];
	        private float[] mfVals = new float[3];

	        private float[] matrixR = new float[9];
	        private float[] matrixI = new float[9];
	        private float[] matrixVals = new float[3];
	        
			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
		        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	            	if (gyroNegaNum > 0 && oriNegaNum > 0)
	            	{
	            		// calculate average
	            		// float t = 0;
	            		// for(int i = 0; i < ARRAY_LENGTH; i++){
	            		// 	t += 180 * gyroDegreeArray[i] / Math.PI - oriDegreeArray[i];
	            		// }
	            		// minusAvg = t/ARRAY_LENGTH;

	            		// Log.d("avg", Float.toString(minusAvg));

	            		// low-pass filtering of correctional angle
	            		float total[] = new float[2];
	            		for (int i = 0; i < ARRAY_LENGTH; i++)
	            		{
	            			double deg = 180 * gyroDegreeArray[i] / Math.PI - oriDegreeArray[i];
	            			total[0] += Math.cos(deg);
	            			total[1] += Math.sin(deg);
	            		}
	            		double length = Math.sqrt((total[0] * total[0]) + (total[1] * total[1]));
	            		minusAvg = (float)Math.atan2(total[1], total[0]);
	            	}
		            MainActivity.this.cam1.setOrientation((float) (180 * gyroDegree / Math.PI) - minusAvg);
		        }
		        
		        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			       	MainActivity.this.cam2.setOrientation((float)(180.0D * -Math.atan2(event.values[1], event.values[0]) / Math.PI));
		        }

				// switch (event.sensor.getType())
				// {
				// 	case Sensor.TYPE_ACCELEROMETER:
				// 		for (int i = 0; i < 3; i++)
				// 		{
				// 			accelVals[i] = event.values[i];
				// 		}
				// 		break;
				// 	case Sensor.TYPE_MAGNETIC_FIELD:
				// 		for (int i = 0; i < 3; i++)
				// 		{
				// 			mfVals[i] = event.values[i];
				// 		}
				// 		break;
				// }
		  //       boolean success = SensorManager.getRotationMatrix(matrixR, matrixI, accelVals, mfVals);
		  //       if (success)
		  //       {
		  //       	SensorManager.getOrientation(matrixR, matrixVals);
		  //       	double azimuth = Math.toDegrees(matrixVals[0]);
		  //       	double pitch = Math.toDegrees(matrixVals[1]);
		  //       	double roll = Math.toDegrees(matrixVals[2]);
		  //       	MainActivity.this.cam1.setOrientation((float)azimuth);
		  //       }

		  //       MainActivity.this.cam2.setOrientation((float)1.0);


		        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
		        	oriDegree = (event.values[0] + 90 < 360)? (event.values[0] + 90.0F) : (event.values[0] + 90 - 360);
	            	if(oriCounter >= ARRAY_LENGTH){
	            		Log.d("Ori: " + Integer.toString(oriCounter), "Over");
	            		oriCounter = oriCounter % ARRAY_LENGTH;
	            		oriNegaNum ++;
	            	}
	            	oriDegreeArray[oriCounter] = oriDegree;
            		Log.d("Ori: " + Integer.toString(oriCounter), Float.toString(oriDegreeArray[oriCounter]));
	            	if(oriCounter <= gyroCounter){
		            	oriCounter++;
	            	}
	            	else{
	            		
	            	}
		            MainActivity.this.cam4.setOrientation(-event.values[0] - 90.0F);

		        }
		        
		        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
		            long l = System.currentTimeMillis();
		            if (this.gyroTime > 0L){
		            	this.gyroDegree += event.values[2] * (float)(l - this.gyroTime) / 1000;
		            	if(gyroCounter >= ARRAY_LENGTH){
		            		gyroCounter = gyroCounter % ARRAY_LENGTH;
		            		gyroNegaNum ++;
		            	}
	            		gyroDegreeArray[gyroCounter] = gyroDegree;
	            		Log.d("Gyro: " + Integer.toString(gyroCounter), Double.toString(180 * gyroDegreeArray[gyroCounter] / Math.PI));
		            	if(oriCounter >= gyroCounter){
			            	gyroCounter++;
		            	}
		            	MainActivity.this.cam3.setOrientation((float) (180 * gyroDegree / Math.PI));

		            }
		            this.gyroTime = l;
		        }
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
		
	    this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(1), SensorManager.SENSOR_DELAY_NORMAL);
	    this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(2), SensorManager.SENSOR_DELAY_NORMAL);
	    this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(4), SensorManager.SENSOR_DELAY_NORMAL );
	    this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(3), SensorManager.SENSOR_DELAY_NORMAL );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.example.android.soundrec;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.MediaRecorder;

public class Soundrec extends Activity implements OnClickListener{
	MediaRecorder recorder = new MediaRecorder();
	int recCount=0;
	
	View startButton,stopButton;
	Spinner selectSpinner;
    RadioGroup radioGroup;
    //String path;
    
	/** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        /* start */
        startButton = this.findViewById(R.id.Button01);
        startButton.setOnClickListener(this);
        
        /* stop */
        stopButton = this.findViewById(R.id.Button02);
        stopButton.setOnClickListener(this);

        /* Encoder */
        ArrayAdapter <String> arrayAdapterE = new ArrayAdapter <String> (this, android.R.layout.simple_spinner_item);
        arrayAdapterE.add("AMR-NB");
        arrayAdapterE.add("AMR-WB");
        arrayAdapterE.add("AAC");
        arrayAdapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectSpinner = (Spinner) findViewById(R.id.encoder);
        selectSpinner.setAdapter(arrayAdapterE);

        /* Sampling rate */
        ArrayAdapter <String> arrayAdapterS = new ArrayAdapter <String> (this, android.R.layout.simple_spinner_item);
        arrayAdapterS.add("8KHz");
        arrayAdapterS.add("16KHz");
        arrayAdapterS.add("44.1KHz");
        arrayAdapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectSpinner = (Spinner) findViewById(R.id.smpfreq);
        selectSpinner.setAdapter(arrayAdapterS);
        
        /* mono , stereo */
    }

    /* �{�^���������ꂽ�Ƃ��̏��� */
    public void onClick(View v){
   	
		if(v==startButton){
			View txtResult;
			/* Start Button�������ꂽ�Ƃ� */
			txtResult = this.findViewById(R.id.TextView01);
			((TextView)txtResult).setText("Pushed Start Button!!");

			int channel;
			{
				RadioButton radio = (RadioButton)findViewById(R.id.radio0);
    			if(radio.isChecked() == true){
    				channel = 1;
    			} else{
    				channel = 2;
    			}
        		Log.i("@@@","channel = " + channel);
			}
			
	        int freq;
	        {
    	        selectSpinner = (Spinner) findViewById(R.id.smpfreq);
    	        int smpFreq=selectSpinner.getSelectedItemPosition();
    	        if(0==smpFreq){
            		freq = 8000;
    	        } else if(1==smpFreq){
            		freq = 16000;
    	        } else if(2==smpFreq){
            		freq = 44100;
    	        } else {
    				return;
    	        }
        		Log.i("@@@","freq = " + freq);
	        }

	        int format;
	        {
	        	selectSpinner = (Spinner) findViewById(R.id.encoder);
	        	int Encoder=selectSpinner.getSelectedItemPosition();
	        	if(0==Encoder){
	        		Log.i("@@@","AMR-NB");
	        		format = MediaRecorder.AudioEncoder.AMR_NB;
	        	} else if(1==Encoder){
	        		Log.i("@@@","AMR-WB");
	        		format = MediaRecorder.AudioEncoder.AMR_WB;
	        	} else if(2==Encoder){
            		Log.i("@@@","AAC");
                  	format = MediaRecorder.AudioEncoder.AAC;
    			} else {
    				return;
    			}
	        }
			
			try {
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setOutputFile("/sdcard/SoundRec" + recCount++ + ".3gp");
			} catch (IllegalStateException e) {
				txtResult = this.findViewById(R.id.TextView01);
				((TextView)txtResult).setText("Pushed Start Button!! Error A");
				return;				
			}

        	try {
        		recorder.setAudioEncodingBitRate(128000);
        		recorder.setAudioChannels(channel);
        		recorder.setAudioSamplingRate(freq);
              	recorder.setAudioEncoder(format);
        		recorder.prepare();

        	}catch (IOException e){
        		txtResult = this.findViewById(R.id.TextView01);
        		((TextView)txtResult).setText("Pushed Start Button!! prepare A Error");
        		return;
        	}catch (IllegalStateException f){
        		txtResult = this.findViewById(R.id.TextView01);
        		((TextView)txtResult).setText("Pushed Start Button!! prepare B Error");
        		return;
        	}
        	 
        	try {
        		recorder.start();   // Recording is now started
        	}catch (IllegalStateException e){        		 
        		txtResult = this.findViewById(R.id.TextView01);
        		((TextView)txtResult).setText("Pushed Start Button!! start Error");
        		return;
        	}
        	
		} else {
			View txtResult;
			/* Stop Button�������ꂽ�Ƃ� */
			txtResult = this.findViewById(R.id.TextView01);
			((TextView)txtResult).setText("Pushed Stop Button!!");
			
	    	try {
	    		recorder.stop();
	    	}catch (IllegalStateException e){
				txtResult = this.findViewById(R.id.TextView01);
				((TextView)txtResult).setText("Pushed Stop Button!!stop Error = " + e.getMessage());
	    	}finally {
	    		recorder.reset();
	    		//recorder.release();			
	    	}
    	}
	}
}
	
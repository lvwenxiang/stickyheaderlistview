/**
 * 
 */
package com.example.activity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.util.VoiceRecorder;
import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;

/**
 * @author wenxiang.lv
 * 
 */
public class ChatActivity extends Activity {
	private View buttonSetModeVoice;
	private View mainLayout;
	private RelativeLayout edittext_layout;
	private View buttonPressToSpeak;
	private View buttonSetModeKeyboard;
	// �������� �����Ŀ�
	private View recordingContainer;
	private TextView recordingHint;
	private VoiceRecorder voiceRecorder;
	private PowerManager.WakeLock wakeLock;
	private ImageView micImage;
	private Drawable[] micImages;
	
	
    private int BASE = 600;  
    private int SPACE = 200;// ���ȡ��ʱ��  
	 private MediaRecorder recorder=null;
	private static class MyHandler extends Handler {
		private final WeakReference<ChatActivity> mActivity;
		private onTaskListener listener;

		public MyHandler(ChatActivity activity, onTaskListener listener) {
			mActivity = new WeakReference<ChatActivity>(activity);
			this.listener = listener;
		}

		public interface onTaskListener {
			public void task(android.os.Message msg);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			System.out.println(msg);
			if (mActivity.get() == null) {
				return;
			}
			if (listener != null) {
				listener.task(msg);
			}
		}
	}

	private MyHandler micImageHandler = new MyHandler(this,
			new MyHandler.onTaskListener() {

				@Override
				public void task(android.os.Message msg) {
					// TODO Auto-generated method stub
					try {
						MyLog.e("TAG", "==="+msg.what);	
						  if(msg.what>14){
							  msg.what=14;
				            }
						micImage.setImageDrawable(micImages[msg.what]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainLayout = LayoutInflater.from(this).inflate(
				R.layout.activity_chat_lv, null);
		setContentView(mainLayout);

		initView(mainLayout);
		setUpView();
	}

	@SuppressWarnings("deprecation")
	private void initView(View mainLayout) {
		buttonSetModeVoice = mainLayout.findViewById(R.id.btn_set_mode_voice);
		edittext_layout = (RelativeLayout) mainLayout
				.findViewById(R.id.edittext_layout);
		// ��ס˵��
		buttonPressToSpeak = mainLayout.findViewById(R.id.btn_press_to_speak);
		buttonSetModeKeyboard = mainLayout
				.findViewById(R.id.btn_set_mode_keyboard);
		
		//
		micImage = (ImageView) mainLayout.findViewById(R.id.mic_image);
		recordingContainer = mainLayout.findViewById(R.id.recording_container);
		recordingHint = (TextView) mainLayout.findViewById(R.id.recording_hint);

		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());

		// ������Դ�ļ�,����¼������ʱ
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };
		voiceRecorder = new VoiceRecorder(micImageHandler);
	}

	private void setUpView() {
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

	}
	 
    private Runnable mUpdateMicStatusTimer = new Runnable() {  
        public void run() {  
            updateMicStatus();  
        }  
    }; 
    
    //ʹ��ϵͳ��¼��
	private void updateMicStatus() {  
	         if(recorder!=null){
//	        	 �����������Ҫ���һ��ʱ�����һ�εģ�Ҳ����˵����Ҫ�����߳�������õġ���һ�ε��û᷵��0��
	             int ratio = recorder.getMaxAmplitude() / BASE;  
	             int db = 0;// �ֱ�   
	             if (ratio > 1)  
	                 db = (int) (20 * Math.log10(ratio));  
	                System.out.println("�ֱ�ֵ��"+db+"     "+Math.log10(ratio));  
	                //�Ҷ����ֻ�˵����������ʱ��db�ﵽ��35���ң�
	                micImageHandler.postDelayed(mUpdateMicStatusTimer, SPACE);  
	                //���Գ���2��Ϊ�ľ��Ƕ�Ӧ14��ͼƬ
	                micImageHandler.sendEmptyMessage(db);
	         }
	     }
	class PressToSpeakListen implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
//               ����ϵͳ��		MediaRecorder		
//				recorder=new MediaRecorder();
//	            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//������Դ�ǻ�Ͳ
//                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//���ø�ʽ
//                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//���ý��뷽ʽ
//                recorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        +File.separator+"luyin.3gp");
//                try {
//					recorder.prepare();
//				} catch (IllegalStateException | IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				recorder.start();
//				updateMicStatus();		
				
				
				v.setPressed(true);
				recordingContainer.setVisibility(View.VISIBLE);
				recordingHint.setText(getString(R.string.move_up_to_cancel));
				recordingHint.setBackgroundColor(Color.TRANSPARENT);
				voiceRecorder.startRecording(null, "", getApplicationContext());

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
//				MyLog.e("TAG", "getVoiceFilePath==="+voiceRecorder.getVoiceFilePath());	
				
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				}else{
					
					
//					int length = voiceRecorder.stopRecoding();
				
					
				}
				return true;

			default:

				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}

	}

	/**
	 * ���¼���ͼ��
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		edittext_layout.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
	}

	/**
	 * ��������ͼ�갴ť
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		edittext_layout.setVisibility(View.GONE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
	}
}

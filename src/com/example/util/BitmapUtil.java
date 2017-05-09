package com.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/*20170223
 * 
 * lvwenxiang
 * */

public class BitmapUtil {
	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap getBitmap(byte[] data, int width, int height) {
		Bitmap bitmap = null;
		if (data != null) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			int xScale = opts.outWidth / width;
			int yScale = opts.outHeight / height;

			opts.inJustDecodeBounds = false;
			opts.inSampleSize = Math.max(xScale, yScale);// 设置采样率

			opts.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
			opts.inPurgeable = true;// 同时设置才会有效
			opts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		}
		return bitmap;
	}

	// public static Bitmap getBitmap(String path) {
	// return BitmapFactory.decodeFile(path);
	// }

	public static boolean save(Bitmap bitmap, File file) throws IOException {
		if (bitmap != null && file != null) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream out = new FileOutputStream(file);
			boolean result = bitmap.compress(CompressFormat.JPEG, 100, out);
			return result;
		}
		return false;
	}

	/**
	 * 通过路径获取文件
	 * 
	 * @param path
	 * @param mContext
	 * @return
	 */
	public static File createFile(String path, Context context) {//
		// path=http:/183.62.134.115:8083/LightKey_Business/resource/upload/user/351/20160905095738_998596.jpg
		// 截取path = resource/upload/user/351/20160905095738_998596.jpg
		if (path != null && path.contains("resource")) {// 解决金立等有些手机因为.:而不能保存文件的问题
			int index = path.indexOf("resource");
			path = path.substring(index);
		}
		File result = new File(context.getExternalCacheDir(), path);

		return result;
	}

}

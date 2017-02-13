package com.example.extra;

import java.io.IOException;

import com.example.activity.TApplication;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PicassoCacheInterceptor implements Interceptor {

	
	public PicassoCacheInterceptor() {
	 	
	}
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
	if (TApplication.isNetWorkAvailable()) {
				Response response = chain.proceed(request);
				// read from cache for 60 s
				int maxAge = 300;
				String cacheControl = request.cacheControl().toString();
				MyLog.e("TAG", maxAge + "s load cahe:" + cacheControl);
				return response.newBuilder().removeHeader("Pragma")
					.removeHeader("Cache-Control")
						.header("Cache-Control", "public, max-age=" + maxAge)
						.build();
			} else {
			MyLog.e("TAG", " no network load cahe");
				request = request.newBuilder()
					.cacheControl(CacheControl.FORCE_CACHE).build();
		Response response = chain.proceed(request);
		// set cahe times is 3 days
		int maxStale = 60 * 60 * 24 * 3;
			return response
					.newBuilder()
				.removeHeader("Pragma")
				.removeHeader("Cache-Control")
					.header("Cache-Control",
						"public, only-if-cached, max-stale=" + maxStale)
				.build();
		}
}

}

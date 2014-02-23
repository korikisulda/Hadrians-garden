package net.korikisulda.hadriangarden.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class ConvenientGet extends ConvenientRequest{
	@Override
	public HttpRequestBase getMethod() {
		return new HttpGet(URL);
	}
}

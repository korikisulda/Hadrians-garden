package net.korikisulda.hadriangarden.http;

import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;

public class ConvenientHead extends ConvenientRequest{
	@Override
	public HttpRequestBase getMethod() {
		return new HttpHead(URL);
	}
}

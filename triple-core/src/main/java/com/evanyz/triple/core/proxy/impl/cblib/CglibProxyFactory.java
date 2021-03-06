package com.evanyz.triple.core.proxy.impl.cblib;

import com.evanyz.triple.core.net.domain.TripleRequest;
import com.evanyz.triple.core.proxy.AbstractProxyFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * Created by evan on 2018/11/11.
 */
public class CglibProxyFactory extends AbstractProxyFactory {

    @Override public <T> T getProxy(Class<T> clazz) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setClassLoader(clazz.newInstance().getClass().getClassLoader());
            enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
                TripleRequest request = new TripleRequest();
                request.setServiceName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParams(objects);
                return (T)handler(request).getData();
            });
            return (T)enhancer.create();
        } catch (Exception e) {
            throw new RuntimeException("生成cglib代理失败", e);
        }
    }
}

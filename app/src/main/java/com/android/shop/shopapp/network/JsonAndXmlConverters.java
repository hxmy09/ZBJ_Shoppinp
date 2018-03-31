/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.shop.shopapp.network;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Both the Gson converter and the Simple Framework converter accept all types. Because of this,
 * you cannot use both in a single service by default. In order to work around this, we can create
 * an @Json and @Xml annotation to declare which serialization format each endpoint should use and
 * then write our own Converter.Factory which delegates to either the Gson or Simple Framework
 * converter.
 */
public final class JsonAndXmlConverters
{
    @Retention(RUNTIME)
    public @interface Json
    {
    }

    @Retention(RUNTIME)
    public @interface Xml
    {
    }

    public static class QualifiedTypeConverterFactory extends Converter.Factory
    {
        private final Converter.Factory jsonFactory;
        private final Converter.Factory xmlFactory;

        public QualifiedTypeConverterFactory(Converter.Factory jsonFactory, Converter.Factory xmlFactory)
        {
            this.jsonFactory = jsonFactory;
            this.xmlFactory = xmlFactory;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit)
        {
            for (Annotation annotation : annotations)
            {
                if (annotation instanceof Json)
                {
                    return jsonFactory.responseBodyConverter(type, annotations, retrofit);
                }
                if (annotation instanceof Xml)
                {
                    return xmlFactory.responseBodyConverter(type, annotations, retrofit);
                }
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit)
        {
            for (Annotation annotation : parameterAnnotations)
            {
                if (annotation instanceof Json)
                {
                    return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
                }
                if (annotation instanceof Xml)
                {
                    return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
                }
            }
            return null;
        }
    }

}
